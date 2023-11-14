package Controll;

import Model.Channel;

import java.util.ArrayList;

public interface Observer {
    void update(ArrayList<Channel> channels);
    void channelUpdate(ArrayList<Channel> channels);
}

