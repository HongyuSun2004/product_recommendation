
object SampleLogLineParser {

  /**
    * Check whether this log line contains the product
    */
  def containsProduct(line : String) : Boolean = {
    line.contains("/sc.do") && line.contains("com.nlg.web.interceptor.ActionLoggingInterceptor") && !line.contains("proxyhandler") && !line.contains("login.do")
  }

  /**
    * Parse the Product ID from the log line
    */
  def parseProductID(line: String): String = {
    val a = line.indexOf("sc.do")

    //Discard it if it contains empty ID
    val aaa = line.indexOf("i=&", a)
    val bbb = line.indexOf("c=&", a)
    val ccc = line.indexOf("v=&", a)

    if (aaa >= 0 || bbb >= 0 || ccc >= 0) {
      return ""
    }

    //Discard it if it miss any ID
    val i = line.indexOf("i=", a)
    val c = line.indexOf("c=", a)
    val v = line.indexOf("v=", a)

    if (i < 0 || c < 0 || v < 0) {
      return ""
    }

    val ii = parseSubProductID(line, i)
    val cc = parseSubProductID(line, c)
    val vv = parseSubProductID(line, v)

    ii + ":" + cc + ":" + vv
  }

  def parseSubProductID(line: String, index: Int): String = {
    var ii = line.substring(index)
    val i2 = line.indexOf("&", index)
    if (i2 > 0) {
      ii = line.substring(index, i2)
    }
    ii
  }

  /**
    * Parse the User Session ID from the log line
    */
  def parseSessionID(line: String): String = {
    val a = line.indexOf("]")
    val b = line.indexOf(":", a)

    var sessionID = line.substring(a + 2, b - 1)

    //remove {} if it exist
    val c = sessionID.indexOf("{")
    if (c == 0) {
      sessionID = sessionID.substring(1, sessionID.length() - 1)
    }
    sessionID
  }

}
