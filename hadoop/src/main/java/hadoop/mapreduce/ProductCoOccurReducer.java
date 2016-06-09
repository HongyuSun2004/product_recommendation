package hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * It's a simple sum reducer
 */
public class ProductCoOccurReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	//reuse the SUM object
	private IntWritable SUM = new IntWritable();

	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		
		for (IntWritable value : values) {
			sum += value.get();
		}

		SUM.set(sum);
		context.write(key, SUM);
	}

}
