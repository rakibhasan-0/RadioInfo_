package Model;
import Controll.Controller;
import Controll.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Gazi Md Rakibul Hasan
 * that class is responsible for fetching channels, channel's program schedules.
 */
public class APIManager implements Observer {
    private final Controller controller;

    public APIManager( Controller controller) {
        this.controller = controller;
    }


    /**
     * That method invoked when channels has fetched from the API.
     * Thereafter, it notifies the Controller with the data.
     * @param channelsType A set of channel types.
     * @param channelsWithtype A map of channels categorized by their types.
     */
    @Override
    public void channelUpdate(HashSet<String> channelsType, HashMap<String, ArrayList<Channel>> channelsWithtype) {
        controller.updatedChannels(channelsType, channelsWithtype);
    }


    /**
     * It gets called whenever the program's schedule is updated. Then it notifies the Controller
     * with the updated schedule data.
     * @param channel The channel whose schedule has been updated.
     * @param schedules The updated list of schedules for the channel.
     */
    @Override
    public void scheduleUpdate(Channel channel, ArrayList<Schedule> schedules) {
        controller.getSchedule(channel, schedules);
    }



    /**
     * Initiates the process of fetching schedule data for a specific channel.
     * It creates and executes a ScheduleWorker to perform the task in the background.
     * @param channel The channel for which the schedule data is to be fetched.
     */
    public void fetchScheduleForChannel(Channel channel) {
        ScheduleWorker scheduleWorker = new ScheduleWorker(channel);
        scheduleWorker.registerObserver(this);
        scheduleWorker.execute();
    }



    /**
     * Initiates the process of fetching channel data from the API.
     * It creates and executes an XMLParserWorker to perform the task in the background.
     */
    public void fetchChannelDataFromAPI() {
        XMLParserWorker xmlWorker = new XMLParserWorker();
        xmlWorker.registerObserver(this);
        xmlWorker.execute();
    }

}
