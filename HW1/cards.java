
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
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableMapper;
import org.apache.hadoop.hbase.TableMapReduceUtil;

public class cards {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// Configuration config = new Configuration();

		Configuration conf = HbaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		Admin admin = conn.getAdmin();
		HTableDescriptor htdesc = new HTableDescriptor(TableName.valueOf("MissingCards"));
		HColumnDescriptor hcdesc = new HColumnDescriptor(Bytes.toBytes("cf"));
		htdesc.addFamily(hcdesc);
		admin.createTable(htdesc);

		Job job = Job.getInstance(conf, "Finding Missing Cards");

		job.setJarByClass(cards.class);
		job.setMapperClass(map.class);
		job.setReducerClass(reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		TableMapReduceUtil.initTableReducerJob("cards", reduce.class, job);

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
			Text suit = new Text(subRow[2]);
			IntWritable intWritable = new IntWritable(Integer.parseInt(subRow[1]));
			context.write(suit, intWritable);

		}
	}

	// extend reducer class
	public static class reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		IntWritable result = new IntWritable();
		public static final byte[] CF = "cf".getBytes();
		public static final byte[] SUITE = "suite".getBytes();
		public static final byte[] NUMBER = "number".getBytes();

		public void reduce(Text key, Iterable<IntWritable> value, Context context)
				throws IOException, InterruptedException {

			ArrayList<Integer> deck = new ArrayList<Integer>();

			for (int i = 1; i <= 13; i++) {
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
					IntWritable result = new IntWritable(deck.get(counter));
					context.write(key, result);
					counter++;
				}
			}
			Put put = new Put(Bytes.toBytes(key.toString()));
			put.addColumn(CF, SUIT, Bytes.toBytes(key.toString()));
			put.addColumn(CF, NUMBER, Bytes.toBytes(result.toString()));
			context.write(null, put);
		}
	}

}