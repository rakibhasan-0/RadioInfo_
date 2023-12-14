package Model;
import javax.swing.*;

public class Channel {
    private String channelName;
    private String scheduleURL;
    private int id;
    private String channelType;
    private ImageIcon icon;
    private String tagline;


    public String getChannelName() {
        return channelName;
    }

    public String getScheduleURL() {
        return scheduleURL;
    }


    public int getId() {
        return id;
    }

    public String getChannelType() {
        return channelType;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }



    public void setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
    }


    public void setChannelImage(ImageIcon image) {
        this.icon = image;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public void setId(int id) {
        this.id = id;
    }

    Channel() {
    }
}