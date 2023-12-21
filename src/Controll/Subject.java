package Controll;

/**
 * That class has been used within the observer pattern. Those classes which
 * implement this interface will be used as observerble. Its update on specific
 * events will get notified to the observers.
 * @author Gazi Md Rakibul Hasan
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
