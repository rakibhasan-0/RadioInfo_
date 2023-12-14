package Model;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class Schedule {
    private  String programName = null;
    private String description = null;
    private LocalTime startTime = null;
    private LocalTime endTime = null;
    private Image image = null;

    public String getProgramName() {
        return programName;
    }

    public String getDescription() {
        return description;
    }


    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
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

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    Schedule(){

    }
}
