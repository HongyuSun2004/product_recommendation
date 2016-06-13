# Product Recommendation
Use Hadoop and Spark MapReduce jobs to analyze e-commerce web log files to provide product recommendation.



### Project Structure
- hadoop folder contains the hadoop MapReduce Job Implementation.
- spark folder contains the spark MapReduce Job Implementation.

### Recommendation Model
- One customer could visit multiple products during a visit session.
- Product co-occurrence measures the frequency with which two products appear close to each other in a customer’s visit session.
- It provides results for “people who is interested in this, also interested that”.

### MapReduce Jobs:

![MapReduce Jobs](mapreduce.png?raw=true “MapReduce Jobs“)
- The first MapReduce Job generates a History matrix which contains the interactions between user visit session and products. The input is web log files. The output key is a visit session ID, and the value is a list of product IDs visited during the visit session. Note: The product list contains the unique products.

- The second MapReduce Job transforms the history matrix into an product-by-product matrix which stores product pairs co-occur or appear together in multiple users visit session. The input is the output of the previous MapReduce Job output. The output key is a pair of product IDs, and the value is the co-occur number of the pair of products appear together in multiple user’s visit sessions.

- The third MapReduce Job generate the indicator matrix which retains only the anomalous (interesting) co-occurrences. The indicator matrix will serve as clues for recommendation. The input is the output of the previous MapReduce Job output. The output key is a product IDs, and the value is a list of products whick can be used as recommendation. The production list are ranked by the co-occur number.


### Hadoop Implementation:

#### How to create your MapReduce Job recommendation jar file:
* Create your LogFileLineParser java class which implements the parser.LogFileLineParser interface. Your LogFileLineParser should provide parse session ID and parse product ID methods. Those methods will be used by map-reduce job to parse each line of log files. You can refer to SampleLogFileLineParser.java file.
* Use maven to build and create the recommendation-1.0-SNAPSHOT.jar file 
```sh
mvn package
```

#### How to run the haoop map-reduce jobs:

Step 1 - Run User session Product job with your LogFileLineParser class and your logfiles HDFS path.
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.UserProductDriver -DlogFileLineParserClass="<your LogFileLineParser class name>" <your logfiles HDFS path> out/step1
```

Step 2 - Run Product CoOccur job
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.ProductCoOccurDriver out/step1 out/step2
```

Step 3 - Run Product Indicator job
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.ProductIndicatorDriver out/step2 out/step3
```

Check the final result:
```sh
hadoop fs -cat out/step3/part-r-00000 | less
```

### Spark Implementation:

#### How to create your MapReduce Job recommendation jar file:
- Implements the LogFileLineParser. The LogFileLineParser should provide parse session ID and parse product ID methods. Those methods will be used by map-reduce job to parse each line of log files. You can refer to SampleLogLineParser.scala file.
- Use sbt to build and create the recommendation jar file 
```sh
sbt package
```

#### How to run the spark map-reduce jobs:

Submit the recommendation jar file to spark cluster with input/output locations. The final result will be stored in the output location. 
```sh
spark-submit --class "RecommendationApp" --master <master-url> product-recommendation_2.11-1.0.jar <input_log_files_location> <output_location>
```

License
----

MIT