import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleWorker extends SwingWorker<List<Schedule>, Void> {
    private final List<Channel> channels;
    private final Cache cache;

    public ScheduleWorker(List<Channel> channels, Cache cache) {
        this.channels = channels;
        this.cache = cache;
    }


    @Override
    protected List<Schedule> doInBackground() throws Exception {
        List<Schedule> allSchedules = new ArrayList<>();
        for (Channel channel : channels) {
            ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
            List<Schedule> schedules = scheduleParser.fetchSchedules();
            allSchedules.addAll(schedules);
        }
        return allSchedules;
    }


    @Override
    protected void done() {
        try {
            List<Schedule> fetchedSchedules = get();
            // Notify any observers if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
