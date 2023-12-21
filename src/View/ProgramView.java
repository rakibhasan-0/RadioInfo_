package View;
import javax.swing.*;
import java.awt.*;


/**
 * This class reponsible for showing the table that holds information about the selected
 * channel's information on the GUI. At the same time, it will be responsible for displaying
 * a specific program's detailed information.
 * @author Gazi Md Rakibul Hasan.
 */
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

    /**
     * This method returns the JTable which will be used to display the program's schedules on the
     * gui.
     * @return the JTable.
     */
    public JTable getProgramTable(){
        return programTable;
    }

    /**
     * This method returns the panel which used the card layout to display
     * the list of programs and prorgam's details.
     * @return A panel.
     */
    public JPanel addCardPanel(){
        cardPanel = new JPanel();
        programDetailsPanel = new JPanel(new BorderLayout());
        cardPanel.setLayout(new CardLayout());
        cardPanel.add("programsList", programScrollPane);
        cardPanel.add("programDetails",programDetailsPanel);
        return cardPanel;
    }

    /**
     * It returns the panel which holds the information about a program's details
     * information.
     * @return A panel.
     */
    public JPanel programDetailsPanel(){
        return programDetailsPanel;
    }

    /**
     * It returns the panel which holds the information about the panel where
     * the card panel has been created.
     * @return a panel.
     */
    public JPanel getCardPanel(){
        return cardPanel;
    }


    /**
     *  It is a scrollable panel where it will display the channel's program details information.
     * @return a scrollable panel.
     */
    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }


    /**
     * It adds a button on the channel panel.
     * @param channelButton the button.
     */
    public void addChannelButton(JButton channelButton) {
        channelButton.setPreferredSize(new Dimension(280, 50));
        channelButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        channelPane.add(channelButton);
        channelPane.revalidate();
        channelPane.repaint();
    }


    /**
     * It clears all existing buttons from the gui.
     */
    public void clearChannelButtons() {
        channelPane.removeAll();
        channelPane.revalidate();
        channelPane.repaint();
    }

    /**
     * A panles that stotres all displaying channel on the GUI.
     * @return a scrollable panel.
     */
    public JScrollPane getScrollChannel() {
        return scrollChannel;
    }
}
