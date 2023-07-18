import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class XMLParser {
    private final ArrayList<Channel> channels;

    public XMLParser() {
        channels = new ArrayList<>();
        fetchChannelsData();
    }

    private void fetchChannelsData() {
        try {
            int currentPage = 1;
            int totalPages = 0;

            do {
                URL url = new URL("http://api.sr.se/api/v2/channels/?page=" + currentPage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(connection.getInputStream());
                    Element root = document.getDocumentElement();
                    if (currentPage == 1) {
                        totalPages = getTotalPages(root);
                    }
                    NodeList channelList = root.getElementsByTagName("channel");
                    processChannelList(channelList);
                }
                currentPage++;
            } while (currentPage <= totalPages);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private int getTotalPages(Element root) {
        Element paginationElement = (Element) root.getElementsByTagName("pagination").item(0);
        return Integer.parseInt(paginationElement.getElementsByTagName("totalpages").item(0).getTextContent());
    }

    private void processChannelList(NodeList channelList) {
        for (int i = 0; i < channelList.getLength(); i++) {
            Element channelElement = (Element) channelList.item(i);
            String channelName = channelElement.getAttribute("name");
            int id = Integer.parseInt(channelElement.getAttribute("id"));

            String imageUrl = getTextContent(channelElement, "image");
            URL scheduleUrl = createURL(getTextContent(channelElement, "scheduleurl"));
            Image image = loadImage(imageUrl);

            if (image == null) {
                image = createPlaceholderImage();
            }

            Channel channel = new Channel(channelName, scheduleUrl, image, id);
            channels.add(channel);
        }
    }

    private String getTextContent(Element element, String tagName) {
        Element childElement = (Element) element.getElementsByTagName(tagName).item(0);
        return childElement != null ? childElement.getTextContent() : null;
    }

    private URL createURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            }
            return url;
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Image loadImage(String imageUrl) {
        if (imageUrl != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                Image originalImage = imageIcon.getImage();
                int width = 50;
                int height = 50;
                return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                System.out.println("Error loading image from URL: " + imageUrl);
            }
        }
        return null;
    }

    private Image createPlaceholderImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 50, 50);
        g2d.dispose();
        return image;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

}
