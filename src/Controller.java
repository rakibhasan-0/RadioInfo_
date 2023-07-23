import javax.swing.*;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private MenuBarView menuBarView;
    private Cache cache;

    public Controller(ChannelView channelView, ProgramView programView, MenuBarView menuBarView) {
        this.channelView = channelView;
        this.programView = programView;
        this.menuBarView = menuBarView;

        this.cache = new Cache();
        // Fetch channels using the XMLParser
        List<Channel> channels = fetchChannelsFromXML();

        // Check if channels are available
        if (channels != null && !channels.isEmpty()) {
            System.out.println("Channels fetched successfully!");
            // Set up channel buttons and action listeners
            for (Channel channel : channels) {
                JButton button = new JButton(channel.getChannelName());
                button.setIcon(new ImageIcon(channel.getChannelImage()));
                button.addActionListener(e -> {
                    ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
                    List<Schedule> schedules = scheduleParser.fetchSchedules();
                    programView.populateProgramTable(schedules);
                });
                channelView.addChannelButton(button);
            }

            // Action listener for the "Update Channel" menu item
            menuBarView.addUpdateChannelListener(e -> {
                // Fetch channels from the server
                List<Channel> updatedChannels = fetchChannelsFromXML();
                // Update the channels in the channelView
                channelView.updateChannels(updatedChannels);
                // Update the last updated time in the panel
                menuBarView.updateLastUpdatedTime();
            });

            // Action listener for the "Update Schedule" menu item
            menuBarView.addUpdateScheduleListener(e -> {
                // Get currently selected channel
                Channel selectedChannel = cache.getSelectedChannel();
                if (selectedChannel != null) {
                    // Fetch updated schedules for the selected channel from the server
                    ScheduleParser scheduleParser = new ScheduleParser(selectedChannel, cache);
                    List<Schedule> updatedSchedules = scheduleParser.fetchSchedules();
                    // Update the schedules for the selected channel in the cache
                    cache.addSchedules(selectedChannel, updatedSchedules);
                    // Update the program view with the new schedules
                    programView.populateProgramTable(updatedSchedules);
                }
                // Update the last updated time in the panel
                menuBarView.updateLastUpdatedTime();
            });

            // Start automatic updates
            startAutomaticUpdates();
        }
        // Other initialization and setup code...
    }

    private void startAutomaticUpdates() {
        Timer timer = new Timer(60 * 60 * 1000, e -> {
            // Fetch channels from the server
            List<Channel> updatedChannels = fetchChannelsFromXML();
            // Update the channels in the channelView
            channelView.updateChannels(updatedChannels);
            // Update the last updated time in the panel
            menuBarView.updateLastUpdatedTime();
        });
        timer.start();
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
}
