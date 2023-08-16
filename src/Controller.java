import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private MenuBarView menuBarView;
    private Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;

    public Controller(ChannelView channelView, ProgramView programView, MenuBarView menuBarView) {
        this.channelView = channelView;
        this.programView = programView;
        this.menuBarView = menuBarView;
        this.cache = new Cache();

        // Fetch channels using the XMLParser
        List<Channel> channels = fetchChannelsFromXML();

        // Check if channels are available
        if (channels != null && !channels.isEmpty()) {
            setupChannelButtons(channels);
            setupMenuListeners();
            startAutomaticUpdates();
        }
    }

    private void setupChannelButtons(List<Channel> channels) {
        channelView.clearChannelButtons();
        for (Channel channel : channels) {
            JButton button = new JButton(channel.getChannelName());
            button.setIcon(new ImageIcon(channel.getChannelImage()));
            button.addActionListener(e -> updateProgramTable(channel));
            channelView.addChannelButton(button);
        }
    }


    private void updateProgramTable(Channel channel) {
        selectedChannel = channel;

        List<Schedule> schedules = cache.getSchedules(channel);

        if (schedules == null) {
            ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
            schedules = scheduleParser.fetchSchedules();
            if (schedules.isEmpty()) {
               // programView.setProgramTextField(); // Update the text field
                menuBarView.setSelectedChannelLabel(channel.getChannelName());
                return;
            }
            cache.addSchedules(channel, schedules);
        }

        programView.populateProgramTable(schedules,this);
        menuBarView.setSelectedChannelLabel(channel.getChannelName());
    }

    private void startAutomaticUpdates() {
        automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
            // Update the channel buttons
            List<Channel> updatedChannels = fetchChannelsFromXML();
            setupChannelButtons(updatedChannels);
            // Update the last updated time in the panel
            menuBarView.updateLastUpdatedTime();
        });
        automaticUpdateTimer.start();
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

    private void setupMenuListeners() {
        // Action listener for the "Update Channel" menu item
        menuBarView.addUpdateChannelListener(e -> handleUpdateChannel());

        // Action listener for the "Update Schedule" menu item
        menuBarView.addUpdateScheduleListener(e -> handleUpdateSchedule());
    }

    private void handleUpdateChannel() {
        cache.clearCacheForAChannel(selectedChannel);
        List<Channel> updatedChannels = fetchChannelsFromXML();
        setupChannelButtons(updatedChannels);
        menuBarView.updateLastUpdatedTime();
        resetAutomaticUpdates();
    }

    private void handleUpdateSchedule() {
        cache.clearCacheForAChannel(selectedChannel);
        updateProgramTable(selectedChannel);
        menuBarView.updateLastUpdatedTime();
        resetAutomaticUpdates();
    }

    private void resetAutomaticUpdates() {
        if (automaticUpdateTimer != null && automaticUpdateTimer.isRunning()) {
            automaticUpdateTimer.stop();
        }
        startAutomaticUpdates();
    }

    public void addProgramTableActionListener(JButton button, Schedule schedule) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProgramDetails(schedule);
            }
        });
    }

    public void showProgramDetails(Schedule schedule) {
        String programDetails = "\n\n Program Name: " + schedule.getProgramName() +
                "\nStart Time: " + schedule.getStartTime() +
                "\nEnd Time: " + schedule.getEndTime() +
                "\nDescription: " + schedule.getDescription();

        try {
            if (schedule.getImageUrl() != null && !schedule.getImageUrl().isEmpty()) {
                URL imageUrl = new URL(schedule.getImageUrl());
                BufferedImage image = ImageIO.read(imageUrl);

                if (image != null) {
                    ImageIcon imageIcon = new ImageIcon(image);

                    JLabel label = new JLabel();
                    label.setText("<html>" + programDetails.replaceAll("\n", "<br>") + "</html>");
                    label.setIcon(imageIcon);

                    JOptionPane.showMessageDialog(null, label);
                } else {
                    // Image is null, so just display the text
                    JOptionPane.showMessageDialog(null, programDetails);
                }
            } else {
                // Image URL is null or empty, so just display the text
                JOptionPane.showMessageDialog(null, programDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Exception occurred, so just display the text
            JOptionPane.showMessageDialog(null, programDetails);
        }
    }


}
