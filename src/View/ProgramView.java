package View;
import javax.swing.*;
import java.awt.*;

public class ProgramView{
    private JTable programTable;
    private JScrollPane programScrollPane;
    private  JPanel cardPanel;
    JPanel programDetailsPanel;

    public ProgramView() {
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
}
