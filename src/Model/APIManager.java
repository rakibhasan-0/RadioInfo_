package Model;
import Controll.Controller;
import Controll.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class APIManager implements Observer {
    private Controller controller;

    public APIManager( Controller controller) {
        this.controller = controller;
    }

    @Override
    public void channelUpdate(HashSet<String> channelsType, HashMap<String, ArrayList<Channel>> channelsWithtype) {
        controller.updatedChannels(channelsType, channelsWithtype);
    }


    @Override
    public void scheduleUpdate(Channel channel, ArrayList<Schedule> schedules) {
        controller.getSchedule(channel, schedules);
    }

    public void fetchScheduleForChannel(Channel channel) {
        ScheduleWorker scheduleWorker = new ScheduleWorker(channel);
        scheduleWorker.registerObserver(this);
        scheduleWorker.execute();
    }

    public void fetchChannelDataFromAPI() {
        XMLParserWorker xmlWorker = new XMLParserWorker();
        xmlWorker.registerObserver(this);
        xmlWorker.execute();
    }

}
