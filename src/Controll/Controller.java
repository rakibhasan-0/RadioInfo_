package Controll;
import Model.*;
import View.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * It is the controller class it will be responsible to be the bridge between GUI and Modell.
 * @author Gazi Md Rakibul Hasan
 */
public class Controller implements ChannelListener {
    private final MenuBarView menuBarView;
    private final Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;
    private final UIManager uiManager;
    private final APIManager apiManager;


    /**
     * Constructor of the controller class. It takes two GUI classes as parameters and instantiates
     * the cache class and API manager class too.
     * @param menuBarView the menu view which holds information which related to the menu bar.
     * @param programView it holds gui information about the program view and channel view on the gui.
     */
    public Controller(MenuBarView menuBarView, ProgramView programView) {
        this.menuBarView = menuBarView;
        this.uiManager = new UIManager(programView, menuBarView, this);
        this.cache = new Cache();
        apiManager = new APIManager(this);
        apiManager.fetchChannelDataFromAPI();
        setupMenuListeners();
    }


    /**
     * That method is responsible for update the schedules of the selected channel. When the update
     * occurs, it makes sure that it clears the cache for the selected channel, reset timer and fetch
     * the schedule from API
     */
    private void UpdateSchedule() {
        if (selectedChannel != null) {
            System.out.println("Updating schedule from controller"+selectedChannel.getChannelName());
            uiManager.setScheduleIsUpdatingLabel();
            cache.clearCacheForAChannel(selectedChannel);
            apiManager.fetchScheduleForChannel(selectedChannel);
            resetAutomaticUpdates();
        }else {
            JOptionPane.showMessageDialog(null, "Please select a channel first before updating the schedule.");
        }
    }


    /**
     * It updates the channel by fetching data from the API and reset, timer and
     * it shows the user that update is occurring.
     */
    private void updateChannels() {
        uiManager.setChannelUpdatingLabel();
        apiManager.fetchChannelDataFromAPI();
        resetAutomaticUpdates();
    }


    /**
     * That method responisble for updating channels and a selected channel's programs schedules.
     */
    private void setupMenuListeners() {
        menuBarView.addUpdateChannelListener(e -> {
            updateChannels();
        });

        menuBarView.addUpdateScheduleListener(e -> {
            UpdateSchedule();
        });
    }

    /**
     * That method resets the timer responsible for automatic updates.
     * If an existing timer is running, it is stopped before a new update cycle is started.
     */
    private void resetAutomaticUpdates() {
        if (automaticUpdateTimer != null && automaticUpdateTimer.isRunning()) {
            automaticUpdateTimer.stop();
        }
        startAutomaticUpdates();
    }


    /**
     * That method initialises and starts the time for the automatic update
     * channel and program schedules. This update occurs every hour by clearing
     * cache and fetches data from the API.
     */
    private void startAutomaticUpdates() {
        automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
            menuBarView.setCurrentTimeLabel(LocalDateTime.now());
            cache.clearCache();
            apiManager.fetchChannelDataFromAPI();
        });
        automaticUpdateTimer.start();
    }


    /**
     * That method is responsible for adding channels based on its type on the GUI. It clears cache
     * and resets the timer for the automatic update.
     * @param types it represents the categories of the channels.
     * @param channelWithType A map that contains categories and its corresponding channels.
     */
    public void updatedChannels (HashSet<String> types, HashMap<String,ArrayList<Channel>> channelWithType) {
        SwingUtilities.invokeLater(() -> {
            cache.clearCache();
            uiManager.setupChannelButtons(types,channelWithType);
            uiManager.setChannelUpdatedLabel();
            resetAutomaticUpdates();
        });
    }


    /**
     * That method will chache the given channel and its programs schedules.
     * It will be responsible for updating the GUI by showing those channels on UI.
     * @param channel the channel.
     * @param schedules the given channel's programs schedules.
     */
    public void getSchedule (Channel channel, ArrayList<Schedule> schedules) {
        cache.addSchedules(channel, schedules);
        SwingUtilities.invokeLater(() -> {
            selectedChannel  = channel;
            uiManager.updateProgramTable(channel, schedules);
            uiManager.setScheduleUpdatedLabel(channel.getChannelName());
        });
    }


    /**
     * It responsible for updating the GUI by showing the selected channel's programs schedules
     * on the GUI. It tries to utilize the cache. If the information of the given channel is not
     * cached then it will fetch data from the API.
     * @param channel the selected channel.
     */
    @Override
    public void onChannelSelected(Channel channel) {
        // Array list will be concern of the thread safety in that case.
        ArrayList<Schedule> schedules = cache.getSchedules(channel);
        selectedChannel = channel;
        if (schedules != null) {
            SwingUtilities.invokeLater(() -> {
                uiManager.updateProgramTable(channel, schedules);
            });
        }else {
            uiManager.setScheduleIsUpdatingLabel(); // won't you update the uiMangerTable after fetching the data?
            apiManager.fetchScheduleForChannel(selectedChannel); // don't use any updates related task
        }
    }


    /**
     * Whenever a program from a specific channel has been clicked, then it will show more
     * information of the program on the gui.
     * @param schedule the schedule to display.
     */
    @Override
    public void onButtonClick(Schedule schedule) {
        uiManager.showDetailsOfProgram(schedule);
        //uiManager.setScheduleUpdatedLabel();
    }

}