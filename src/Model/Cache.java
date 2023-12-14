package Model;
import java.util.ArrayList;
import java.util.HashMap;

public class Cache {
    private final HashMap<Channel, ArrayList<Schedule>> caches;
    public Cache() {
        this.caches = new HashMap<>();
    }
    public void addSchedules(Channel channel, ArrayList<Schedule> schedules) {
        caches.put(channel, schedules);
    }

    public ArrayList<Schedule> getSchedules(Channel channel) {
        return caches.get(channel);
    }

    public boolean hasChannel(Channel channel) {
        return caches.containsKey(channel);
    }

    public void clearCache() {
        caches.clear();
    }

    public void clearCacheForAChannel(Channel channel) {
        if (channel != null) {
            caches.remove(channel);
        }
    }
}
