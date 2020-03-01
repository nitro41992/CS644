
import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class cards {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration config = new Configuration();
		Job job = Job.getInstance(config, "Finding Missing Cards");

		job.setJarByClass(cards.class);
		job.setMapperClass(map.class);
		job.setReducerClass(reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean status = job.waitForCompletion(true);
		if (status) {
			System.exit(0);
		} else {
			System.exit(1);
		}

	}

	// extend mapper class
	public static class map extends Mapper<LongWritable, Text, Text, IntWritable> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String row = value.toString();
			String[] subRow = row.split(",");
			Text suit = new Text(subRow[0]);
			IntWritable intWritable = new IntWritable(Integer.parseInt(subRow[1]));
			context.write(suit, intWritable);

		}
	}

	// extend reducer class
	public static class reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterable<IntWritable> value, Context context)
				throws IOException, InterruptedException {

			ArrayList<Integer> deck = new ArrayList<Integer>();

			for (int i = 1; i < 13; i++) {
				deck.add(i);
			}

			int counts = 0, rank = 0;
			int size = 0;

			for (IntWritable cards : value) {
				counts++;
				rank = cards.get();
				if (deck.contains(rank)) {
					int index = deck.indexOf(rank);
					deck.remove(index);
				}
			}

			counts = 12 - counts;
			if (counts <= 12 && counts >= 0) {
				size = deck.size();
				int counter = 0;
				while (counter < size) {
					IntWritable intWritable = new IntWritable(deck.get(counter));
					context.write(key, intWritable);
					counter++;
				}
			}
		}
	}

}