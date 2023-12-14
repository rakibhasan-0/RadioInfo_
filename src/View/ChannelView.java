package View;
import javax.swing.*;
import java.awt.*;

public class ChannelView {

    private final JPanel channelPane;

    private JScrollPane scrollChannel;


    public ChannelView() {
        channelPane = new JPanel();
        JPanel panel = new JPanel();
        channelPane.setLayout(new BoxLayout(channelPane, BoxLayout.Y_AXIS));
        scrollChannel = new JScrollPane(channelPane);
        JLabel label = new JLabel("Select A Channel Type");
        panel.setLayout(new BorderLayout());
        channelPane.add(panel);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 30));
        label.setForeground(Color.BLACK);
        panel.add(label, BorderLayout.CENTER);
        scrollChannel.setPreferredSize(new Dimension(320, 300));
    }


    public void addChannelButton(JButton channelButton) {
        channelButton.setPreferredSize(new Dimension(280, 50));
        channelButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        channelPane.add(channelButton);
        channelPane.revalidate();
        channelPane.repaint();
    }


    public void clearChannelButtons() {
        channelPane.removeAll();
        channelPane.revalidate();
        channelPane.repaint();
    }



    public JScrollPane getScrollChannel() {
        return scrollChannel;
    }


}