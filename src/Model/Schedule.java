package Model;

import javax.swing.*;
import java.time.LocalTime;

public class Schedule {
    private final String programName;
    private final String description;
    private final String imageUrl;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private JButton showMoreButton;

    public Schedule(String programName, String description, String imageUrl, LocalTime startTime, LocalTime endTime) {
        this.programName = programName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getProgramName() {
        return programName;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
