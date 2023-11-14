package Controll;
import Model.*;
import View.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller implements Observer {
    private final ChannelView channelView;
    private final ProgramView programView;
    private final MenuBarView menuBarView;
    private final Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;

    public Controller(ChannelView channelView, MenuBarView menuBarView, JFrame frame) {
        this.channelView = channelView;
        this.menuBarView = menuBarView;

        initializeNorthPanel(frame);

        this.programView = new ProgramView( this);
        this.cache = Cache.getInstance();
        XMLParserWorker xmlWorker = new XMLParserWorker();
        xmlWorker.execute();
        xmlWorker.registerObserver(this);
        setupMenuListeners();

    }

    private void initializeNorthPanel(JFrame frame) {
        JPanel combinedNorthPanel = new JPanel(new BorderLayout());
        combinedNorthPanel.add(menuBarView.getTimePanel(), BorderLayout.NORTH);
        combinedNorthPanel.add(channelView.getSpinner(), BorderLayout.CENTER);
        frame.add(combinedNorthPanel, BorderLayout.NORTH);
    }


    private void handleUpdateSchedule() {
        if (selectedChannel != null) {
            fetchScheduleForChannel(selectedChannel);
            menuBarView.updateLastUpdatedTime();
            resetAutomaticUpdates();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a channel first before updating the schedule.");
        }
    }


    private void fetchScheduleForChannel(Channel channel) {
        ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
        List<Schedule> schedules = scheduleParser.fetchSchedules();
        cache.addSchedules(channel, schedules);
        populateProgramTable(schedules);
    }

    public void populateProgramTable(List<Schedule> schedules) {
        schedules = schedules;
        String[] columnNames = {"Program Name", "Start Time", "End Time"};
        Object[][] data = new Object[schedules.size()][3];

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            data[i][0] = schedule.getProgramName();
            data[i][1] = schedule.getStartTime();
            data[i][2] = schedule.getEndTime();
        }

        programView.getProgramTable().setModel(new DefaultTableModel(data, columnNames));
        programView.getProgramTable().getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        programView.getProgramTable().getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JCheckBox(), schedules, this));

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



    private void handleUpdate() {
        cache.clearCacheForAChannel(selectedChannel);
        // data will be fetched from the XML server.
        menuBarView.updateLastUpdatedTime();
        resetAutomaticUpdates();
    }

    private void setupMenuListeners() {
        menuBarView.addUpdateChannelListener(e -> {
            //System.out.println("Update Channel Button Clicked!");
            handleUpdate();
        });

        menuBarView.addUpdateScheduleListener(e -> {
            //System.out.println("Update Schedule Button Clicked!");
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
            // new data will be fetched from the XML server.
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

        populateProgramTable(schedules);
        menuBarView.setSelectedChannelLabel(channel.getChannelName());
    }

    public void update(ArrayList<Channel> channels) {
        SwingUtilities.invokeLater(() -> {
            setupChannelButtons(channels);
            menuBarView.updateLastUpdatedTime();
            resetAutomaticUpdates();
            channelView.hideSpinner();
        });
    }

    @Override
    public void channelUpdate(ArrayList<Channel> channels) {

    }
}