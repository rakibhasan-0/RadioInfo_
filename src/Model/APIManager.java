package Model;
import Controll.Controller;
import Controll.Observer;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Gazi Md Rakibul Hasan
 * that class is responsible for fetching channels, channel's program schedules.
 */
public class APIManager implements Observer {
    private final Controller controller;

    private ArrayList<Channel> channels;
    private   HashMap <String, ArrayList<Channel>> channelWithCategory = new HashMap<String, ArrayList<Channel>>();

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
        HashSet<String> channelCategory = new HashSet<String>();
        HashMap <String, ArrayList<Channel>> channelWithCategory = new HashMap<String, ArrayList<Channel>>();
        creatingChannelWithCategory(channelWithCategory);
        getTotalCategory(channelCategory);
        this.channelWithCategory = channelWithCategory;
        controller.updatedChannels(channelCategory, channelWithCategory);
    }

    /**
     * for the testing purpose
     */
    public HashMap<String,ArrayList<Channel>> getTheChannelsWithCategory(){
        return channelWithCategory;
    }


    /**
     * It will retrive the total categories that exist.
     * @param channelCategory it is a set where all channels categories will be stored.
     */
    private void getTotalCategory(HashSet<String> channelCategory) {
        for(Channel channel : this.channels) {
            channelCategory.add(channel.getChannelType());
        }
    }



    /**
     * It alligns the channels with its category.
     *
     * @param channelWithCategory a map where the channels and with its corresponding category
     *                            will be stored.
     */
    private void creatingChannelWithCategory(HashMap <String, ArrayList<Channel>> channelWithCategory){
        for (Channel channel : this.channels) {
            ArrayList<Channel> channelList = channelWithCategory.get(channel.getChannelType());
            if (channelList == null) {
                channelList = new ArrayList<>();
            }
            channelList.add(channel);
            channelWithCategory.put(channel.getChannelType(),channelList);
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
        System.out.println("Scheduling update/ we are getting the schedule");
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
