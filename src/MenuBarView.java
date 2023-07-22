import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuBarView {
    private JFrame frame;
    private JMenuBar menuBar;
    private JLabel currentTimeLabel;
    private JLabel lastUpdatedLabel;
    private JPanel timePanel;
    private LocalDateTime lastUpdatedTime; // Store the last updated time

    public MenuBarView(JFrame frame) {
        this.frame = frame;
        menuBar = new JMenuBar();

        JMenu channel = new JMenu("Channel");
        JMenu schedule = new JMenu("Schedule");

        JMenuItem updateChannel = new JMenuItem("Update");
        JMenuItem updateSchedule = new JMenuItem("Update");

        channel.add(updateChannel);
        schedule.add(updateSchedule);
        menuBar.add(channel);
        menuBar.add(schedule);

        timePanel = new JPanel(new GridLayout(1, 2));
        currentTimeLabel = new JLabel();
        lastUpdatedLabel = new JLabel();

        timePanel.add(currentTimeLabel);
        timePanel.add(lastUpdatedLabel);

        frame.add(timePanel, BorderLayout.NORTH);
        frame.setJMenuBar(menuBar);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start updating the current time label every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCurrentTimeLabel();
            }
        });
        timer.start();

        // Set initial values for current time and last updated time
        updateCurrentTimeLabel();
        updateLastUpdatedTime();
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    public void setCurrentTimeLabel(LocalDateTime currentTime) {
        String formattedTime = "Current Time: " + currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        currentTimeLabel.setText(formattedTime);
    }

    public void setLastUpdatedLabel(LocalDateTime lastUpdatedTime) {
        String formattedTime = "Last Updated: " + lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lastUpdatedLabel.setText(formattedTime);
    }

    public void addUpdateChannelListener(ActionListener listener) {
        JMenuItem updateChannel = menuBar.getMenu(0).getItem(0);
        updateChannel.addActionListener(listener);
    }

    public void addUpdateScheduleListener(ActionListener listener) {
        JMenuItem updateSchedule = menuBar.getMenu(1).getItem(0);
        updateSchedule.addActionListener(listener);
    }

    public void updateLastUpdatedTime() {
        lastUpdatedTime = LocalDateTime.now();
        setLastUpdatedLabel(lastUpdatedTime);
    }

    public void updateCurrentTimeLabel() {
        setCurrentTimeLabel(LocalDateTime.now());
    }
}
