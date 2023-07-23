import java.util.HashMap;
import java.util.List;

public class Cache {
    private final HashMap<Channel, List<Schedule>> cache = new HashMap<>();
    private Channel selectedChannel;
    private long lastUpdatedTime;

    public Cache() {
    }

    public void addSchedules(Channel channel, List<Schedule> schedules) {
        cache.put(channel, schedules);
    }

    public List<Schedule> getSchedules(Channel channel) {
        return cache.get(channel);
    }

    public boolean hasChannel(Channel channel) {
        return cache.containsKey(channel);
    }

    public void clearCache() {
        cache.clear();
    }

    public void setSelectedChannel(Channel channel) {
        selectedChannel = channel;
    }

    public Channel getSelectedChannel() {
        return selectedChannel;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }
}
