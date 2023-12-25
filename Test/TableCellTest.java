import Controll.Controller;
import Model.Channel;
import Model.ChannelBuilder;
import View.ButtonEditor;
import View.MenuBarView;
import View.ProgramView;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TableCellTest {
    @Test
    public void checkIfTableCellGetsClicked() throws InterruptedException {

        ProgramView programView = new ProgramView();
        MenuBarView menuBarView = new MenuBarView();
        programView.addCardPanel();
        Controller controller = new Controller(menuBarView, programView);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS);

        JMenuItem menuItem = menuBarView.getMenuBar().getMenu(2).getItem(3);
        String category = menuItem.getText();
        menuItem.doClick();
        latch.await(10, TimeUnit.SECONDS);
        //System.out.println("it is empty:  "+ controller.getChannels().get(category));

        JButton button = null;
        Component [] components = programView.getChannelPane().getComponents();
        for(Component component : components) {
            if(component instanceof JButton){
                button = (JButton) component;
                break;
            }
        }

        Channel p1 = new ChannelBuilder()
                        .setChannelName(button.getText())
                        .setScheduleURL("http://api.sr.se/v2/scheduledepisodes?channelid=132")
                        .build();

        controller.onChannelSelected(p1);
        latch.await(10, TimeUnit.SECONDS);
        JButton tableButton = getButtonFromCell(programView);
        assertNotNull(tableButton);
        tableButton.doClick();
        latch.await(3, TimeUnit.SECONDS);
        tableButton.addActionListener(e -> {
            String text = controller.getDescriptionLabelFromTable().getText();
            assertFalse(text.isEmpty());
        });

    }


    private JButton getButtonFromCell(ProgramView programView) {
        JTable table = programView.getProgramTable();
        // System.out.println(table.getRowCount());
        // System.out.println(table.getColumnCount());

        if (table.getRowCount() > 2 && table.getColumnCount() > 0) {
            TableCellEditor cellEditor = table.getCellEditor(2, 0);
            if (cellEditor instanceof ButtonEditor buttonEditor) {
                return buttonEditor.getButtonEditor();
            }
        }
        return null;
    }








}
