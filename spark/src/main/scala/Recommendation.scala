import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object MyFunctions {

  /**
    * Build the product pair list from a product list
    */
  def buildProductCoOccurPairs(products: String): Array[(String, String)] = {
    val productArray = products.split(",")

    val productCoOccurPairs = for {
      x <- productArray
      y <- productArray
      if (x.length() > 0 && y.length() > 0 && x < y)
    } yield (x, y)

    productCoOccurPairs
  }

  /**
    * Add two list together and sort the list by the second element in the tuple
    */
  def addList(list1: List[(String, Int)], list2: List[(String, Int)]): List[(String, Int)] = {
    val totalList = list1 ::: list2

    val sortedList = totalList.sortWith(_._2 > _._2)

    sortedList
  }
}

object RecommendationApp {
  def main(args: Array[String]) {

    if(args.length < 2){
      println(" ********* Usage: ************")
      println("spark-submit --class \"RecommendationApp\" --master <master-url> product-recommendation_2.11-1.0.jar <input_log_files_location> <output_location>")
    }else{
      //create SparkContext
      val conf = new SparkConf().setAppName("Product Recommendation")
      val sc = new SparkContext(conf)

      //Load the log files to RDD
      val logFile = sc.textFile(args(0))
      //Filter out the logs which contain products
      val productRDD = logFile.filter(line => LogLineParser.containsProduct(line))

      //Map the log RDD to user session - single product pair RDD
      val sessionProductRDD = productRDD.map(line => (LogLineParser.parseSessionID(line), LogLineParser.parseProductID(line)))
      val sessionProductPairRDD = sessionProductRDD.filter( a => a._2.length() > 0 )
      //Reduce the user session - product pair to get History matrix which contains the interactions between users and multiple products
      val sessionProductsRDD = sessionProductPairRDD.reduceByKey((a, b) => a + "," + b).filter(a => a._2.contains(","))

      //Map the sessionProductsRDD to ProductCoOccur Pair
      val productCoOccurPairs= sessionProductsRDD.flatMap(a => MyFunctions.buildProductCoOccurPairs(a._2)).map( a => (a,1) )
      //Reduce the product CoOccur Pairs and get CoOccur count for each product pairs
      val productCoOccurCount = productCoOccurPairs.reduceByKey( (a,b) => a+b ).filter( a => a._2 > 1)

      //Map the product CoOccur Count to product Indicator Pairs. The key is product A, and the value is product B and CoOcuur count
      val productIndicatorPairs = productCoOccurCount.flatMap( a => Array( (a._1._1,(a._1._2, a._2)), (a._1._2,(a._1._1, a._2)) ) )
      //Reduce the product Indicator Pairs RDD to generate the product Indicator list which sorted by CoOccur Count in descending
      val productIndicator = productIndicatorPairs.map(a => (a._1, List(a._2))).reduceByKey(MyFunctions.addList)

      //Save the RDD to file
      productIndicator.saveAsTextFile(args(1))
    }

  }
}