package View;
import javax.swing.*;
import java.awt.*;

public class ProgramView{
    private JTable programTable;
    private JScrollPane programScrollPane;
    private  JPanel cardPanel;
    JPanel programDetailsPanel;
    private  JPanel channelPane;
    private JScrollPane scrollChannel;

    public ProgramView() {

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
        programTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        programTable.setRowHeight(30);
        programScrollPane = new JScrollPane(programTable);

    }

    public JTable getProgramTable(){
        return programTable;
    }

    public JPanel addCardPanel(){
        cardPanel = new JPanel();
        programDetailsPanel = new JPanel(new BorderLayout());
        cardPanel.setLayout(new CardLayout());
        cardPanel.add("programsList", programScrollPane);
        cardPanel.add("programDetails",programDetailsPanel);
        return cardPanel;
    }

    public JPanel programDetailsPanel(){
        return programDetailsPanel;
    }
    public JPanel getCardPanel(){
        return cardPanel;
    }
    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
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
