package View;

import Controll.ShowMoreButtonListener;
import Model.Schedule;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor{
    private JButton button;
    private ShowMoreButtonListener listener;
    private ArrayList<Schedule> schedules;
    private String label;
    private int row;

    public ButtonEditor(ArrayList<Schedule> schedules, ShowMoreButtonListener buttonClickListener) {
        this.schedules = schedules;
        this.listener = buttonClickListener;
        this.button = new JButton();
        this.button.setOpaque(true);
        this.button.addActionListener(e -> {
            fireEditingStopped();
            if (row >= 0 && row < schedules.size()) {
                listener.onButtonClick(schedules.get(row));
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        this.row = row;
        return button;
    }

}
