package hadoop.mapreduce;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce the user - products list. Keep unique product for each user session.
 */
public class UserProductReducer extends Reducer<Text, Text, Text, Text> {

	//reuse the following objects in the reducer
	private StringBuilder sb = new StringBuilder();
	private Set<String> productSet = new HashSet<String>();
	private Text products = new Text();

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		// build unique product set
		productSet.clear();
		for (Text value : values) {
			String valueStr = value.toString();
			if (valueStr.length() > 0) {
				productSet.add(valueStr);
			}
		}

		if (productSet.size() > 1) {
			//combine the product list
			sb.setLength(0);
			for (String product : productSet) {
				sb.append(product + ",");
			}

			products.set(sb.toString());
			context.write(key, products);
		}
	}
}