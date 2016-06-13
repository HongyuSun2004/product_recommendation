
/**
  * Please Implements the LogFileLineParser.
  * The LogFileLineParser should provide parse session ID and parse product ID methods.
  * Those methods will be used by map-reduce job to parse each line of log files.
  */
object LogLineParser {

  /**
    * Check whether this log line contains the product
    */
  def containsProduct(line : String) : Boolean = {
    false
  }

  /**
    * Parse the Product ID from the log line
    */
  def parseProductID(line: String): String = {
    ""
  }

  /**
    * Parse the User Session ID from the log line
    */
  def parseSessionID(line: String): String = {
    ""
  }

}
