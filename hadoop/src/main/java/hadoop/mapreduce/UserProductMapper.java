package hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Filter the log file line and parse the user session and product ID for each line
 */
public class UserProductMapper extends Mapper<LongWritable, Text, Text, Text> {
	
    //reuse the following objects
	private Text session = new Text();
    private Text product = new Text();

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString();
		
		if(containsProduct(line)) {
			String productID = parseProductID(line);
			
			if(productID != null){
				String sessionID = parseSessionID(line);
				session.set(sessionID);
				product.set(productID);
				
				context.write(session, product);
			}
			
		}

	}

	private String parseProductID(String line) {
		int a = line.indexOf("sc.do");
		int aaa = line.indexOf("i=&", a);
		int bbb = line.indexOf("c=&", a);
		int ccc = line.indexOf("v=&", a);

		if (aaa >= 0 || bbb >= 0 || ccc >= 0) {
			return null;
		}

		int i = line.indexOf("i=", a);
		int c = line.indexOf("c=", a);
		int v = line.indexOf("v=", a);

		if (i < 0 || c < 0 || v < 0) {
			return null;
		}

		int i2 = line.indexOf("&", i);
		String ii;
		if (i2 > 0) {
			ii = line.substring(i, i2);
		} else {
			ii = line.substring(i);
		}

		int c2 = line.indexOf("&", c);
		String cc;
		if (c2 > 0) {
			cc = line.substring(c, c2);
		} else {
			cc = line.substring(c);
		}

		int v2 = line.indexOf("&", v);
		String vv;
		if (v2 > 0) {
			vv = line.substring(v, v2);
		} else {
			vv = line.substring(v);
		}

		return ii + ":" + cc + ":" + vv;
	}  

	private String parseSessionID(String line) {
		int a = line.indexOf("]");
		int b = line.indexOf(":", a);

		String sessionID = line.substring(a + 2, b - 1);
		int c = sessionID.indexOf("{");

		if (c == 0) {
			sessionID = sessionID.substring(1, sessionID.length() - 1);
		}
		return sessionID;
	}
    
	private boolean containsProduct(String line){
		return line.indexOf("/sc.do") >= 0
				&& line.indexOf("com.nlg.web.interceptor.ActionLoggingInterceptor") >= 0
				&& line.indexOf("proxyhandler") < 0
				&& line.indexOf("login.do") < 0;
	}

}
