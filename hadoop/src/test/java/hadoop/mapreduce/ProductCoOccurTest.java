package hadoop.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;

import org.junit.Before;
import org.junit.Test;

/**
 * Product CoOccur MRUnit Test
 */
public class ProductCoOccurTest {

  /*
   * Declare harnesses that let you test a mapper, a reducer, and
   * a mapper and a reducer working together.
   */
    private MapDriver<Text, Text, Text, IntWritable> mapDriver;
    private ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    private MapReduceDriver<Text, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

  /*
   * Set up the test. This method will be called before every test.
   */
  @Before
  public void setUp() {
		/*
		 * Set up the mapper test harness.
		 */
		ProductCoOccurMapper mapper = new ProductCoOccurMapper();
		mapDriver = new MapDriver<Text, Text, Text, IntWritable>();
		mapDriver.setMapper(mapper);

		/*
		 * Set up the reducer test harness.
		 */
		ProductCoOccurReducer reducer = new ProductCoOccurReducer();
		reduceDriver = new ReduceDriver<Text, IntWritable, Text, IntWritable>();
		reduceDriver.setReducer(reducer);

		/*
		 * Set up the mapper/reducer test harness.
		 */
		mapReduceDriver = new MapReduceDriver<Text, Text, Text, IntWritable, Text, IntWritable>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setReducer(reducer);
  }

  /*
   * Test the mapper.
   */
  @Test
  public void testMapper() throws IOException{
    mapDriver.withInput(new Text("session1"),new Text("product1,product2,product3,"));

    mapDriver.withOutput(new Text("product1,product2"), new IntWritable(1));
    mapDriver.withOutput(new Text("product1,product3"), new IntWritable(1));
    mapDriver.withOutput(new Text("product2,product3"), new IntWritable(1));

    mapDriver.runTest();
  }

  /*
   * Test the reducer.
   */
  @Test
  public void testReducer() throws IOException {

    List<IntWritable> values = new ArrayList<IntWritable>();
    values.add(new IntWritable(1));
    values.add(new IntWritable(1));

    reduceDriver.withInput(new Text("product1,product2"), values);
    reduceDriver.withOutput(new Text("product1,product2"), new IntWritable(2));

    reduceDriver.runTest();
  }

  /*
   * Test the mapper and reducer working together.
   */
  @Test
  public void testMapReduce() throws IOException{

    mapReduceDriver.withInput(new Text("session1"), new Text("product1,product2,"));
    mapReduceDriver.addOutput(new Text("product1,product2"), new IntWritable(1));

    mapReduceDriver.runTest();
  }
}
