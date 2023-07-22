import java.time.LocalDateTime;

public class Schedule {
    private final String programName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;


    public Schedule(String programName, LocalDateTime startTime, LocalDateTime endTime){
        this.programName = programName;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    LocalDateTime getStartTime(){
        return startTime;
    }

    LocalDateTime getEndTime(){
        return endTime;
    }

    String getProgramName(){
        return programName;
    }

}
