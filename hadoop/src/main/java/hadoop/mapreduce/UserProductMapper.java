package hadoop.mapreduce;

import java.io.IOException;

import parser.LogFileLineParser;
import org.apache.hadoop.conf.Configuration;
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
	private LogFileLineParser logFileLineParser;

	private String logFileLineParserClassName;


	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		logFileLineParserClassName = conf.get("logFileLineParserClass", "parser.SampleLogFileLineParser");

		logFileLineParser = instantiate(logFileLineParserClassName, LogFileLineParser.class);
	}

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString();
		if(line.length() > 0){
			logFileLineParser.setLogFileLine(line);
			if(logFileLineParser.containsProduct(line)) {
				String productID = logFileLineParser.parseProductID(line);

				if(productID != null){
					String sessionID = logFileLineParser.parseSessionID(line);
					session.set(sessionID);
					product.set(productID);

					context.write(session, product);
				}
			}
		}
	}

	private <T> T instantiate(final String className, final Class<T> type){
		try{
			return type.cast(Class.forName(className).newInstance());
		} catch(final InstantiationException e){
			throw new IllegalStateException(e);
		} catch(final IllegalAccessException e){
			throw new IllegalStateException(e);
		} catch(final ClassNotFoundException e){
			throw new IllegalStateException(e);
		}
	}

}
