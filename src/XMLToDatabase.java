import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLToDatabase {

    public static void main(String[] args) {
        try {
            // Load the XML file
            File xmlFile = new File("src\\books.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the XML structure
            doc.getDocumentElement().normalize();

            // Set up the database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/task1_books", "root", "");

            // Process each book node in the XML
            NodeList bookList = doc.getElementsByTagName("book");
            for (int i = 0; i < bookList.getLength(); i++) {
                Node bookNode = bookList.item(i);
                if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNode;

                    // Extract data from XML
                    String title = bookElement.getElementsByTagName("title").item(0).getTextContent();
                    String author = bookElement.getElementsByTagName("author").item(0).getTextContent();
                    int year = Integer.parseInt(bookElement.getElementsByTagName("year").item(0).getTextContent());

                    // Save data to the database
                    saveToDatabase(connection, title, author, year);
                }
            }

            // Close the database connection
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToDatabase(Connection connection, String title, String author, int year)
            throws SQLException {
        // Assuming you have a table named "books" with columns: title, author, year
        String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.executeUpdate();
        }
    }
}
