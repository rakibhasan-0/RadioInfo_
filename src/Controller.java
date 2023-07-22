import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private Cache cache;

    public Controller(ChannelView channelView, ProgramView programView, MenuBarView menuBar) {
        this.channelView = channelView;
        this.programView = programView;
        this.cache = new Cache();

        // Fetch channels using the XMLParser
        List<Channel> channels = fetchChannelsFromXML();

        // Check if channels are available
        if (channels != null && !channels.isEmpty()) {
            System.out.println("Channels fetched successfully!");

            // Use HashMap to store schedules for each channel
            HashMap<Channel, List<Schedule>> schedulesMap = fetchSchedulesForChannels(channels);

            // Set up channel buttons and action listeners
            for (Channel channel : channels) {
                JButton button = new JButton(channel.getChannelName());
                button.setIcon(new ImageIcon(channel.getChannelImage()));

                // ActionListener to populate program table when the button is clicked
                button.addActionListener(e -> {
                    List<Schedule> schedules = schedulesMap.get(channel);
                    programView.populateProgramTable(schedules);
                });

                channelView.addChannelButton(button);
            }
        }

        // Other initialization and setup code...
    }

    private List<Channel> fetchChannelsFromXML() {
        List<Channel> channels = null;
        try {
            XMLParser parser = new XMLParser();
            parser.execute();
            channels = parser.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }

    private HashMap<Channel, List<Schedule>> fetchSchedulesForChannels(List<Channel> channels) {
        HashMap<Channel, List<Schedule>> schedulesMap = new HashMap<>();
        ScheduleWorker scheduleWorker = new ScheduleWorker(channels, cache);
        scheduleWorker.execute();

        try {
            HashMap<Channel, List<Schedule>> fetchedSchedulesMap = scheduleWorker.get();
            schedulesMap.putAll(fetchedSchedulesMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedulesMap;
    }
}
