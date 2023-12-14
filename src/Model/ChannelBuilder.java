package Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChannelBuilder {
    private String channelName;
    private String scheduleURL;
    private int channelId;
    private String tagline;
    private ImageIcon imageIcon;
    private String channelType;

    public ChannelBuilder setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public ChannelBuilder setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
        return this;
    }

    public ChannelBuilder setChannelId(int channelId) {
        this.channelId = channelId;
        return this;
    }

    public ChannelBuilder setTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public ChannelBuilder setImage(String imageURL) {
           Image channelImage =processChannelsImage(imageURL);
           this.imageIcon = new ImageIcon(channelImage);
           return this;
    }

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


    private Image createPlaceholderImage() {
        int IMAGE_WIDTH = 50, IMAGE_HEIGHT = 50;
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.dispose();
        return image;
    }

    public ChannelBuilder setChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }


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
