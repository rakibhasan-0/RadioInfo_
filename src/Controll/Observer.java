package Controll;
import Model.Channel;
import Model.Schedule;

import javax.print.attribute.HashAttributeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface Observer {
    void channelUpdate(HashSet<String> channelType, HashMap<String, ArrayList<Channel>> channelsWithtypes);
    void scheduleUpdate(Channel channel, ArrayList<Schedule> schedule);
}

