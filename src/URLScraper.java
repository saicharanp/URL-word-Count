
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Hashtable;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class URLScraper {

    /**
     * This is the URLScraper's processURL method which generates a document from the URL's content.
     * The generated document is sent to processDocument function
     * @param url URL for which the content is to be processed.
     * @throws java.sql.SQLException, java.io.IOException
     */

    public void processURL(String url) throws SQLException, IOException {

        URL convertedURL = new URL(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(convertedURL.openStream()));
        String temp;
        StringBuffer sb = new StringBuffer();
        while((temp = br.readLine()) != null) {
            sb.append(temp);
            sb.append("\n");
        }
        Document doc = Jsoup.parse(sb.toString());
        this.processDocument(doc, url);
        br.close();
    }

    /**
     * This is the URLScraper's processDocument method which generates a word count map from the URL's content document.
     * The generated word count map to writeResultsToDB function.
     * @param doc URL content document
     * @param url URL for which the content document is generated
     * @throws java.sql.SQLException
     */

    public void processDocument(Document doc, String url) throws SQLException {
        Hashtable<String, Integer> wordCountMap = new Hashtable<String, Integer>();
        String[] tokens = doc.text().split("\\s");

        for(int i=0; i<tokens.length; i++) {

            tokens[i] = tokens[i].toLowerCase();
            if(wordCountMap.isEmpty() || !wordCountMap.containsKey(tokens[i])) {
                wordCountMap.put(tokens[i], 1);
            }
            else {
                int count = wordCountMap.get(tokens[i]);
                wordCountMap.put(tokens[i], count+1);
            }
        }

        this.writeResultsToDB(wordCountMap, url);
    }

    /**
     * This is the URLScraper's writeResultsToDB method which sends the word count map to DBResultWriter.
     * @param wordCountMap URL's content document
     * @param url URL for which the content document is generated
     * @throws java.sql.SQLException
     */

    public void writeResultsToDB(Hashtable wordCountMap, String url) throws SQLException {
        DBResultWriter drw = new DBResultWriter();
        drw.write(wordCountMap, url);
    }
}
