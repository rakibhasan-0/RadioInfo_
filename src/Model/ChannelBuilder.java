package Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


/**
 * It builds the channel object step by step by utlizing the builder design pattern.
 */

public class ChannelBuilder {
    private String channelName;
    private String scheduleURL;
    private int channelId;
    private String tagline;
    private ImageIcon imageIcon;
    private String channelType;

    /**
     * Setter for the channel name.
     * @param channelName the name of the channel
     * @return the channel builder.
     */
    public ChannelBuilder setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    /**
     * Setter for the channel's schedule URL.
     * @param scheduleURL the schedule URL.
     * @return the channel builder.
     */
    public ChannelBuilder setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
        return this;
    }


    /**
     * Setter for the channel id
     * @param channelId the id of the channel
     * @return the channel builder.
     */
    public ChannelBuilder setChannelId(int channelId) {
        this.channelId = channelId;
        return this;
    }

    /**
     * setter for tag line of the channel
     * @param tagline tag line of the channel
     * @return the channel builder.
     */
    public ChannelBuilder setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }


    /**
     * Setter for the image url and process the image based on the url
     * @param imageURL the image url of the channel.
     * @return the builder of the channel.
     */
    public ChannelBuilder setImage(String imageURL) {
           Image channelImage =processChannelsImage(imageURL);
           this.imageIcon = new ImageIcon(channelImage);
           return this;
    }


    /**
     * It prcocess creates a fixed image based on the url.
     * @param imageUrl that url of the channel's image.
     * @return the image which has been created based on the url.
     */
    private Image processChannelsImage(String imageUrl) {
        int IMAGE_WIDTH = 50, IMAGE_HEIGHT = 50;
        if (imageUrl == null || imageUrl.isEmpty()) {
            return createPlaceholderImage();
        }
        try {
            URL url = new URL(imageUrl);
            BufferedImage originalImage = ImageIO.read(url);
            return originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            return createPlaceholderImage();
        }
    }

    /**
     * Setter fot the channel's type.
     * @param channelType it represents the channel type.
     * @return channel builder.
     */
    public ChannelBuilder setChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    /**
     * It creates custom iamge for the channel if the channel has no image url to fetch image from.
     * @return the created image.
     */
    private Image createPlaceholderImage() {
        int IMAGE_WIDTH = 50, IMAGE_HEIGHT = 50;
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.dispose();
        return image;
    }


    /**
     * it builds the channel step by step.
     * @return the channel.
     */
    public Channel build(){
        Channel channel = new Channel();
        channel.setChannelType(channelType);
        channel.setId(channelId);
        channel.setChannelImage(imageIcon);
        channel.setChannelName(channelName);
        channel.setTagline(tagline);
        channel.setScheduleURL(scheduleURL);
        return channel;
    }

}
