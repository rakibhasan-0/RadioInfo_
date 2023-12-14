package Model;
import Controll.Observer;
import Controll.Subject;
import javax.swing.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class XMLParserWorker extends SwingWorker<ArrayList<Channel>,Void> implements Subject {
    private final ArrayList<Observer> observers = new ArrayList<Observer>();
    private final HashSet<String> channelCategory = new HashSet<String>();
    private  HashMap <String, ArrayList<Channel>> channelWithCategory;

    @Override
    protected ArrayList<Channel> doInBackground() throws Exception {
        XMLParser parser = new XMLParser();
        channelWithCategory = new HashMap <String, ArrayList<Channel>>();
        return parser.getChannels();
    }

    @Override
    protected void done() {
        try {
            ArrayList <Channel> channels = get();
            getTotalCategory(channels);
            creatingChannelWithCategory(channels);
            //System.out.println("SIZE OF CHANNEL: " + channels.size());
            notifyObservers();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    private void getTotalCategory(ArrayList<Channel> channels) {
        for(Channel channel : channels) {
            if (!channelCategory.contains(channel.getChannelType())){
                System.out.println("----------------");
                System.out.println(channel.getChannelType());
                channelCategory.add(channel.getChannelType());
                channelWithCategory.put(channel.getChannelType(), new ArrayList<>());
            }
        }
    }

    private void creatingChannelWithCategory(ArrayList<Channel> channels) {
        for (Channel channel : channels) {
            ArrayList<Channel> channelList = channelWithCategory.get(channel.getChannelType());
            if (channelList != null) {
                channelList.add(channel);
            }
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers){
            o.channelUpdate(channelCategory,channelWithCategory);
        }
    }
}