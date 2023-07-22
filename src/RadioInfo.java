import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RadioInfo {
    public RadioInfo() {
        JFrame frame = new JFrame("Radio Info");

        MenuBarView menuBar = new MenuBarView(frame);

        ChannelView channelView = new ChannelView(frame);
        ProgramView programView = new ProgramView(frame);
        XMLParser xmlParser = new XMLParser();

        Cache cache = new Cache();

        xmlParser.execute(); // Start the XMLParser task

        // Wait for the XMLParser task to finish
        try {
            List<Channel> channels = xmlParser.get();
            Controller controller = new Controller(channelView, programView, channels);

            JPanel menuPanel = new JPanel();
            menuPanel.add(channelView.getScrollChannel());
            menuPanel.add(programView.getProgramScrollPane());
            menuPanel.setLayout(new GridLayout(1, 2));

            frame.setJMenuBar(menuBar.getMenuBar());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.add(menuPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RadioInfo::new);
    }
}
