import javax.swing.*;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private MenuBarView menuBarView;
    private Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;

    public Controller(ChannelView channelView, ProgramView programView, MenuBarView menuBarView){

        this.channelView = channelView;
        this.programView = programView;
        this.menuBarView = menuBarView;
        this.cache = new Cache();

        // Fetch channels using the XMLParser
        List<Channel> channels = fetchChannelsFromXML();

        // Check if channels are available
        if (channels != null && !channels.isEmpty()) {
           // System.out.println("Channels fetched successfully!");
            // Set up channel buttons and action listeners
            for (Channel channel : channels) {
                JButton button = new JButton(channel.getChannelName());
                button.setIcon(new ImageIcon(channel.getChannelImage()));
                button.addActionListener(e -> {
                    selectedChannel = channel;
                    ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
                    List<Schedule> schedules = scheduleParser.fetchSchedules();
                    programView.populateProgramTable(schedules);
                    // TODO: check if channel things get updated properly or not.
                    // Selected channel's name should be visible.
                    menuBarView.setSelectedChannelLabel(channel.getChannelName());
                });
                channelView.addChannelButton(button);
            }

            // Action listener for the "Update Channel" menu item
            menuBarView.addUpdateChannelListener(e -> {
                //System.out.println("hello world");
                // Clear the cache which has stored.
                cache.clearCache();
                // Fetch channels from the server
                List<Channel> updatedChannels = fetchChannelsFromXML();
                System.out.println(channels.isEmpty());

                for (Channel channel : updatedChannels) {
                    JButton button = new JButton(channel.getChannelName());
                    button.setIcon(new ImageIcon(channel.getChannelImage()));
                    button.addActionListener(e1 -> {
                        this.selectedChannel = channel;
                        ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
                        List<Schedule> schedules = scheduleParser.fetchSchedules();
                        programView.populateProgramTable(schedules);
                        // TODO: check if channel things get updated properly or not.
                        // Selected channel's name should be visible.
                        menuBarView.setSelectedChannelLabel(channel.getChannelName());
                    });
                    channelView.addChannelButton(button);
                }
                // Update the last updated time in the panel
                menuBarView.updateLastUpdatedTime();
                // Start automatic updates again after manual update
                startAutomaticUpdates();
            });

            // Action listener for the "Update Schedule" menu item
            menuBarView.addUpdateScheduleListener(e -> {
                // Get currently selected channel
                Channel selectedChannel = this.selectedChannel;

                // remove that selected channel's schedule from the cache.
                cache.clearCacheForAChannel(selectedChannel);

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

                // Stop the automatic updates timer
                if (automaticUpdateTimer != null && automaticUpdateTimer.isRunning()) {
                    automaticUpdateTimer.stop();
                }
            });
            // Start automatic updates initially
            startAutomaticUpdates();
        }
    }

    private void startAutomaticUpdates() {
        // Only start the timer if it's not already running
        if (automaticUpdateTimer == null || !automaticUpdateTimer.isRunning()) {
            automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
                // Check if cache is empty before clearing it
                if (!cache.isEmpty()) {
                    cache.clearCache();
                }
                // Fetch channels from the server
                List<Channel> updatedChannels = fetchChannelsFromXML();

                for (Channel channel : updatedChannels) {
                    JButton button = new JButton(channel.getChannelName());
                    button.setIcon(new ImageIcon(channel.getChannelImage()));
                    button.addActionListener(e1 -> {
                        this.selectedChannel = channel;
                        ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
                        List<Schedule> schedules = scheduleParser.fetchSchedules();
                        programView.populateProgramTable(schedules);
                        // TODO: check if channel things get updated properly or not.
                        // Selected channel's name should be visible.
                        menuBarView.setSelectedChannelLabel(channel.getChannelName());
                    });
                    channelView.addChannelButton(button);
                }
                // Update the last updated time in the panel
                menuBarView.updateLastUpdatedTime();
            });
            automaticUpdateTimer.start();
        }
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
