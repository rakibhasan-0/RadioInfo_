package View;
import Controll.ChannelListener;
import Model.Schedule;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;


/**
 * This is a custom TableCellEditor that places a JButton in a table cell.
 * It allows for interactive buttons within the table cells.
 * @author Gazi Md Rakibul Hasan
 */

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor{
    private final JButton button;
    private final ChannelListener listener;
    private String label;
    private int row;


    /**
     * Constructor for ButtonEditor.
     *
     * @param schedules An ArrayList of Schedule objects
     *                  it used to determine the action on button click.
     * @param buttonClickListener The listener that handles button click events.
     */
    public ButtonEditor(ArrayList<Schedule> schedules, ChannelListener buttonClickListener) {
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

    public JButton getButtonEditor() {
        return button;
    }

}
