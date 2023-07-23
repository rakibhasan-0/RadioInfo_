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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleParser {
    private final Channel channel;
    private final Cache cache;

    public ScheduleParser(Channel channel, Cache cache) {
        this.channel = channel;
        this.cache = cache;
    }

    public List<Schedule> fetchSchedules() {
        List<Schedule> schedules = cache.getSchedules(channel);

        if (schedules != null) {
            System.out.println("Fetching schedules from cache for channel: " + channel.getChannelName());
            return schedules;
        }

        schedules = new ArrayList<>();
        URL scheduleURL = channel.getScheduleURL();

        if (scheduleURL == null) {
            System.out.println("Channel '" + channel.getChannelName() + "' has no schedule information.");
            return schedules;
        }

        int currentPage = 1;
        int totalPages = Integer.MAX_VALUE;

        try {
            while (currentPage <= totalPages) {
                URL pageURL = new URL(scheduleURL + "&page=" + currentPage);
                InputStream inputStream = pageURL.openStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element root = document.getDocumentElement();

                if (currentPage == 1) {
                    totalPages = getTotalPages(root);
                }

                NodeList scheduleNodes = root.getElementsByTagName("scheduledepisode");
                for (int i = 0; i < scheduleNodes.getLength(); i++) {
                    Element scheduleElement = (Element) scheduleNodes.item(i);
                    Schedule schedule = parseScheduleElement(scheduleElement);
                    if (schedule != null) {
                        schedules.add(schedule);
                    }
                }
                currentPage++;
                inputStream.close();
            }
            cache.addSchedules(channel, schedules); // Cache the schedules
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return schedules;
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
            // Convert to Swedish local time
            ZoneId swedishZoneId = ZoneId.of("Europe/Stockholm");
            startTime = startTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(swedishZoneId).toLocalDateTime();
            endTime = endTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(swedishZoneId).toLocalDateTime();
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }

        if (startTime != null && endTime != null) {
            if (title == null) {
                title = "Program title missing";
            }
            return new Schedule(title, startTime, endTime);
        }else {
            return null;
        }
    }

    private String getTextContent(Element element, String tagName) {
        Element childElement = (Element) element.getElementsByTagName(tagName).item(0);
        return childElement != null ? childElement.getTextContent() : null;
    }
}
