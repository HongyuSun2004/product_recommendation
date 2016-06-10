# product_recommendation
Use Hadoop MapReduce jobs to analysis log files to provide product recommendation.

### Recommendation Model
- One customer could visit multiple products during a visit session.
- Product co-occurrence measures the frequency with which two products appear close to each other in a customer’s visit session.
- It provides results for “people who is interested in this, also interested that”.

### Builds three matrices from web log files:
* History matrix:  contains the interactions between users and products as a user-by-product binary matrix
* Co-occurrence matrix:  transforms the history matrix into an product-by-product matrix, recording which product co-occur or appear together in user histories.
* Indicator matrix: The indicator matrix retains only the anomalous (interesting) co-occurrences that will serve as clues for recommendation. 

![MapReduce Jobs](mapreduce.png?raw=true “MapReduce Jobs“)

### How to create your recommendation jar file:
* Create your LogFileLineParser java class which implements the parser.LogFileLineParser interface.
* Use maven to build and create the recommendation-1.0-SNAPSHOT.jar file 
```sh
mvn package
```

### How to run the haoop map-reduce jobs:

Step 1:
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.UserProductDriver -DlogFileLineParserClass="<your LogFileLineParser class name>" <your logfiles HDFS path> out/step1
```

Step 2:
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.ProductCoOccurDriver out/step1 out/step2
```

Step 3:
```sh
hadoop jar recommendation-1.0-SNAPSHOT.jar hadoop.mapreduce.ProductIndicatorDriver out/step2 out/step3
```

Check the final result:
```sh
hadoop fs -cat out/step3/part-r-00000 | less
```
License
----

MIT