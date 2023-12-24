package Controll;
import Model.Channel;
import Model.Schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents the obsevers, on a specific event all observers will get notified.
 * In that case, it will get notified when channels and programs schedules for the specific
 * channel get updated/fetched from the API.
 */
public interface Observer {
    void channelUpdate(ArrayList<Channel> channels);
    void scheduleUpdate(Channel channel, ArrayList<Schedule> schedule);
}

