package Model;
import Controll.Observer;
import Controll.Subject;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * @author Gazi Md Rakibul Hasan
 * That class inherits from the Swing Worker class. This class will fetch information and
 * the channel's program's specificd information such as start time, end time, image and
 * description from API. To fetch this information, it will use the background
 * threads.
 */
public class ScheduleWorker extends SwingWorker<ArrayList<Schedule>,Void> implements Subject {
    private  ArrayList<Schedule> schedules;
    private final ArrayList<Observer> observers;
    private final Channel channel;


    /**
     * It is the constructor of the ScheduleWorker class. It takes the given channel as argument
     * instansitate the schedules.
     * @param channel the channel.
     */
    public ScheduleWorker(Channel channel){
        this.channel = channel;
        observers = new ArrayList<Observer>();
        this.schedules = new ArrayList<Schedule>();
    }


    /**
     * It performs the long-running tasks in the background thread. In that case, it fetches all
     * programs scheduls of the given channel.
     * @return list of programs and its schedules.
     */
    @Override
    protected ArrayList<Schedule> doInBackground()  {
        ScheduleParser parser = new ScheduleParser(channel);
        return parser.getScheduleList();
    }


    /**
     * It retrieves the data processed in the background thread. After fetching the data,
     * it notifies all registered observers about the update.
     **/
    @Override
    protected void done() {
        try {
            schedules = get();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "The operation was interrupted.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ExecutionException e) {
            JOptionPane.showMessageDialog(null, "An error occurred during the background operation: " + e.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        notifyObservers();
    }


    /**
     * It registers the observer.
     * @param o the observer.
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * It removes the observer from the list of observers.
     * @param o the observer.
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }


    /**
     * It notifies all observers to get notified on a specified event.
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
           o.scheduleUpdate(channel,schedules);
        }
    }
}
