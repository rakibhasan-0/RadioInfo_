package View;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


/**
 * This class allows each cell of the table to be displayed as a button.
 */

public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }

}
