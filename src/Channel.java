import java.awt.*;
import java.net.URL;

public class Channel {
    private final String channelName;
    private final URL scheduleURL;
    private final Image channelImage;


    public Channel(String channelName, URL scheduleURL, Image channelImage){
        this.channelName = channelName;
        this.scheduleURL = scheduleURL;
        this.channelImage = channelImage;
    }

    public String getChannelName() {
        return channelName;
    }

    public URL getScheduleURL(){
        return scheduleURL;
    }

    public Image getChannelImage() {
        return channelImage;
    }
}
