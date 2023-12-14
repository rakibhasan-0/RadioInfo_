package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuBarView {
    private final JMenuBar menuBar;
    private final JLabel currentTimeLabel;
    private final JLabel channelUpdatedLabel;
    private final JPanel timePanel;
    private final JLabel selectedChannelLabel;
    private final JMenu channelTypes;
    private LocalDateTime lastUpdateTime;
    private final JLabel programUpdatedLabel;

    public MenuBarView() {
        menuBar = new JMenuBar();
        JMenu channel = new JMenu("Channel");
        JMenu schedule = new JMenu("Schedule");
        channelTypes = new JMenu("Channel Types");
        JMenuItem updateChannel = new JMenuItem("Update");
        JMenuItem updateSchedule = new JMenuItem("Update");
        channel.add(updateChannel);
        schedule.add(updateSchedule);
        menuBar.add(channel);
        menuBar.add(schedule);
        menuBar.add(channelTypes);
        timePanel = new JPanel(new GridLayout(2, 4));
        currentTimeLabel = new JLabel();
        programUpdatedLabel = new JLabel("Program updated: ");
        channelUpdatedLabel = new JLabel("Updating . . . .");
        timePanel.add(currentTimeLabel);
        timePanel.add(programUpdatedLabel);
        timePanel.add(channelUpdatedLabel);
        selectedChannelLabel = new JLabel("Selected Channel: ");
        timePanel.add(selectedChannelLabel);
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    public JLabel getChannelUpdatedLabel(){
        return channelUpdatedLabel;
    }


    public void addUpdateChannelListener(ActionListener listener) {
        JMenuItem updateChannelListener = menuBar.getMenu(0).getItem(0);
        updateChannelListener.addActionListener(listener);
    }

    public void addUpdateScheduleListener(ActionListener listener) {
        JMenuItem scheduleListener = menuBar.getMenu(1).getItem(0);
        scheduleListener.addActionListener(listener);
    }

    public void setSelectedChannelLabel(String channelName) {
        selectedChannelLabel.setText("Selected Channel: " + channelName);
    }

    public JLabel getProgramUpdatedLabel(){
        return programUpdatedLabel;
    }

    public JPanel getTimePanel() {
        return timePanel;
    }

    public void startClock() {
        Timer clockTimer = new Timer(1000, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            SwingUtilities.invokeLater(() -> setCurrentTimeLabel(currentTime));
        });
        clockTimer.start();
    }

    public void setCurrentTimeLabel(LocalDateTime currentTime) {
        String formattedTime = "Current Time: " + currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SwingUtilities.invokeLater(() -> currentTimeLabel.setText(formattedTime));
    }

    public JMenu getChannelsTypeMenu() {
        return channelTypes;
    }
}
