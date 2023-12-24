import View.MenuBarView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

public class MenuBarViewTest {
    private MenuBarView menuBarView;
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        this.menuBarView = new MenuBarView();
        menuBarView.startClock();
        this.frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(menuBarView.getMenuBar(), BorderLayout.CENTER);
        frame.setJMenuBar(menuBarView.getMenuBar());
        frame.setSize(300, 200);
    }

    /**
     * we will check if the time is shwoing and updating
     */
    @Test
    public void checkTimeIsShowing() throws InterruptedException {
        String initialTime = menuBarView.getCurrentTimeLabel().getText();
        // Wait for some time to allow the clock to update
        Thread.sleep(2000);
        String updatedTime = menuBarView.getCurrentTimeLabel().getText();
        // Check that the label is not null and the time has been updated
        assertNotEquals("Time should have updated", initialTime, updatedTime);
    }


    /**
     * It shows to user that If the category is visible to the user.
     */
    @Test
    public void testCannelCategory() throws Exception {
        JMenuItem riksKanal = new JMenuItem("RiksKanal");
        JMenuItem lokalKanal = new JMenuItem("Lokal");
        JMenuItem musikKanal = new JMenuItem("MusikKanal");
        menuBarView.getChannelsTypeMenu().add(riksKanal);
        menuBarView.getChannelsTypeMenu().add(lokalKanal);
        menuBarView.getChannelsTypeMenu().add(musikKanal);
        assertTrue(menuBarView.getChannelsTypeMenu().getItemCount() > 0,"Channel Types menu should have items");
    }


    /**
     * It will check if the update text is visible on the GUI.
     * It will show the user when we are about to update a program of a certain channel.
     */
    @Test
    public void checkIfUpdatingShowsOnGUI() throws Exception {
        // first check if the "update..." text is visible on the GUI.
        // after that shows on the GUI that the channel has been updated.
        menuBarView.getProgramUpdatedLabel().setText("Updating . . . .");
        assertEquals(setScheduleUpdatedLabel(), menuBarView.getProgramUpdatedLabel().getText());

    }

    /**
     * It sets the time on the GUI
     *
     * @return the text which should be updated on the GUI.
     */
    private String  setScheduleUpdatedLabel() {
        LocalDateTime lastUpdatedTime = LocalDateTime.now();
        String formattedTime = "Schedule Updated: " + " << "+ "channel 1" +" >> "+ lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        menuBarView.getProgramUpdatedLabel().setText(formattedTime);
        return formattedTime;
    }


    /**
     * It will show on the GUI that a certain channel has been clicked, and it gets updated.
     */
    @Test
    public void testOnClickChannelShowsOnGUI() {
        JButton button = new JButton("Channel 1");
        button.addActionListener(e->{
            menuBarView.setSelectedChannelLabel(button.getText());
        });
        button.doClick();
        assertEquals("Selected Channel: "+button.getText(), menuBarView.getSelectedChannelLabel().getText());
    }

    @AfterEach
    public void tearUp() {
        if (frame != null) {
            frame.dispose();
        }
        menuBarView = null;
    }

}
