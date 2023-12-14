package Controll;

import Model.Schedule;
import View.ProgramView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ProgramDetails{

    ProgramView programView;
    public ProgramDetails(ProgramView programView){
        this.programView = programView;
    }

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
            e.printStackTrace();

        }
    }

    private String formatProgramDetails(Schedule schedule) {
        return "<html>" +
                "<b>Program Name:</b> " + schedule.getProgramName() + "<br><br>" +
                "<b>Start Time:</b> " + schedule.getStartTime() + "<br><br>" +
                "<b>End Time:</b> " + schedule.getEndTime() + "<br><br>" +
                "<b>Description:</b> " + schedule.getDescription() +
                "</html>";
    }


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

    private void displayProgramDetailsWithoutImage(String programDetails) {
        programView.programDetailsPanel().removeAll();
        JLabel label = new JLabel(programDetails);
        JButton okButton = createOkButton();
        programView.programDetailsPanel().add(label, BorderLayout.CENTER);
        programView.programDetailsPanel().add(okButton, BorderLayout.SOUTH);
        showProgramDetailsCard();
    }

    private JButton createOkButton() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
            layout.show(programView.getCardPanel(), "programsList");
        });
        return okButton;
    }

    private void showProgramDetailsCard() {
        CardLayout layout = (CardLayout) programView.getCardPanel().getLayout();
        layout.show(programView.getCardPanel(), "programDetails");
    }

}
