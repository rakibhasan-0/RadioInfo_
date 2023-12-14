package Controll;
import Model.*;
import View.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Controller implements ChannelListener, ShowMoreButtonListener {
    private final ChannelView channelView;
    private final ProgramView programView;
    private final MenuBarView menuBarView;
    private final Cache cache;
    private Channel selectedChannel;
    private Timer automaticUpdateTimer;
    private UIManager uiManager;
    private APIManager apiManager;

    public Controller(ChannelView channelView, MenuBarView menuBarView, ProgramView programView) {
        this.channelView = channelView;
        this.menuBarView = menuBarView;
        this.programView = programView;
        this.uiManager = new UIManager(programView, channelView, menuBarView, this, this);
        this.cache = new Cache();
        apiManager = new APIManager(this);
        apiManager.fetchChannelDataFromAPI();
        setupMenuListeners();
    }

    private void UpdateSchedule() {
        if (selectedChannel != null) {
            uiManager.setScheduleIsUpdatingLabel();
            cache.clearCacheForAChannel(selectedChannel);
            apiManager.fetchScheduleForChannel(selectedChannel);
            resetAutomaticUpdates();
        }else {
            JOptionPane.showMessageDialog(null, "Please select a channel first before updating the schedule.");
        }
    }

    private void updateChannels() {
        uiManager.setChannelUpdatingLabel();
        apiManager.fetchChannelDataFromAPI();
        resetAutomaticUpdates();
    }

    private void setupMenuListeners() {
        menuBarView.addUpdateChannelListener(e -> {
            updateChannels();
        });

        menuBarView.addUpdateScheduleListener(e -> {
            System.out.println("get clicked");
            UpdateSchedule();
        });
    }

    private void resetAutomaticUpdates() {
        if (automaticUpdateTimer != null && automaticUpdateTimer.isRunning()) {
            automaticUpdateTimer.stop();
        }
        startAutomaticUpdates();
    }

    private void startAutomaticUpdates() {
        automaticUpdateTimer = new Timer(60 * 60 * 1000, e -> {
            menuBarView.setCurrentTimeLabel(LocalDateTime.now());
            cache.clearCache();
            apiManager.fetchChannelDataFromAPI();
        });
        automaticUpdateTimer.start();
    }

    public void updatedChannels (HashSet<String> types, HashMap<String,ArrayList<Channel>> channelWithType) {
        SwingUtilities.invokeLater(() -> {
            cache.clearCache();
            uiManager.setupChannelButtons(types,channelWithType);
            uiManager.setChannelUpdatedLabel();
            resetAutomaticUpdates();
        });
    }


    public void getSchedule (Channel channel, ArrayList<Schedule> schedules) {
        SwingUtilities.invokeLater(() -> {
            cache.addSchedules(channel, schedules);
            selectedChannel  = channel;
            uiManager.updateProgramTable(channel, schedules);
            uiManager.setScheduleUpdatedLabel(channel.getChannelName());
        });
    }

    // it is just unecessary to make cache as normal; I mean you may not need to use it outside of the controller,
    @Override
    public void onChannelSelected(Channel channel) {
        ArrayList<Schedule> schedules = cache.getSchedules(channel);
        if (schedules != null) {
            uiManager.updateProgramTable(channel, schedules);
            System.out.println("Schedule updated from selected");
        }else {
            uiManager.setScheduleIsUpdatingLabel();
            apiManager.fetchScheduleForChannel(channel);
            System.out.println("Schedule not updated from selected");
        }
    }

    @Override
    public void onButtonClick(Schedule schedule) {
        uiManager.showDetailsOfProgram(schedule);
        //uiManager.setScheduleUpdatedLabel();
    }

}