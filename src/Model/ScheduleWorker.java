package Model;
import Controll.Observer;
import Controll.Subject;


import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleWorker extends SwingWorker<ArrayList<Schedule>,Void> implements Subject {
    private ArrayList<Schedule> schedules;
    private ArrayList<Observer> observers;
    private Channel channel;

    public ScheduleWorker(Channel channel){
        this.channel = channel;
        observers = new ArrayList<Observer>();
        this.schedules = new ArrayList<Schedule>();
    }

    @Override
    protected ArrayList<Schedule> doInBackground()  {
        ScheduleParser parser = new ScheduleParser(channel);
        return parser.getScheduleList();
    }

    @Override
    protected void done() {
        try {
            schedules = get();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
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
        for (Observer o : observers) {
           o.scheduleUpdate(channel,schedules);
        }
    }
}
