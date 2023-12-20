package Model;
import java.awt.*;

/**
 * That class represents a specific channel's program information such as the program name,
 * start time, end time, image and description. That class will be used in the builder pattern
 * to build a program's information.
 */
public class Schedule {
    private  String programName = null;
    private String description = null;
    private String startTime = null;
    private String endTime = null;
    private Image image = null;


    /**
     * Getter method to the program name.
     * @return the program name.
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Getter method to the description of the program.
     * @return the description of the program.
     */
    public String getDescription() {
        return description;
    }


    /**
     *  Getter method to the start time of the program
     * @return start time of the program.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Getter method to the end time of the program
     * @return end time of the program.
     */
    public  String getEndTime() {
        return endTime;
    }

    /**
     * Getter method to the image of the program
     * @return image of the program.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Setter method to set the program name.
     * @param programName the name of the program.
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }


    /**
     * Setter method to set the program description.
     * @param description the description of the program.
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Setter method to set the start time of the program.
     * @param startTime the start time of the program.
     */
    public void setStartTime( String startTime) {
        this.startTime = startTime;
    }

    /**
     * Setter method to set the end time of the program.
     * @param endTime the end time of the program.
     */
    public void setEndTime( String endTime) {
        this.endTime = endTime;
    }

    /**
     * Setter method to set the image of the program.
     * @param image the image of the program.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    Schedule(){

    }
}
