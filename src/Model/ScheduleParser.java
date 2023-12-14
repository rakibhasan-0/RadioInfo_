package Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleParser {
    private final ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    private final Channel channel;

    public ScheduleParser(Channel channel){

        this.channel = channel;
        String scheduleURL = channel.getScheduleURL();
        // since space stated that we have to fetch sechedule from 12 before and 12 hours after of the current time
        // which means that may need to fetch data from the previous or next day I guess.
        if(scheduleURL != null){
            try{
                LocalTime now = LocalTime.now();
                LocalTime sixHourBefore = now.minusHours(12);
                LocalTime twelveHoursAfter = now.plusHours(12);
                LocalTime cutOffTime = LocalTime.of(twelveHoursAfter.getHour(), 0);
                scheduleURL = channel.getScheduleURL();
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
            } catch (ProtocolException | MalformedURLException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else{
            System.out.println("There is nothing to fetch");
        }
    }

    private void  processSchedule(Document document){
        NodeList nodeList = document.getElementsByTagName("scheduledepisode");
        for(int i = 0; i < nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            String programName = getElementTextContent(element,"title");
            String description = getElementTextContent(element,"description");
            String imageURL = getElementTextContent(element,"imageurl");
            String starttimeutc = getElementTextContent(element,"starttimeutc");
            String endtimeutc = getElementTextContent(element,"endtimeutc");
            LocalTime startTime = LocalTime.parse(starttimeutc.substring(11, 19), DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime endTime = LocalTime.parse(endtimeutc.substring(11, 19), DateTimeFormatter.ofPattern("HH:mm:ss"));
            Schedule schedule = new ScheduleBuilder()
                    .setEndTime(endTime)
                    .setStartTime(startTime)
                    .setProgramName(programName)
                    .setImage(imageURL)
                    .setDescription(description)
                    .build();

            schedules.add(schedule);
        }
    }

    private String getElementTextContent(Element parentElement, String childElementName) {
        NodeList list = parentElement.getElementsByTagName(childElementName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }

    public ArrayList<Schedule> getScheduleList() {
        //System.out.println("Size of the Schedule    " + schedules.size());
        return schedules;
    }

}
