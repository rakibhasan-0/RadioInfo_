import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private MenuBarView menuBarView;
    private Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;

    public Controller(ChannelView channelView, MenuBarView menuBarView, JFrame frame) {
        this.channelView = channelView;
        this.programView = new ProgramView(frame,this);
        this.menuBarView = menuBarView;
        this.cache = Cache.getInstance();
        cache.registerObserver(programView);
        List<Channel> channels = fetchChannelsFromXML();

        // should I handle null pointer exceptions in that case?
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
            if (schedules.isEmpty()){
                menuBarView.setSelectedChannelLabel(channel.getChannelName());
                return;
            }
            cache.addSchedules(channel, schedules);
        }

        programView.populateProgramTable(schedules);
        menuBarView.setSelectedChannelLabel(channel.getChannelName());
    }

    private void startAutomaticUpdates() {
        automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
            List<Channel> updatedChannels = fetchChannelsFromXML();
            setupChannelButtons(updatedChannels);
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
        menuBarView.addUpdateChannelListener(e -> handleUpdateChannel());
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
                    JOptionPane.showMessageDialog(null, programDetails);
                }
            } else {
                JOptionPane.showMessageDialog(null, programDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, programDetails);
        }
    }

    ProgramView getProgramView(){
        return this.programView;
    }
}
