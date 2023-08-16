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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleParser {
    private final Channel channel;
    private final Cache cache;
    private LocalTime now, sixHourBefore, twelveHoursAfter;

    public ScheduleParser(Channel channel, Cache cache) {
        this.channel = channel;
        this.cache = cache;
    }

    public List<Schedule> fetchSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        URL scheduleURL = channel.getScheduleURL();

        if (scheduleURL == null) {
            System.out.println("Channel '" + channel.getChannelName() + "' has no schedule information.");
            return schedules;
        }

        now = LocalTime.now();
        sixHourBefore = now.minusHours(6);
        twelveHoursAfter = now.plusHours(12);

        LocalTime cutOffTime = LocalTime.of(twelveHoursAfter.getHour(), 0);

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
                    Schedule schedule = parseScheduleElement(scheduleElement, cutOffTime);
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

    private Schedule parseScheduleElement(Element scheduleElement, LocalTime cutOffTime) {
        String title = getTextContent(scheduleElement, "title");
        String description = getTextContent(scheduleElement, "description");
        String imageUrl = getTextContent(scheduleElement, "imageurl");

        String dateStr = getTextContent(scheduleElement, "starttimeutc").substring(0, 10);
        String startTimestr = getTextContent(scheduleElement, "starttimeutc").substring(11, 19);
        String endTimestr = getTextContent(scheduleElement, "endtimeutc").substring(11, 19);

        LocalTime startTime;
        LocalTime endTime;
        LocalDate parseDate;
        LocalDate nowDate = LocalDate.now();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            parseDate = LocalDate.parse(dateStr, dateTimeFormatter);
            startTime = LocalTime.parse(startTimestr, formatter);
            endTime = LocalTime.parse(endTimestr, formatter);
        } catch (Exception e) {
            System.err.println("Error parsing time: " + e.getMessage());
            return null;
        }

        if (title == null) {
            title = "Program title missing";
        }

        if ((startTime.isAfter(sixHourBefore) && !nowDate.isAfter(parseDate)) || startTime.isBefore(twelveHoursAfter)) {
            System.out.println("Filtered Schedule (Within Range): " + startTime);
            return new Schedule(title, description, imageUrl, startTime, endTime);
        } else {
            System.out.println("Schedule Outside Range: " + startTime);
            return null;
        }
    }

    private String getTextContent(Element element, String tagName) {
        Element childElement = (Element) element.getElementsByTagName(tagName).item(0);
        return childElement != null ? childElement.getTextContent() : null;
    }
}
