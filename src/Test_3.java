import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Test_3 {
    public static void main(String[] args) {
        try {
            ScheduleParser scheduleParser = new ScheduleParser();
            Map<Channel, List<Schedule>> channelsInfo = scheduleParser.getChannelsInfo();

            for (Map.Entry<Channel, List<Schedule>> entry : channelsInfo.entrySet()) {
                Channel channel = entry.getKey();
                List<Schedule> schedules = entry.getValue();

                System.out.println("Channel: " + channel.getChannelName());
                System.out.println("Channel URL: " + channel.getScheduleURL());

                System.out.println("Schedules:");

                for (Schedule schedule : schedules) {
                    System.out.println("Title: " + schedule.getProgramName());
                    System.out.println("Start Time: " + schedule.getStartTime());
                    System.out.println("End Time: " + schedule.getEndTime());
                    System.out.println();
                }

                System.out.println("------------------------");
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
