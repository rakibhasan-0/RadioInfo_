import javax.swing.*;
import java.util.List;

public class Controller {
    private ChannelView channelView;
    private ProgramView programView;
    private Cache cache;

    public Controller(ChannelView channelView, ProgramView programView, List<Channel> channels) {
        this.channelView = channelView;
        this.programView = programView;
        this.cache = new Cache();

        for (Channel channel : channels) {
            JButton button = new JButton(channel.getChannelName());
            button.setIcon(new ImageIcon(channel.getChannelImage()));
            button.addActionListener(e -> {
                ScheduleParser scheduleParser = new ScheduleParser(channel, cache);
                List<Schedule> schedules = scheduleParser.fetchSchedules();
                programView.populateProgramTable(schedules);
            });
            channelView.addChannelButton(button);
        }
    }
}
