package hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * convert the products pair to the product1 and product2 with co-occur number
 */

public class ProductIndicatorMapper extends Mapper<Text, Text, Text, Text>{
	private int relevanceLevel = 2;

	//reuse the following objects
	private Text product1 = new Text();
	private Text product2 = new Text();
	private Text product1WithCoOccurNumber = new Text();
	private Text product2WithCoOccurNumber = new Text();

	@Override
	public void setup(Context context) {
		// Get the configured relevanceLevel value.
		// This is depends on the input data set size.
		Configuration conf = context.getConfiguration();
		relevanceLevel = conf.getInt("relevanceLevel", 2);
	}
	
	@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		
		String valueStr = value.toString();
		
		if(valueStr.length() > 0){
			int intValue = Integer.parseInt(valueStr);
			
			if(intValue >= relevanceLevel){
				String[] products = key.toString().split(",");

				product1.set(products[0]);
				product2.set(products[1]);

				product1WithCoOccurNumber.set(products[0] + "," + intValue);
				product2WithCoOccurNumber.set(products[1] + "," + intValue);

				//out put two records with each product as key, and another product and co-occur number as value
				context.write(product1, product2WithCoOccurNumber);
				context.write(product2, product1WithCoOccurNumber);
			}
		}
	}
}
