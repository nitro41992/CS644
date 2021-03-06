package PokerCard;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;


public class PokerCard {
	// mapper function
	public static class cardMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		public void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
			String line = values.toString();
			String[] split = line.split(",");
			context.write(new Text(split[2]), new IntWritable(Integer.parseInt(split[1])));
		}
	}
	
	
	public static class cardReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>  {
		IntWritable result = new IntWritable();
		public static final byte[] CF = "cf".getBytes();
		public static final byte[] SUIT = "suit".getBytes();
		public static final byte[] NUMBER = "number".getBytes();
		
	 	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
	 		ArrayList<Integer> nums = new ArrayList<Integer>();
	 		
	 					int sum = 0;
	 					int temp = 0;
	 		
	 					for (IntWritable val : values) {
	 						sum += val.get();
	 						temp = val.get();
	 						nums.add(temp);
	 					}
	 		
	 					if (sum < 91) {
	 						for (int i = 1; i <= 13; i++) {
	 							if (!nums.contains(i))
	 								result.set(i);
	 							// context.write(key, new IntWritable(i));
	 						}
	 					}

	 					Put put = new Put(Bytes.toBytes(key.toString()));
	 					put.addColumn(CF, SUIT, Bytes.toBytes(key.toString()));
	 					put.addColumn(CF, NUMBER, Bytes.toBytes(result.toString()));
	 					context.write(null, put);
	   	}
	}


	// main function
	public static void main(String[] args) throws Exception {

		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		
		Admin admin = conn.getAdmin();
		
		HTableDescriptor htdesc = new HTableDescriptor(TableName.valueOf("PokerCard"));
		HColumnDescriptor hcdesc = new HColumnDescriptor(Bytes.toBytes("cf"));
		htdesc.addFamily(hcdesc);
		admin.createTable(htdesc);
		
		Job job = Job.getInstance(conf, "Finding Missing Cards");

		job.setJarByClass(PokerCard.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		// configure mapper and reducer
		job.setMapperClass(cardMapper.class);

		TableMapReduceUtil.initTableReducerJob("PokerCard", cardReducer.class, job);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}