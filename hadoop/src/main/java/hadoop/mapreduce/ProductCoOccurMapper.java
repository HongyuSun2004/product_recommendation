package hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Map the product list to product pairs with co-occur number 1
 */
public class ProductCoOccurMapper extends Mapper<Text, Text, Text, IntWritable>{

    //reuse the following objects
	private Text PRODUCT = new Text();
    private IntWritable ONE = new IntWritable(1);

	@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		 
		String[] products = value.toString().split(",");
		int size = products.length;
		
		//double loop to produce product pairs
		if(size > 1){
			for(int i = 0; i < size -1; i++){
				for(int j = i + 1; j < size; j++){
					String product1 = products[i];
					String product2 = products[j];
					if(product1.length() > 0 && product2.length() > 0){
						if(product1.compareTo(product2) < 0){
							PRODUCT.set(product1 + "," + product2); 
						}else{
							PRODUCT.set(product2 + "," + product1); 
						}
						context.write(PRODUCT, ONE);
					}
				}
			}
			
		}
	}

}
