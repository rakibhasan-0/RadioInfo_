import javax.swing.*;

public class MenuBarView {
    JFrame frame;
    JPanel menuPanel;
    JMenuBar menuBar;

    public MenuBarView(JFrame frame){

        this.frame = frame;
        menuBar = new JMenuBar();

        JMenu channel = new JMenu("Channel");
        JMenu schedule = new JMenu("Schudle");

        JMenuItem updateChannel = new JMenuItem("Update");
        JMenuItem updateSchedule = new JMenuItem("Update");

        channel.add(updateChannel);
        schedule.add(updateSchedule);
        menuBar.add(channel);
        menuBar.add(schedule);

        menuPanel = new JPanel();
        menuPanel.add(menuBar);

        frame.add(menuPanel);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public JFrame getFrame(){
        return this.frame;
    }

    public JMenuBar getMenuBar(){
        return this.menuBar;
    }

}
