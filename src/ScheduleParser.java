import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleParser {
    private final Map<Channel, List<Schedule>> channelsInfo = new HashMap<>();

    public ScheduleParser() throws IOException, ParserConfigurationException, SAXException {
        XMLParser xmlParser = new XMLParser();
        List<Channel> channels = xmlParser.getChannels();
        fetchChannelsInfo(channels);
    }

    private void fetchChannelsInfo(List<Channel> channels) throws IOException, ParserConfigurationException, SAXException {
        for (Channel channel : channels) {
            URL channelURL = channel.getScheduleURL();
            if (channelURL != null ) {
                try {
                    List<Schedule> schedules = fetchSchedules(channelURL.toString());
                    if (schedules.isEmpty()) {
                        String missingScheduleMessage = "Program schedule missing for channel: " + channel.getChannelName();
                        schedules.add(new Schedule(missingScheduleMessage, null, null));
                    }
                    channelsInfo.put(channel, schedules);
                } catch (IOException e) {
                    System.err.println("Error fetching schedule for channel: " + channel.getChannelName());
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Schedule> fetchSchedules(String url) throws IOException, ParserConfigurationException, SAXException {
        List<Schedule> schedules = new ArrayList<>();
        int currentPage = 1;
        int totalPages = Integer.MAX_VALUE;

        while (currentPage <= totalPages) {
            String apiUrl = url + "&page=" + currentPage;
            Document document = fetchXmlDocument(apiUrl);

            Element root = document.getDocumentElement();
            totalPages = getTotalPages(root);

            NodeList scheduleNodes = root.getElementsByTagName("scheduledepisode");
            for (int i = 0; i < scheduleNodes.getLength(); i++) {
                Element scheduleElement = (Element) scheduleNodes.item(i);
                Schedule schedule = parseScheduleElement(scheduleElement);
                if (schedule != null) {
                    schedules.add(schedule);
                }
            }
            currentPage++;
        }

        return schedules;
    }

    private Document fetchXmlDocument(String url) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document;
        try (InputStream inputStream = new URL(url).openStream()) {
            document = builder.parse(inputStream);
        }
        return document;
    }

    private int getTotalPages(Element root) {
        String totalPagesStr = getTextContent(root, "totalpages");
        if (totalPagesStr != null) {
            return Integer.parseInt(totalPagesStr);
        }
        return Integer.MAX_VALUE;
    }

    private Schedule parseScheduleElement(Element scheduleElement) {
        String title = getTextContent(scheduleElement, "title");
        String startTimestr = getTextContent(scheduleElement, "starttimeutc");
        String endTimestr = getTextContent(scheduleElement, "endtimeutc");

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            startTime = LocalDateTime.parse(startTimestr, formatter);
            endTime = LocalDateTime.parse(endTimestr, formatter);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }

        if (startTime != null && endTime != null) {
            if (title == null) {
                title = "Program title missing";
            }
            return new Schedule(title, startTime, endTime);
        } else {
            return null;
        }
    }

    private String getTextContent(Element element, String tagName) {
        Element childElement = (Element) element.getElementsByTagName(tagName).item(0);
        return childElement != null ? childElement.getTextContent() : null;
    }

    public Map<Channel, List<Schedule>> getChannelsInfo() {
        return channelsInfo;
    }
}
