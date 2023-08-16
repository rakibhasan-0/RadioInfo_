import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProgramView {
    private JTable programTable;
    private JScrollPane programScrollPane;
    private List<Schedule> schedules;

    public ProgramView(JFrame frame) {
        programTable = new JTable() {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? JButton.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        programScrollPane = new JScrollPane(programTable);
        frame.add(programScrollPane, BorderLayout.CENTER);
    }

    public void populateProgramTable(List<Schedule> schedules, Controller controller) {
        this.schedules = schedules;
        programTable.setRowHeight(25);

        String[] columnNames = {"Program Name", "Start Time", "End Time"};
        Object[][] data = new Object[schedules.size()][3];

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            data[i][0] = schedule.getProgramName();
            data[i][1] = schedule.getStartTime();
            data[i][2] = schedule.getEndTime();
        }

        programTable.setModel(new DefaultTableModel(data, columnNames));
        programTable.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        programTable.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JCheckBox(), schedules, controller));
    }

    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }
}
