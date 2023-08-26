package Controll;

import Controll.Observer;
import Model.*;
import View.ChannelView;
import View.MenuBarView;
import View.ProgramView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Controller implements Observer {

    private final ChannelView channelView;
    private final ProgramView programView;
    private final MenuBarView menuBarView;
    private final Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;
    private final XMLParser parser;

    public Controller(ChannelView channelView, MenuBarView menuBarView, JFrame frame) {
        this.channelView = channelView;
        this.menuBarView = menuBarView;

        initializeNorthPanel(frame);

        this.programView = new ProgramView(frame, this);
        this.cache = Cache.getInstance();
        this.parser = new XMLParser();
        parser.registerObserver(this);
        setupMenuListeners();
        fetchChannelsFromXML();
    }

    private void initializeNorthPanel(JFrame frame) {
        JPanel combinedNorthPanel = new JPanel(new BorderLayout());
        combinedNorthPanel.add(menuBarView.getTimePanel(), BorderLayout.NORTH);
        combinedNorthPanel.add(channelView.getSpinner(), BorderLayout.CENTER);
        frame.add(combinedNorthPanel, BorderLayout.NORTH);
    }

    private void fetchChannelsFromXML() {
        try {
            parser.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateSchedule() {
        if (selectedChannel != null) {
            fetchScheduleForChannel(selectedChannel);
            menuBarView.updateLastUpdatedTime();
            resetAutomaticUpdates();
        } else {
            // Show a dialog informing the user to select a channel first.
            JOptionPane.showMessageDialog(null, "Please select a channel first before updating the schedule.");
        }
    }


    private void fetchScheduleForChannel(Channel channel) {
        // Fetch the updated schedules for the given channel
        ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
        List<Schedule> schedules = scheduleParser.fetchSchedules();

        // Update the cache with the new schedules
        cache.addSchedules(channel, schedules);

        // Update the program view with the new schedules
        programView.populateProgramTable(schedules);
    }


    private void setupChannelButtons(List<Channel> channels) {
        if (channels == null) return;

        channelView.clearChannelButtons();
        for (Channel channel : channels) {
            JButton button = new JButton(channel.getChannelName());
            button.setIcon(new ImageIcon(channel.getChannelImage()));
            button.addActionListener(e -> updateProgramTable(channel));
            channelView.addChannelButton(button);
        }
    }

    @Override
    public void update() {
        try {
            List<Channel> channels = parser.get();
            if (channels != null && !channels.isEmpty()) {
                setupChannelButtons(channels);
                menuBarView.updateLastUpdatedTime();
                resetAutomaticUpdates();
                channelView.hideSpinner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUpdate() {
        cache.clearCacheForAChannel(selectedChannel);
        fetchChannelsFromXML();
        menuBarView.updateLastUpdatedTime();
        resetAutomaticUpdates();
    }

    private void setupMenuListeners() {
        menuBarView.addUpdateChannelListener(e -> {
            System.out.println("Update Channel Button Clicked!");
            handleUpdate();
        });

        menuBarView.addUpdateScheduleListener(e -> {
            System.out.println("Update Schedule Button Clicked!");
            handleUpdateSchedule();
        });
    }

    private void resetAutomaticUpdates() {
        if (automaticUpdateTimer != null && automaticUpdateTimer.isRunning()) {
            automaticUpdateTimer.stop();
        }
        startAutomaticUpdates();
    }

    private void startAutomaticUpdates() {
        automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
            fetchChannelsFromXML();
            menuBarView.updateLastUpdatedTime();
        });
        automaticUpdateTimer.start();
    }

    public void addProgramTableActionListener(JButton button, Schedule schedule) {
        button.addActionListener(e -> showProgramDetails(schedule));
    }
    public void showProgramDetails(Schedule schedule) {
        if (schedule == null) {
            // Handle null schedule
            JOptionPane.showMessageDialog(null, "No schedule details available.");
            return;
        }

        String programDetails = formatProgramDetails(schedule);

        try {
            if (isImageUrlValid(schedule)) {
                BufferedImage image = fetchProgramImage(schedule);
                if (image != null) {
                    displayProgramDetailsWithImage(programDetails, image);
                } else {
                    displayProgramDetailsWithoutImage(programDetails);
                }
            } else {
                displayProgramDetailsWithoutImage(programDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayProgramDetailsWithoutImage(programDetails);
        }
    }

    private String formatProgramDetails(Schedule schedule) {
        return "\n\n Program Name: " + schedule.getProgramName() +
                "\nStart Time: " + schedule.getStartTime() +
                "\nEnd Time: " + schedule.getEndTime() +
                "\nDescription: " + schedule.getDescription();
    }

    private boolean isImageUrlValid(Schedule schedule) {
        return schedule.getImageUrl() != null && !schedule.getImageUrl().isEmpty();
    }

    private BufferedImage fetchProgramImage(Schedule schedule) throws IOException {
        URL imageUrl = new URL(schedule.getImageUrl());
        return ImageIO.read(imageUrl);
    }

    private void displayProgramDetailsWithImage(String programDetails, BufferedImage image) {
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel label = new JLabel();
        label.setText("<html>" + programDetails.replaceAll("\n", "<br>") + "</html>");
        label.setIcon(imageIcon);

        JOptionPane.showMessageDialog(null, label);
    }

    private void displayProgramDetailsWithoutImage(String programDetails) {
        JOptionPane.showMessageDialog(null, programDetails);
    }

    public ProgramView getProgramView() {
        return this.programView;
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
}