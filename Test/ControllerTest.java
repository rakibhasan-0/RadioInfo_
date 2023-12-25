import Controll.Controller;
import Model.Channel;
import Model.ScheduleParser;
import View.MenuBarView;
import View.ProgramView;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;


/**
 * We will check the controller class.
 */
public class ControllerTest {
    /**
     * The purpose of this method is to test the channel update.
     */
    @Test
    public void testChannelUpdate() throws InterruptedException {
        MenuBarView menuBarView = new MenuBarView();
        ProgramView programView = new ProgramView();
        Controller controller = new Controller(menuBarView, programView);
        assertNull(controller.getApiManager().getChannels());
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(20, TimeUnit.SECONDS);
        assertFalse(controller.getApiManager().getChannels().isEmpty()); // it can't be true...
        controller.getApiManager().getChannels().clear(); // clear it...
        JMenuItem item = menuBarView.getMenuBar().getMenu(0).getItem(0);
        SwingUtilities.invokeLater(item::doClick);
        latch.await(20, TimeUnit.SECONDS);
        assertFalse(controller.getApiManager().getChannels().isEmpty());
    }


    /**
     * Test if the schedule gets updated by clicking one of the menus itme...
     */
    @Test
    public void testCheckScheduleGetUpdated() throws InterruptedException {
        // UI gets added on the controller.
        MenuBarView menuBarView = new MenuBarView();
        ProgramView programView = new ProgramView();
        Controller controller = new Controller(menuBarView, programView);
        assertNull(controller.getApiManager().getChannels());
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(20, TimeUnit.SECONDS);
        Channel channel = controller.getApiManager().getChannels().get(0);
        assertNull(controller.getCache().getSchedules(channel)); // there is no cahche for this channel...
        ScheduleParser parser = new ScheduleParser(channel);
        controller.getSchedule(channel,parser.getScheduleList()); // now cache will be available..
        JMenuItem item = menuBarView.getMenuBar().getMenu(1).getItem(0); // in index there is a channel item..
        item.doClick(); // let clcik the schedule item.....
        latch.await(20, TimeUnit.SECONDS);
        assertNotNull(controller.getCache().getSchedules(channel));
    }


    /**
     * Test to ensure that selecting a channel category displays the correct channels in the GUI.
     */
    @Test
    public void testChannelSelectionDisplaysCorrectChannels() throws InterruptedException {
        MenuBarView menuBarView = new MenuBarView();
        ProgramView programView = new ProgramView();
        Controller controller = new Controller(menuBarView, programView);
        waitForApiChannels(controller);
        String selectedCategory = selectChannelCategory(menuBarView);
        assertDisplayedChannelsMatchCategory(programView, controller, selectedCategory);
    }

    private void waitForApiChannels(Controller controller) throws InterruptedException {
        CountDownLatch initialLoadLatch = new CountDownLatch(1);
        initialLoadLatch.await(13, TimeUnit.SECONDS);
        assertNotNull(controller.getApiManager().getChannels());
    }

    private String selectChannelCategory(MenuBarView menuBarView) throws InterruptedException {
        JMenuItem categoryItem = menuBarView.getMenuBar().getMenu(2).getItem(0);
        String channelCategory = categoryItem.getText();
        categoryItem.doClick();
        CountDownLatch updateLatch = new CountDownLatch(1);
        updateLatch.await(4, TimeUnit.SECONDS);
        return channelCategory;
    }

    private void assertDisplayedChannelsMatchCategory(ProgramView programView, Controller controller, String channelCategory) {
        ArrayList<JButton> channelButtons = getChannelButtons(programView);
        HashSet<String> expectedChannelNames = getChannelNamesForCategory(controller, channelCategory);

        for (JButton channelButton : channelButtons) {
            String channelName = channelButton.getText();
            assertTrue(expectedChannelNames.contains(channelName), "Channel name not found in category: " + channelName);
        }
    }

    private ArrayList<JButton> getChannelButtons(ProgramView programView) {
        ArrayList<JButton> channelButtons = new ArrayList<>();
        Component[] components = programView.getChannelPane().getComponents();

        for (Component component : components) {
            if (component instanceof JButton) {
                channelButtons.add((JButton) component);
            }
        }

        return channelButtons;
    }

    private HashSet<String> getChannelNamesForCategory(Controller controller, String channelCategory) {
        HashMap<String, ArrayList<Channel>> channelsByCategory = controller.getApiManager().getTheChannelsWithCategory();
        ArrayList<Channel> channels = channelsByCategory.get(channelCategory);

        HashSet<String> channelNames = new HashSet<>();
        for (Channel channel : channels) {
            channelNames.add(channel.getChannelName());
        }

        return channelNames;
    }


    /**
     * now by clicking a button aka a channel, its program schedule will be displayed.
     */
    @Test
    public void checkIfProgramScheduleIsVisible() throws InterruptedException {

        ProgramView programView = new ProgramView();
        MenuBarView menuBarView = new MenuBarView();
        Controller controller = new Controller(menuBarView, programView);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS);
        JMenuItem menuItem = menuBarView.getMenuBar().getMenu(2).getItem(3);
        menuItem.doClick();// rikskanal
        Component [] components = programView.getChannelPane().getComponents();
        JButton button = null;

        for(Component component : components) {
            if(component instanceof JButton){
                button = (JButton) component; // P1 will be selected.
                System.out.println("button: " + button.getText());
                break;
            }
        }

        assertNotNull(button, "the button object cannot be null");
        button.doClick();
        latch.await(4, TimeUnit.SECONDS);
        //programView.getProgramTable() the table cells should not empty.
        assertTrue(isSecondRowPopulatedWithProgramData(programView));

    }

    private boolean isSecondRowPopulatedWithProgramData(ProgramView programView) {
        JTable table = programView.getProgramTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        System.out.println(tableModel.getRowCount());
        return tableModel.getRowCount() >= 2;
    }


}












