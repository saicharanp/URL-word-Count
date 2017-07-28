import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

public class DBResultWriter {

    private String DATABASE_URL = "jdbc:sqlite:C://sqlite/db/Rakuten.db";

    private String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS url_wordcount (\n"
            + "	url text NOT NULL,\n"
            + "	word text NOT NULL,\n"
            + "	count integer,\n"
            + " PRIMARY KEY (url, word)\n"
            + ");";

    private String CREATE_DB_TABLE_INDEX = "CREATE INDEX IF NOT EXISTS url_wordcount_index \n"
            + "	ON url_wordcount (url)";

    private String INSERT_DB = "INSERT INTO url_wordcount(url,word,count) VALUES(?,?,?)";

    private String DELETE_DB = "DELETE FROM url_wordcount WHERE url = ?";

    private String SELECT_DB = "SELECT count(*) AS urlCount FROM url_wordcount WHERE url = ?";

    /**
     * This methods writes the URL's word count map to a database.
     * @param result word count map
     * @param url url for which the word count map is generated
     * @throws java.sql.SQLException
     */

    public void write(Hashtable<String, Integer> result, String url) throws SQLException {

        Connection connection = DriverManager.getConnection(DATABASE_URL);
        //Create database table
        Statement stmt = connection.createStatement();
        stmt.execute(CREATE_DB_TABLE);
        stmt.execute(CREATE_DB_TABLE_INDEX);

        //Check if there are any records with the same URL.
        PreparedStatement pstm = connection.prepareStatement(SELECT_DB);
        pstm.setString(1, url);
        ResultSet rs = pstm.executeQuery();
        rs.next();
        int count = rs.getInt("urlCount");

        //Delete the existing records for this url
        if(count > 0) {
            pstm = connection.prepareStatement(DELETE_DB);
            pstm.setString(1, url);
            pstm.executeUpdate();
        }

        //Insert records into table
        pstm = connection.prepareStatement(INSERT_DB);

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            pstm.setString(1, url);
            pstm.setString(2, entry.getKey());
            pstm.setInt(3, entry.getValue());
            pstm.executeUpdate();
        }
        stmt.close();
        pstm.close();
        connection.close();
    }
}
