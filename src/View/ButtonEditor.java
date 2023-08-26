package View;

import Controll.Controller;
import Model.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ButtonEditor extends DefaultCellEditor implements ActionListener {
    private JButton button;
    private List<Schedule> schedules;
    private Controller controller;
    private int row;

    public ButtonEditor(JCheckBox checkBox, List<Schedule> schedules, Controller controller) {
        super(checkBox);
        this.schedules = schedules;
        this.controller = controller;
        button = new JButton("Details");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return schedules.get(row).getProgramName();  // Return the correct string instead of a boolean value.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        controller.showProgramDetails(schedules.get(row));
    }
}
