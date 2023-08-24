import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cache implements Subject {
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

    // Observer pattern methods
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addSchedules(Channel channel, List<Schedule> schedules) {
        cache.put(channel, schedules);
        notifyObservers();
    }

    public List<Schedule> getSchedules(Channel channel) {
        return cache.get(channel);
    }

    public boolean hasChannel(Channel channel) {
        return cache.containsKey(channel);
    }

    public void clearCache() {
        cache.clear();
        notifyObservers();
    }

    public void setSelectedChannel(Channel channel) {
        selectedChannel = channel;
        notifyObservers();
    }

    public Channel getSelectedChannel() {
        return selectedChannel;
    }

    public void clearCacheForAChannel(Channel channel) {
        if (channel != null) {
            cache.remove(channel);
            notifyObservers();
        }
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }
}
