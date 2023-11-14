package Model;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private static final String BASE_URL = "http://api.sr.se/api/v2/channels/";
    private static final int IMAGE_WIDTH = 50;
    private static final int IMAGE_HEIGHT = 50;
    private ArrayList<Channel> channels;

    public XMLParser() {
        channels = new ArrayList<>();
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void fetchChannelsData() throws IOException, ParserConfigurationException, SAXException {
        int currentPage = 1;
        int totalPages = Integer.MAX_VALUE;

        while (currentPage <= totalPages) {
            Document document = getDocumentForPage(currentPage);
            if (currentPage == 1) {
                totalPages = getTotalPages(document);
            }
            NodeList channelList = document.getElementsByTagName("channel");
            processChannelList(channelList);
            currentPage++;
        }
    }

    private Document getDocumentForPage(int currentPage) throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL(BASE_URL + "?page=" + currentPage);
        HttpURLConnection connection = createHttpConnection(url);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(connection.getInputStream());
    }

    private HttpURLConnection createHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        return connection;
    }

    private int getTotalPages(Document document) {
        Element paginationElement = (Element) document.getElementsByTagName("pagination").item(0);
        return Integer.parseInt(paginationElement.getElementsByTagName("totalpages").item(0).getTextContent());
    }

    private void processChannelList(NodeList channelList) {
        for (int i = 0; i < channelList.getLength(); i++) {
            Element channelElement = (Element) channelList.item(i);
            Channel channel = createChannelFromElement(channelElement);
            channels.add(channel);
        }
    }

    private Channel createChannelFromElement(Element channelElement) {
        String channelName = channelElement.getAttribute("name");
        int id = Integer.parseInt(channelElement.getAttribute("id"));
        String imageUrl = getTextContent(channelElement, "image");
        URL scheduleUrl = createURL(getTextContent(channelElement, "scheduleurl"));
        Image image = loadImage(imageUrl);
        if (image == null) {
            image = createPlaceholderImage();
        }
        return new Channel(channelName, scheduleUrl, image, id);
    }

    private String getTextContent(Element element, String tagName) {
        Element childElement = (Element) element.getElementsByTagName(tagName).item(0);
        return childElement != null ? childElement.getTextContent() : null;
    }

    private URL createURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            // Log and handle the MalformedURLException
            return null;
        }
    }

    private Image loadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return createPlaceholderImage();
        }
        try {
            ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
            Image originalImage = imageIcon.getImage();
            return originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            // Log and handle the IOException
            return createPlaceholderImage();
        }
    }

    private Image createPlaceholderImage() {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.dispose();
        return image;
    }
}
