package Model;

import Controll.Observer;
import Controll.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class XMLParserWorker extends SwingWorker<ArrayList<Channel>,Void> implements Subject {
    ArrayList<Observer> observers = new ArrayList<Observer>();
    ArrayList<Channel> channels = new ArrayList<Channel>();
    XMLParser parser;

    public XMLParserWorker(){
        this.parser = new XMLParser();
    }

    @Override
    protected ArrayList<Channel> doInBackground() throws Exception {;
        parser.fetchChannelsData();
        return parser.getChannels();
    }

    @Override
    protected void done() {
        try {
            channels = get();
            notifyObservers();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
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
            o.update(channels);
        }
    }

}