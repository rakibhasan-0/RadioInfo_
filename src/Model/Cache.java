package Model;
import Controll.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cache {
    private static volatile Cache instance;
    private final HashMap<Channel, List<Schedule>> cache = new HashMap<>();
    private Channel selectedChannel;
    private final List<Observer> observers = new ArrayList<>();

    private Cache() {
    }

    public static Cache getInstance() {
        if (instance == null) {
            synchronized (Cache.class) {
                if (instance == null) {
                    instance = new Cache();
                }
            }
        }
        return instance;
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

    public void clearCacheForAChannel(Channel channel) {
        if (channel != null) {
            cache.remove(channel);
        }
    }
}
