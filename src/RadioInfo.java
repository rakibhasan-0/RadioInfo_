import javax.swing.*;
import java.awt.*;

public class RadioInfo {
    private JFrame frame;

    public RadioInfo(){

        frame = new JFrame("Radio Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        MenuBarView menuBar = new MenuBarView();

        ChannelView channelView = new ChannelView(frame);
        ProgramView programView = new ProgramView(frame);

        JPanel menuPanel = new JPanel();
        menuPanel.add(channelView.getChannelPanel());
        menuPanel.add(programView.getProgramScrollPane());
        menuPanel.setLayout(new GridLayout(1, 2));

        frame.setJMenuBar(menuBar.getMenuBar());
        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
