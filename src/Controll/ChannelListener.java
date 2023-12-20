package Controll;
import Model.Channel;
import Model.Schedule;


/**
 * A listener interface it notifies its implemented class when a certain button gets clicked
 * and a certain channel has been selected.
 * @author Gazi Md Rakibul Hasan
 */

public interface ChannelListener {
    void onChannelSelected(Channel channel);
    void onButtonClick(Schedule schedule);
}
