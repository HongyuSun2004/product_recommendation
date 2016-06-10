package parser;

/**
 * LogFileLineParser is a interface.
 * The implemented class will provide methods to parse user session ID and product ID
 */
public interface LogFileLineParser {

    void setLogFileLine(String line);

    boolean containsProduct();

    String parseSessionID();

    String parseProductID();

}
