import java.util.ArrayList;

public class TestProgram {
    public static void main(String[] args) {
        try {
            XMLParser xmlParser = new XMLParser();
            ArrayList<Channel> channels = xmlParser.getChannels();

            for (Channel channel : channels) {
                System.out.println("Channel: " + channel.getChannelName());
                System.out.println("Schedule URL: " + channel.getScheduleURL());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

