import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Test {
    public static void main(String[] args) {
        try {
            // Fetch and process all pages
            int currentPage = 1;
            int totalPages = 0;

            do {
                URL url = new URL("http://api.sr.se/api/v2/channels/?page=" + currentPage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the XML response
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(connection.getInputStream());

                    // Get the total pages information if it's not retrieved yet
                    if (currentPage == 1) {
                        Element root = document.getDocumentElement();
                        Element paginationElement = (Element) root.getElementsByTagName("pagination").item(0);
                        totalPages = Integer.parseInt(paginationElement.getElementsByTagName("totalpages").item(0).getTextContent());
                        System.out.println("Total Pages: " + totalPages);
                    }

                    // Process the channel elements
                    Element root = document.getDocumentElement();
                    NodeList channelList = root.getElementsByTagName("channel");
                    for (int i = 0; i < channelList.getLength(); i++) {
                        Element channelElement = (Element) channelList.item(i);
                        String channelName = channelElement.getAttribute("name");
                        System.out.println("Channel: " + channelName);
                    }
                } else {
                    System.out.println("Failed to fetch XML data for page " + currentPage + ". Response Code: " + responseCode);
                }

                connection.disconnect();
                currentPage++;
            } while (currentPage <= totalPages);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
