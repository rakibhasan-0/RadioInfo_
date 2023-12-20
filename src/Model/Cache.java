package Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * That class used to cache the information of the channel and its corresponding program's
 * schedules.
 * @author Gazi Md Rakibul Hasan
 **/

public class Cache {
    private final Map<Channel, ArrayList<Schedule>> caches;

    /**
     * The constructor of the Cache class, we will use the synchronized hash map in order to
     * prevent multithread problems.
     */
    public Cache() {
        this.caches = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * It adds the programs schedules corresponding to the given channel.
     * @param channel the channel.
     * @param schedules list of schedules to the given channel.
     */
    public void addSchedules(Channel channel, ArrayList<Schedule> schedules) {
        caches.put(channel, schedules);
    }

    /**
     * It get the schedule for the given channel.
     * @param channel the channel
     * @return list of programs schedules to the given channel.
     */
    public ArrayList<Schedule> getSchedules(Channel channel) {
        return caches.get(channel);
    }

    /**
     * If checks if the given channel's list of programs schedules mapped
     * in the hash map or what?
     * @param channel the given channel.
     * @return true if the given channel's program schedules mapped in the hash map,
     * false otherwise.
     */
    private boolean hasChannel(Channel channel) {
        return caches.containsKey(channel);
    }

    /**
     * It clears the hash map that contains channels and its programs schedules.
     */
    public void clearCache() {
        caches.clear();
    }


    /**
     * It clears the specified channel's programs schedules from the hash map.
     * @param channel the given channel.
     */
    public void clearCacheForAChannel(Channel channel) {
        if (channel != null && hasChannel(channel) ) {
            caches.remove(channel);
        }
    }
}
