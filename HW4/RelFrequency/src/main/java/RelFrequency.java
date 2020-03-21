
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class RelFrequency{
	
	// Creating a Comparable class to compare pairs and relative frequencies
	public static class Pair implements Comparable<Pair> 
	{
		double Frequency;
		String key;

		Pair(double Frequency, String key) 
		{
			this.Frequency = Frequency;
			this.key = key;
			  
		}

		@Override
		public int compareTo(Pair pair) 
		{
			
			if(this.Frequency <= pair.Frequency)
			{
				return 1;
			}
			else 
			{
				return -1;
			}
			
		}
	}
	
	public static class Map extends Mapper<LongWritable, Text, Text, LongWritable>
	{
            
            private Text wordPair = new Text();
            private LongWritable frequency = new LongWritable(1);

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
		{
	
		    // Converts all characters to lower case and uses regex to remove special characters and numbers			    
	        String line = value.toString().toLowerCase().replaceAll("[^a-z _+]", "");
	        
	        // Split line into tokens based on space delimiter
		    StringTokenizer tokenizer = new StringTokenizer(line);
		    List<String> words = new ArrayList<String>();
		    
		    // Loop through all words
		    while(tokenizer.hasMoreElements())
		    {
		    	// Iterating through each token within while loop
		    	String word = new String(tokenizer.nextToken());
		    	
		    	// Trimming white spaces and adding word to words list
		    	word = word.trim();
		    	words.add(word);
		    	
		    }
		    
		    // Looping through all words
		    for (int i = 0; i < words.size(); i++)
			{
		    	// Setting and writing single word pairs with * as 1st word 
		    	wordPair.set("*"+ " " + words.get(i).trim());
    	    	context.write(wordPair, frequency);
			}
			
		    for (int i = 1; i < words.size(); i++)
		    {   
		    	// Setting and writing neighboring words as pairs
		    	wordPair.set(words.get(i-1).trim() + " " + words.get(i).trim());
		    	context.write(wordPair,frequency);
		    	
		    }
			
	
	    }

	}
	
	public static class Combiner extends Reducer<Text, LongWritable, Text, LongWritable>
	{
		private LongWritable frequency = new LongWritable();
		
	    public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException
		{
			long sum = 0;
			
			// Loops through all frequencies based on the matching keys, sums frequency values and passes combined frequencies to the reducer
			for (LongWritable value : values)
			{
				sum = sum + value.get();
			}
			
			frequency.set(sum);
			
			context.write(key, frequency);
		}

	}
    
	// Establishing TreeSet to maintain a dictionary of unique key: value pairs
	public static TreeSet<Pair> ts = new TreeSet<Pair>();

	public static class Reduce extends Reducer<Text, LongWritable, Text, Text> 
	{

		private DoubleWritable Frequency = new DoubleWritable();
		
		// Establishing a HashMap to convert data into a data frame of key: value pairs
		private HashMap<String,Double> hashMap = new HashMap<String, Double>();
		

		private double getCount(Iterable<LongWritable> values) 
		{
			double count = 0;
			
			for (LongWritable value : values) 
			{
				count += value.get();
			}
			
			return count;
			
		}

		public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException 
		{
			// Extracting single words for single word frequencies based on the existence of *
			if (key.toString().split(" ")[0].equals("*")) 
			{
				double wordFrequency = 0.0;
				String word = key.toString().split(" ")[1];
				
				// Getting frequency of individual words and adding to HashMap
				wordFrequency= getCount(values);        
				hashMap.put(word, wordFrequency);
			}
			else 
			{
				// Extracting first word from word pair
				String wordA = key.toString().split(" ")[0];
				
				// Getting frequency of word pair from combiner
				double pairFrequency = getCount(values);
				
				// Getting frequency of individual word from HashMap
				double wordAFrequency = hashMap.get(wordA);
				
				// Calculating relative frequency of word pair
				Frequency.set((double) pairFrequency / wordAFrequency);  
				Double FinalFrequency = Frequency.get();
				
				// Adding the top 100 word pairs and associated word pairs to TreeSet
				if(FinalFrequency != 1.0d)
				{
					ts.add(new Pair(FinalFrequency, key.toString()));
					
					if (ts.size() > 100) 
					{
						ts.pollLast(); 
					} 
				}
			
			} 
		}
		
		
		// Collecting output and writing to output
		protected void cleanup(Context context) throws IOException, InterruptedException 
		{
			while (!ts.isEmpty()) 
				{
					Pair pair = ts.pollFirst();
	        		context.write(new Text(pair.key), new Text(Double.toString(pair.Frequency)));
				}
		}

	}
	
	
	public static void main(String[] args) throws Exception 
	{   
		Configuration config = new Configuration();
		Job wordFreq = Job.getInstance(config, "Find Top 100K words...");
		
		wordFreq.setJarByClass(RelFrequency.class);
		wordFreq.setMapperClass(Map.class);
		wordFreq.setCombinerClass(Combiner.class);
		wordFreq.setReducerClass(Reduce.class);
		

		FileInputFormat.addInputPath(wordFreq, new Path(args[0]));
		FileOutputFormat.setOutputPath(wordFreq, new Path(args[1]));

		wordFreq.setMapOutputKeyClass(Text.class);
		wordFreq.setMapOutputValueClass(LongWritable.class);
		wordFreq.setOutputKeyClass(Text.class);
		wordFreq.setOutputValueClass(LongWritable.class);

		boolean status = wordFreq.waitForCompletion(true);
        if(status){
        	System.exit(0);
        }
        else{
        	System.exit(1);
        }

	}

}