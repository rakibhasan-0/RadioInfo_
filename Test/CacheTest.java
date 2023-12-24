import Controll.Controller;
import Model.Channel;
import Model.ChannelBuilder;
import Model.Schedule;
import Model.ScheduleParser;
import View.MenuBarView;
import View.ProgramView;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheTest {

    @Test
    public void checkIfCacheHasAdded(){
        Controller controller = new Controller(new MenuBarView(), new ProgramView());
        ArrayList<Channel> channels = controller.getApiManager().getChannels();
        assertTrue(controller.getCache().isEmpty());
    }


    @Test
    public void checkCacheHasContains() throws InterruptedException {
        Controller controller = new Controller(new MenuBarView(), new ProgramView());
        Channel channel = new ChannelBuilder()
                .setScheduleURL("http://api.sr.se/v2/scheduledepisodes?channelid=132")
                .setChannelName("P1")
                .setChannelId(132)
                .setChannelType("Rikskanal")
                .build();
        assertTrue(controller.getCache().isEmpty());
        ScheduleParser parser = new ScheduleParser(channel);
        ArrayList<Schedule> schedules = parser.getScheduleList();
        controller.getSchedule(channel, schedules);
        assertFalse(controller.getCache().isEmpty());
    }

}


