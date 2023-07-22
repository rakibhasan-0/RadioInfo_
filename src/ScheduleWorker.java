import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class ScheduleWorker extends SwingWorker<HashMap<Channel, List<Schedule>>, Void> {
    private final List<Channel> channels;
    private final Cache cache;

    public ScheduleWorker(List<Channel> channels, Cache cache) {
        this.channels = channels;
        this.cache = cache;
    }

    @Override
    protected HashMap<Channel, List<Schedule>> doInBackground() {
        HashMap<Channel, List<Schedule>> schedulesMap = new HashMap<>();
        for (Channel channel : channels) {
            ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
            List<Schedule> schedules = scheduleParser.fetchSchedules();
            schedulesMap.put(channel, schedules);
        }
        return schedulesMap;
    }

    @Override
    protected void done() {
        try {
            HashMap<Channel, List<Schedule>> fetchedSchedulesMap = get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
