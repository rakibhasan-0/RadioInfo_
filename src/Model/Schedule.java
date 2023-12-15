package Model;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Schedule {
    private  String programName = null;
    private String description = null;
    private String startTime = null;
    private String endTime = null;
    private Image image = null;

    public String getProgramName() {
        return programName;
    }

    public String getDescription() {
        return description;
    }


    public String getStartTime() {
        return startTime;
    }

    public  String getEndTime() {
        return endTime;
    }

    public Image getImage() {
        return image;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime( String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime( String endTime) {
        this.endTime = endTime;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    Schedule(){

    }
}
