package View;

import javax.swing.*;
import java.awt.*;


public class ChannelView {
    private JPanel channelPanel;
    private JScrollPane scrollChannel;
    private JProgressBar spinner;


    public ChannelView() {
        channelPanel = new JPanel(new GridBagLayout());
        scrollChannel = new JScrollPane(channelPanel);
        spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setStringPainted(true);
        spinner.setString("Loading channels...");
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

    public JScrollPane getScrollPane() {
        return scrollChannel;
    }


    public void clearChannelButtons() {
        channelPanel.removeAll();
        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public void hideSpinner() {
        spinner.setVisible(false);
        channelPanel.revalidate();
        channelPanel.repaint();
    }

    public JScrollPane getScrollChannel() {
        return scrollChannel;
    }

    public JComponent getSpinner() {
        return spinner; // assuming spinner is a member variable in ChannelView
    }


}