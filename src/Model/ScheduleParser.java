package Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleParser {
    private final ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    private final TimeChecker timeChecker;
    private final DateTimeFormatter parser = DateTimeFormatter.ISO_DATE_TIME;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public ScheduleParser(Channel channel){
        String scheduleURL = channel.getScheduleURL();
        timeChecker = new TimeChecker();

        if(scheduleURL != null){
            timeChecker.setTwelveHoursAfterwards();
            timeChecker.setTwelveHoursFromBackward();
            if(timeChecker.needToFetchDataFromTomorrow()){
                scheduleURL = channel.getScheduleURL()+"&date="+timeChecker.getTommorowDate();
                fetchDataFromAPI(scheduleURL);
            }
            if(timeChecker.needToFetchDataFromYesterday()){
                scheduleURL = channel.getScheduleURL()+"&date="+timeChecker.getYesterdayDate();
                fetchDataFromAPI(channel.getScheduleURL());
                fetchDataFromAPI(scheduleURL);
            }
        }
    }

    private void fetchDataFromAPI(String scheduleURL) {
        if(scheduleURL != null){
            try{
                URL url = new URL(scheduleURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(connection.getInputStream());
                    doc.normalize();
                    processSchedule(doc);
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            JOptionPane.showMessageDialog(null, "There is nothing to fetch");
        }
    }

    private void processSchedule(Document document) {
        NodeList nodeList = document.getElementsByTagName("scheduledepisode");
        ZonedDateTime upperTime = timeChecker.getTwelveHoursFromAfterwards();
        ZonedDateTime lowerTime = timeChecker.getTwelveHoursFromBackward();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String starttimeutc = getElementTextContent(element, "starttimeutc");
            if(starttimeutc == null){
                continue;
            }else{
                ZonedDateTime startTimeZoned = ZonedDateTime.parse(starttimeutc, parser);
                ZonedDateTime startTimeInLocalZone = startTimeZoned.withZoneSameInstant(ZoneId.systemDefault());
                createSchedule(startTimeInLocalZone, lowerTime, upperTime, element);
            }

        }
    }

    private void createSchedule(ZonedDateTime startTimeInLocalZone, ZonedDateTime lowerTime, ZonedDateTime upperTime, Element element) {
        if(startTimeInLocalZone.isAfter(lowerTime) && startTimeInLocalZone.isBefore(upperTime)){
            String startTime = startTimeInLocalZone.format(formatter);
            String programName = getElementTextContent(element, "title");
            String description = getElementTextContent(element, "description");
            String imageURL = getElementTextContent(element, "imageurl");
            String endtimeutc = getElementTextContent(element, "endtimeutc");
            String endTime  = null;
            if (endtimeutc != null) {
                ZonedDateTime endTimeZoned = ZonedDateTime.parse(endtimeutc,parser);
                ZonedDateTime endTimeInLocalZone = endTimeZoned.withZoneSameInstant(ZoneId.systemDefault());
                endTime  = endTimeInLocalZone.format(formatter);
            }
            instanstiateSchedule(startTime, endTime, programName, imageURL, description);
        }
    }



    private void instanstiateSchedule(String startTime, String endTime, String programName, String imageURL, String description) {
        Schedule schedule = new ScheduleBuilder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setProgramName(programName)
                .setImage(imageURL)
                .setDescription(description)
                .build();

        schedules.add(schedule);
    }

    private String getElementTextContent(Element parentElement, String childElementName) {
        NodeList list = parentElement.getElementsByTagName(childElementName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }

    public ArrayList<Schedule> getScheduleList() {
        return schedules;
    }

}
