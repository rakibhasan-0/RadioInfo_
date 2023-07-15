import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChannelView {
    private JPanel channelPanel;
    private JFrame frame;
    private JScrollPane scrollChannel;

    public ChannelView(JFrame frame) {
        this.frame = frame;
        channelPanel = new JPanel(new GridBagLayout());
        scrollChannel = new JScrollPane(channelPanel);
        scrollChannel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void addChannelButton(String channelName) {
        JButton channelButton = new JButton(channelName);
        channelButton.setPreferredSize(new Dimension(140, 60));
        channelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click event for the selected channel
                // Retrieve and display the program information for the selected channel
            }
        });
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 1, 1, 1);
        channelPanel.add(channelButton, constraints);
        frame.revalidate();
    }

    public JPanel getChannelPanel() {
        return channelPanel;
    }

    public JScrollPane getScrollChannel() {
        return scrollChannel;
    }
}
