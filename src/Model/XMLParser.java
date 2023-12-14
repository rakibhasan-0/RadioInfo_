package Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class XMLParser {
    private final ArrayList<Channel> channels = new ArrayList<Channel>();
    public XMLParser() {
        String baseUrl = "http://api.sr.se/api/v2/channels?pagination=false";
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(connection.getInputStream());
                doc.normalize();
                processChannels(doc);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processChannels(Document doc) {

        NodeList channelNodes = doc.getElementsByTagName("channel");

        for (int i = 0; i < channelNodes.getLength(); i++) {
            Element element = (Element) channelNodes.item(i);
            int id = Integer.parseInt(element.getAttribute("id"));
            String name = element.getAttribute("name");
            String image = getElementTextContent(element, "image");
            String scheduleUrl = getElementTextContent(element, "scheduleurl");
            String tagLine = getElementTextContent(element, "tagline");

            if(scheduleUrl != null){
                scheduleUrl = scheduleUrl+"&pagination=false";
            }

            String channelType = getElementTextContent(element, "channeltype");
            //System.out.println("type:"+channelType);

            Channel channel = new ChannelBuilder()
                    .setChannelName(name)
                    .setChannelId(id)
                    .setScheduleURL(scheduleUrl)
                    .setTagline(tagLine)
                    .setChannelType(channelType)
                    .setImage(image)
                    .build();

            channels.add(channel);
        }
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }


    private String getElementTextContent(Element parentElement, String childElementName) {
        NodeList list = parentElement.getElementsByTagName(childElementName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }

}
