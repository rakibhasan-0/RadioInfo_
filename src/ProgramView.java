import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProgramView {
    private JFrame frame;
    private JScrollPane programScrollPane;
    private JTable programTable;

    public ProgramView(JFrame frame) {
        this.frame = frame;
        String[] cols = {"Program", "Start", "End"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        programTable = new JTable(model);
        programScrollPane = new JScrollPane(programTable);
    }

    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }

    public JTable getProgramTable() {
        return programTable;
    }

}
