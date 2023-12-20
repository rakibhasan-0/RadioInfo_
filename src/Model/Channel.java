package Model;
import javax.swing.*;

/**
 * That class represents the channel and its associated information such as channel name, id
 * icon, tag line and schudle url.
 */
public class Channel {
    private String channelName;
    private String scheduleURL;
    private int id;
    private String channelType;
    private ImageIcon icon;
    private String tagline;


    /**
     * Getter method to get the channel name
     * @return the channel name.
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Getter method to get the schedule url.
     * @return the schedule url of the channel.
     */
    public String getScheduleURL() {
        return scheduleURL;
    }

    /**
     * Getter method to get the id of the channel
     * @return the id of the channel.
     */
    public int getId() {
        return id;
    }


    /**
     * Getter methdo to get the type of the channel.
     * @return the type of the channel.
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * Getter method to get the channel's icon.
     * @return the icon of the image icon.
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Getter method to get the tag line of the channel
     * @return tag line of the channel.
     */
    public String getTagline() {
        return tagline;
    }


    /**
     * Setter method to set the tag line.
     * @param tagline the tag line of the channel.
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


    /**
     * Setter method to set the channel schedule.
     * @param scheduleURL the URL of the schedule.
     */
    public void setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
    }


    /**
     * Setter method to set the image icon of the channel.
     * @param image the image of the channel.
     */
    public void setChannelImage(ImageIcon image) {
        this.icon = image;
    }

    /**
     * Setter method to set the channel name
     * @param channelName the name of the channel.
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Setter method to set the channel type.
     * @param channelType the type of the channel.
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * Setter method to set the channel's id.
     * @param id the id of the channel.
     */
    public void setId(int id) {
        this.id = id;
    }

    Channel() {
    }
}