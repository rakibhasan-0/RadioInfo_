package Model;
import Controll.Observer;
import Controll.Subject;
import javax.swing.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * That class will be inherited from the Swing Worker class, It will perform the
 * long-running task on the background thread.
 * @author Gazi Md Rakibul Hasan
 */
public class XMLParserWorker extends SwingWorker<ArrayList<Channel>,Void> implements Subject {
    private final ArrayList<Observer> observers = new ArrayList<Observer>();
    private  ArrayList <Channel> channels;


    /**
     * It will let the background thread perform the long-running task, in that
     * it will parse the XML file to fetch the channels from the API and return
     * the list of channels.
     * @return the list of channels.
     */
    @Override
    protected ArrayList<Channel> doInBackground()  {
        XMLParser parser = new XMLParser();
        return parser.getChannels();
    }


    /**
     * That method will be called from the EDT, and it will get the result
     * of the background thread, and it categorize the channels with the types.
     */
    @Override
    protected void done() {
        try {
            this.channels = get();
            notifyObservers();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "The operation was interrupted. Please try again.");
        } catch (ExecutionException e) {
            JOptionPane.showMessageDialog(null, "Error: An error occurred during execution.");
        }
    }




    /**
     * It registers the observer that will be notified for a specific event.
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
     * That method will notify all observers when a specific event occurs.
     */
    @Override
    public void notifyObservers() {
        for(Observer o : observers){
            o.channelUpdate(channels);
        }
    }
}