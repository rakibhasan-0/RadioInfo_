import javax.swing.*;
import java.util.List;
public class XMLParserTest {
    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser();
        xmlParser.execute();

        try {
            List<Channel> channels = xmlParser.get();
            if (channels != null && !channels.isEmpty()) {
                System.out.println("Channels fetched successfully:");
                for (Channel channel : channels) {
                    System.out.println("Channel Name: " + channel.getChannelName());
                    System.out.println("Channel ID: " + channel.getId());
                    System.out.println("Channel Schedule URL: " + channel.getScheduleURL());
                    System.out.println("Channel Image: " + channel.getChannelImage());
                    System.out.println("Fetching schedules for channel: " + channel.getChannelName());

                    Cache cache = new Cache();
                    List<Channel> singleChannelList = List.of(channel); // Wrap the single channel in a list
                    ScheduleWorker scheduleWorker = new ScheduleWorker(singleChannelList, cache);
                    scheduleWorker.execute();
                    List<Schedule> schedules = scheduleWorker.get();

                    if (!schedules.isEmpty()) {
                        System.out.println("Schedules:");
                        for (Schedule schedule : schedules) {
                            System.out.println("- Title: " + schedule.getProgramName());
                            System.out.println("  Start Time: " + schedule.getStartTime());
                            System.out.println("  End Time: " + schedule.getEndTime());
                        }
                    } else {
                        System.out.println("No schedules found for this channel.");
                    }
                    System.out.println("---------------------------------");
                }
            } else {
                System.out.println("No channels fetched.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
