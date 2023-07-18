import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProgramView {
    private JFrame frame;
    private JScrollPane programScrollPane;
    private JTable programTable;
    private DefaultTableModel programTableModel;

    public ProgramView(JFrame frame) {
        this.frame = frame;
        String[] cols = {"Program", "Start", "End"};
        programTableModel = new DefaultTableModel(cols, 0);
        programTable = new JTable(programTableModel);
        programScrollPane = new JScrollPane(programTable);
    }

    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }

    public void populateProgramTable(List<Schedule> schedules) {
        programTableModel.setRowCount(0); // Clear previous data

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // Format for time display

        for (Schedule schedule : schedules) {
            String[] rowData = {
                    schedule.getProgramName(),
                    schedule.getStartTime().format(timeFormatter),
                    schedule.getEndTime().format(timeFormatter)
            };
            programTableModel.addRow(rowData);
        }
    }
}
