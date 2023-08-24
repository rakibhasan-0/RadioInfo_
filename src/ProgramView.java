import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProgramView implements Observer {
    private JTable programTable;
    private JScrollPane programScrollPane;
    private List<Schedule> schedules;

    private Controller controller;

    public ProgramView(JFrame frame, Controller controller) {

        this.controller = controller;
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

        // Register this view as an observer of the cache
        Cache.getInstance().registerObserver(this);
    }

    @Override
    public void update() {
        // Get the latest data from the cache
        Channel selectedChannel = Cache.getInstance().getSelectedChannel();
        if (selectedChannel != null) {
            List<Schedule> schedules = Cache.getInstance().getSchedules(selectedChannel);
            populateProgramTable(schedules);
        }
    }

    public void populateProgramTable(List<Schedule> schedules) {
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
        // Assuming you have classes ButtonEditor and ButtonRenderer, ensure they're properly imported or referenced
        programTable.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        programTable.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JCheckBox(), schedules, controller));

    }

    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }
}
