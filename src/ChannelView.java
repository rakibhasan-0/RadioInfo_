import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChannelView {
    private JPanel channelPanel;
    private JFrame frame;
    private JScrollPane scrollChannel;

    public ChannelView(JFrame frame) {
        this.frame = frame;
        channelPanel = new JPanel(new GridBagLayout());
        scrollChannel = new JScrollPane(channelPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        constraints.gridwidth = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 1, 1, 1);

        frame.revalidate();
        scrollChannel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void addChannelButton(JButton channelButton) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridy = channelPanel.getComponentCount();
        constraints.insets = new Insets(5, 10, 5, 10);
        channelPanel.add(channelButton, constraints);

        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public JPanel getChannelPanel() {
        return channelPanel;
    }

    public JScrollPane getScrollChannel() {
        return scrollChannel;
    }

    // Method to update the list of channels in the view
    public void updateChannels(List<Channel> channels) {
        channelPanel.removeAll(); // Clear existing channels

        for (Channel channel : channels) {
            JButton button = new JButton(channel.getChannelName());
            button.setIcon(new ImageIcon(channel.getChannelImage()));
            addChannelButton(button);
        }

        channelPanel.revalidate();
        channelPanel.repaint();
    }
}
