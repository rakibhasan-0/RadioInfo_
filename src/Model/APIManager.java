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
    private final HashSet<String> channelCategory = new HashSet<String>();
    private  final HashMap <String, ArrayList<Channel>> channelWithCategory = new HashMap <String, ArrayList<Channel>>();
    private ArrayList<Channel> channels; // for the testing purposes

    public APIManager( Controller controller) {
        this.controller = controller;
    }

    /**
     * for the testing purposes
     */
    public ArrayList<Channel> getChannels() {
        return channels;
    }

    /**
     * That method invoked when channels has fetched from the API.
     * Thereafter, it notifies the Controller with the data.
     * @param channels The list of channels.
     */
    @Override
    public void channelUpdate(ArrayList<Channel> channels) {
        this.channels = channels;
        creatingChannelWithCategory(channels);
        getTotalCategory(channels);
        controller.updatedChannels(channelCategory,  channelWithCategory);
    }



    /**
     * It will retrive the total categories that exist.
     * @param channels the list of channels.
     */
    private void getTotalCategory(ArrayList<Channel> channels) {
        for(Channel channel : channels) {
            if (!channelCategory.contains(channel.getChannelType())){
                //System.out.println("----------------");
                //System.out.println(channel.getChannelType());
                channelCategory.add(channel.getChannelType());
                channelWithCategory.put(channel.getChannelType(), new ArrayList<>());
            }
        }
    }



    /**
     * It alligns the channels with its category.
     * @param channels list of channels.
     */
    private void creatingChannelWithCategory(ArrayList<Channel> channels) {
        for (Channel channel : channels) {
            ArrayList<Channel> channelList = channelWithCategory.get(channel.getChannelType());
            if (channelList != null) {
                channelList.add(channel);
            }
        }
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
