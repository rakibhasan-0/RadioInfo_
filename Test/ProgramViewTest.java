import View.ProgramView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgramViewTest {
    private ProgramView programView;
    private JFrame f;
    @BeforeEach
    void setUp() {
        this.f = new JFrame();
        this.programView = new ProgramView();
        programView.addCardPanel();
        f.setLayout(new BorderLayout());
        f.add(programView.getProgramScrollPane(), BorderLayout.WEST);
        f.add(programView.getCardPanel(), BorderLayout.EAST);
        f.setSize(300, 400);
        f.setVisible(true);
        f.pack();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @AfterEach
    public void tearUp() {
        if (f != null) {
            f.dispose();
        }
        programView = null;
    }


    /**
     * We add a button on the panel and check if it gets added.
     */
    @Test
    public void testButtonAdditionToChannelPane() throws InterruptedException {
        if (programView != null) {
            JButton testButton = new JButton("Test Button");
            programView.getChannelPane().add(testButton);
            SwingUtilities.invokeLater(() -> programView.getChannelPane().revalidate());
            // Use a latch to wait for UI updates
            CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(() -> {
                boolean isButtonPresent = Arrays.asList(programView.getChannelPane().getComponents()).contains(testButton);
                assertTrue(isButtonPresent, "Test button should be present in the channel pane");
                latch.countDown();
            });
            // Wait for the latch to release (UI update to complete)
            latch.await(2, TimeUnit.SECONDS);
        } else {
            System.out.println("Program view is null");
        }
    }

    /**
     * We check if the panels get switched.
     */
    @Test
    public void testPanelGetSwitched() throws InterruptedException {
        if (programView != null) {
            JButton programListButton = new JButton("Program List");
            JButton programDetailsButton = new JButton("Program Details");
            programView.getProgramScrollPane().add(programListButton);
            programView.getPogramDetailsPanel().add(programDetailsButton);
            programListButton.addActionListener(e -> {
                CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
                layout.show(programView.getCardPanel(), "programsList");
            });

            programDetailsButton.addActionListener(e -> {
                CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
                layout.show(programView.getCardPanel(), "programDetails");
            });

            CountDownLatch latch = new CountDownLatch(2);
            SwingUtilities.invokeLater(() -> {
                programListButton.doClick();
                assertTrue(programView.getPogramDetailsPanel().isShowing(), "Program Details panel should be visible after clicking 'Program List'");
                latch.countDown();
            });

            SwingUtilities.invokeLater(() -> {
                programDetailsButton.doClick();
                assertTrue(programView.getPogramDetailsPanel().isShowing(), "Program List panel should be visible after clicking 'Program Details'");
                latch.countDown();
            });
            latch.await(2, TimeUnit.SECONDS);
        } else {
            System.out.println("Program view is null");
        }
    }
}
