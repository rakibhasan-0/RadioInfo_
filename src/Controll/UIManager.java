package Controll;
import Model.Channel;
import Model.Schedule;
import View.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UIManager {
    private final ProgramView programView;
    private final ChannelView channelView;
    private final MenuBarView menuBarView;
    private Channel selectedChannel;
    private final ChannelListener channelListener;
    private final ShowMoreButtonListener showMoreButtonListener;
    private final ProgramDetails programDetails;
    private HashMap<String, ArrayList<Channel>> channelsWithTypes;
    private HashSet<String> types;


    public UIManager(ProgramView programView, ChannelView channelView, MenuBarView menuBarView,
                     ChannelListener channelListener, ShowMoreButtonListener showMoreButtonListener) {
        this.programView = programView;
        this.channelView = channelView;
        this.menuBarView = menuBarView;
        this.channelListener = channelListener;
        this.showMoreButtonListener = showMoreButtonListener;
        programDetails = new ProgramDetails(programView);
    }


    // it will create channel types in the menu bar.
    public void addChannelType() {
        JMenu channelTypeMenu = menuBarView.getChannelsTypeMenu();
        for (String types : types){
            JMenuItem channelType = new JMenuItem(types);
            channelTypeMenu.add(channelType);
            channelType.addActionListener(e->displayChannels(types));
        }
    }


    public void displayChannels(String channelName){
        channelView.clearChannelButtons();
        ArrayList<Channel> channels = channelsWithTypes.get(channelName);

        for (Channel channel : channels) {
            JButton button = new JButton(channel.getChannelName());
            button.setIcon(channel.getIcon());
            button.addActionListener(e -> {
                selectedChannel = channel;
                channelListener.onChannelSelected(selectedChannel);
            });
            channelView.addChannelButton(button);
        }
    }


    public void setupChannelButtons(HashSet<String> types, HashMap<String,ArrayList<Channel>>channelsWithTypes) {
        this.types = types;
        this.channelsWithTypes = channelsWithTypes;
        addChannelType();
    }


    public void showDetailsOfProgram(Schedule schedule){
        programDetails.showProgramDetails(schedule);
    }


    public void updateProgramTable(Channel channel, ArrayList<Schedule> schedules) {
        populateProgramTable(schedules);
        menuBarView.setSelectedChannelLabel(channel.getChannelName());
    }



    private void populateProgramTable(ArrayList<Schedule> schedules) {
        String[] columnNames = {"Program Name", "Start Time", "End Time"};
        Object[][] data = new Object[schedules.size()][3];

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            data[i][0] = schedule.getProgramName();
            data[i][1] = schedule.getStartTime();
            data[i][2] = schedule.getEndTime();
        }

        programView.getProgramTable().setModel(new DefaultTableModel(data, columnNames));
        programView.getProgramTable().getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
        programView.getProgramTable().getColumnModel().getColumn(0).setCellEditor(
                new ButtonEditor(schedules, showMoreButtonListener)
        );

        if (schedules.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) programView.getProgramTable().getModel();
            model.setRowCount(0);
            model.setColumnCount(0);
            model.addColumn("There is no schedule for this program");
        }
    }


    public void setChannelUpdatedLabel() {
        LocalDateTime lastUpdatedTime = LocalDateTime.now();
        String formattedTime = "Channels Updated: " + lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        menuBarView.getChannelUpdatedLabel().setText(formattedTime);
    }


    public void setChannelUpdatingLabel(){
        menuBarView.getChannelUpdatedLabel().setText("Updating---");
    }

    public void setScheduleUpdatedLabel(String channelName) {
        LocalDateTime lastUpdatedTime = LocalDateTime.now();
        String formattedTime = "Schedule Updated: " + " << "+ channelName +" >> "+ lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        menuBarView.getProgramUpdatedLabel().setText( formattedTime );
    }


    public void setScheduleIsUpdatingLabel(){
        menuBarView.getProgramUpdatedLabel().setText("Updating----");
    }

}
