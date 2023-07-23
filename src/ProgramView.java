import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProgramView {
    private JTable programTable;
    private JScrollPane programScrollPane;

    public ProgramView(JFrame frame) {
        programTable = new JTable();
        programScrollPane = new JScrollPane(programTable);

        frame.add(programScrollPane, BorderLayout.CENTER);
    }

    public void populateProgramTable(List<Schedule> schedules) {
        String[] columnNames = {"Program Name", "Start Time", "End Time"};
        Object[][] data = new Object[schedules.size()][3];

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            data[i][0] = schedule.getProgramName();
            data[i][1] = schedule.getStartTime();
            data[i][2] = schedule.getEndTime();
        }

        programTable.setModel(new DefaultTableModel(data, columnNames));
    }

    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }
}
