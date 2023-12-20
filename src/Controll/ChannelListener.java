package Controll;
import Model.Channel;
import Model.Schedule;

public interface ChannelListener {
    void onChannelSelected(Channel channel);
    void onButtonClick(Schedule schedule);
}