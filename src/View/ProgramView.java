package View;
import Controll.Controller;
import Model.Schedule;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProgramView{
    private JTable programTable;
    private JScrollPane programScrollPane;
    private List<Schedule> schedules;
    private Controller controller;

    public ProgramView( Controller controller) {
        this.controller = controller;
        programTable = new JTable();
        programTable.setRowHeight(25);
        programScrollPane = new JScrollPane(programTable);
    }

    public JTable getProgramTable(){
        return programTable;
    }


    public JScrollPane getProgramScrollPane() {
        return programScrollPane;
    }
}
