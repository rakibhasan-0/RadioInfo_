package Model;

import java.awt.*;
import java.net.URL;

public class Channel {
    private final String channelName;
    private final URL scheduleURL;
    private final Image channelImage;
    private final int id;

    public Channel(String channelName, URL scheduleURL, Image channelImage, int id) {
        this.channelName = channelName;
        this.scheduleURL = scheduleURL;
        this.channelImage = channelImage;
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public URL getScheduleURL() {
        return scheduleURL;
    }

    public Image getChannelImage() {
        return channelImage;
    }

    public int getId() {
        return id;
    }

}