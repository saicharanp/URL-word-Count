import java.io.IOException;
import java.sql.SQLException;

/**
 * This class accepts a URL as a command line
 * argument and writes the word count frequency
 * in that URL to a database.
 *
 * @author  Saicharan Poduri
 * @since   2017-07-28
 */

public class Main {

    /**
     * This is the main method which calls the URLScraper's processURL method.
     * @param args The first and only argument should be a URL with valid protocol (http or https)
     */

    public static void main(String[] args) {

        //Get the URL from the command line.
        try {
            if (args.length != 1) {
                throw new Exception("Invalid no of args - pass a valid URL with protocol as the only argument");
            }

            String url = args[0];
            URLScraper us = new URLScraper();
            us.processURL(url);
        }
        catch(IOException io) {
            io.printStackTrace();
        }
        catch(SQLException se) {
            se.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
