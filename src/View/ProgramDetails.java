package View;
// it should be on View
import Model.Schedule;
import View.ProgramView;
import javax.swing.*;
import java.awt.*;

/**
 * That class is responsible for showing more details information about a program's schedule
 * on the GUI.
 */
public class ProgramDetails{

    ProgramView programView;

    public ProgramDetails(ProgramView programView) {
        this.programView = programView;
    }


    /**
     * That method is responsible for showing more information about a program's schedules details
     * information on the GUI.
     * @param schedule the program's schedule.
     */
    public void showProgramDetails (Schedule schedule) {
        if (schedule == null) {
            JOptionPane.showMessageDialog(null, "No schedule details available.");
            return;
        }

        String programDetails = formatProgramDetails(schedule);

        try {
            if (schedule.getImage() != null) {
                displayProgramDetailsWithImage(programDetails,schedule.getImage());
            }else {
                displayProgramDetailsWithoutImage(programDetails);
            }
        } catch (Exception e) {
            e.printStackTrace(); // TODO: no stack trace.

        }
    }

    /**
     * Basically, that method gets all information of the channel and converts them to a string.
     * @param schedule the schedule
     * @return a string that contains the information of the program's schedules.
     */
    private String formatProgramDetails(Schedule schedule) {
        return "<html>" +
                "<b>Program Name:</b> " + schedule.getProgramName() + "<br><br>" +
                "<b>Start Time:</b> " + schedule.getStartTime() + "<br><br>" +
                "<b>End Time:</b> " + schedule.getEndTime() + "<br><br>" +
                "<b>Description:</b> " + schedule.getDescription() +
                "</html>";
    }


    /**
     * It displays the program's schedule information such as stat time, end time,
     * image on the gui. It utilizes the card layout in order to display the information.
     *
     * @param programDetails the program details of the program
     * @param image image of the program.
     */

    private void displayProgramDetailsWithImage(String programDetails, Image image) {
        programView.programDetailsPanel().removeAll();
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel label = new JLabel();
        label.setText(programDetails);
        label.setIcon(imageIcon);
        JButton okButton = createOkButton();
        programView.programDetailsPanel().add(label, BorderLayout.CENTER);
        programView.programDetailsPanel().add(okButton, BorderLayout.SOUTH);
        showProgramDetailsCard();
    }

    /**
     * It displays the program's schedule information such as stat time, end time,
     * on the gui. It utilizes the card layout in order to display the information.
     * That method is used when a program has no image.
     *
     * @param programDetails the program details of the program
     */
    private void displayProgramDetailsWithoutImage(String programDetails) {
        programView.programDetailsPanel().removeAll();
        JLabel label = new JLabel(programDetails);
        JButton okButton = createOkButton();
        programView.programDetailsPanel().add(label, BorderLayout.CENTER);
        programView.programDetailsPanel().add(okButton, BorderLayout.SOUTH);
        showProgramDetailsCard();
    }


    /**
     * It creates a button, and whenever the button is clicked, it switches the panel by
     * using the card layout.
     * @return button.
     */
    private JButton createOkButton() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
            layout.show(programView.getCardPanel(), "programsList");
        });
        return okButton;
    }


    /**
     * It uses the card layout to switch panel components on the GUI. It shows a program's
     * schedule's details information.
     */
    private void showProgramDetailsCard() {
        CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
        layout.show(programView.getCardPanel(), "programDetails");
    }

}
