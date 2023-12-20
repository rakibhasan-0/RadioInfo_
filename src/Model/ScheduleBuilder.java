package Model;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


/**
 * That class's task is to build Schedule for the specified program. It will use the builder
 * pattern to build the schedule of the program step by step.
 */
public class ScheduleBuilder {
    private String programName;
    private String description;
    private  String startTime;
    private String endTime;
    private Image image;


    public ScheduleBuilder(){
    }

    /**
     * It sets the program name.
     * @param programName the name of the program.
     * @return the builder.
     */
    public ScheduleBuilder setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    /**
     * It sets the description of the program
     * @param description the description of the program
     * @return builder.
     */
    public ScheduleBuilder setDescription(String description) {
        this.description = description;
        return this;
    }


    /**
     * It sets the start time of the program.
     * @param startTime the start time of the program.
     * @return the builder.
     */
    public ScheduleBuilder setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }


    /**
     * It sets the end time of the program.
     * @param endTime the end time of the program.
     * @return builder.
     */
    public ScheduleBuilder setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }


    /**
     * It sets the image of the progarm.
     * @param image the image of the program.
     * @return builder.
     */
    public ScheduleBuilder setImage(String image) {
        this.image = processChannelsImage(image);
        return this;
    }


    /**
     * It processes the image in such a way that it gets fixed sized. It uses the url
     * which refers to the image of the program. From that url we will create instance
     * of the image.
     * @param imageUrl the url of the image.
     * @return instance of the image.
     */
    private Image processChannelsImage(String imageUrl) {
        int IMAGE_WIDTH = 320, IMAGE_HEIGHT = 420;
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            return image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            // try to find the right expetion
            JOptionPane.showMessageDialog(null, "Couldn't find image");
            return null;
        }
    }


    /**
     * It is build the Scedule of the program's step by step.
     * @return the Scedule.
     */
    public Schedule build(){
        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setDescription(description);
        schedule.setProgramName(programName);
        schedule.setImage(image);
        return schedule;
    }

}
