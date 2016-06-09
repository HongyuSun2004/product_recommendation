package hadoop.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ProductIndicatorReducer  extends Reducer<Text, Text, Text, Text>{
	//reuse the following objects
	private StringBuilder sb = new StringBuilder();
	private Text indicator = new Text();

	//define the comparator to compare the products by co-occur number
	private Comparator<String> comparator = new Comparator<String>(){
		@Override
		public int compare(String p1, String p2) {
			//get the first product co-occur number
			String[] a = p1.split(",");
			int coOccurNumber1 = Integer.parseInt(a[1]);

			//get the second product co-occur number
			String[] b = p2.split(",");
			int coOccurNumber2 = Integer.parseInt(b[1]);

			//compare the co-occur numbers
			return coOccurNumber2 - coOccurNumber1;
		}
	};

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		//put the products in a list
		List<String> productList = new ArrayList<String>(); 
		for (Text value : values) {
			String valueStr = value.toString();
			if(valueStr.length() > 0){
				productList.add(valueStr);
			}
		}

		//sort the product list by co-occur number descend
		Collections.sort(productList, comparator);
		
		//create sorted product list with co-occur number
		sb.setLength(0);
		for(String product : productList){
			String[] a = product.split(",");
			sb.append(a[0]);
			sb.append("(");
			sb.append(a[1]);
			sb.append("),");
		}

		//output the final product indicator list
		indicator.set(sb.toString());
		context.write(key, indicator);
	}
}
