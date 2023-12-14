import Controll.Controller;
import View.ChannelView;
import View.MenuBarView;
import View.ProgramView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class RadioInfo {
    public RadioInfo() {
        JFrame frame = new JFrame("Radio Info");
        MenuBarView menuBarView = new MenuBarView();
        ProgramView programView = new ProgramView();
        JPanel menuPanel = new JPanel();
        menuPanel.add(programView.getScrollChannel());
        menuPanel.add(programView.getProgramScrollPane());
        menuPanel.setLayout(new GridLayout(1, 2));
        menuPanel.add(programView.addCardPanel());
        initializeNorthPanel(frame, menuBarView);
        menuBarView.startClock();
        Controller controller = new Controller(menuBarView,programView);
        frame.setJMenuBar(menuBarView.getMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.add(programView.getScrollChannel(),BorderLayout.WEST);
        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initializeNorthPanel(JFrame frame, MenuBarView menuBarView) {
        JPanel combinedNorthPanel = new JPanel(new BorderLayout());
        combinedNorthPanel.add(menuBarView.getTimePanel(), BorderLayout.NORTH);
        frame.add(combinedNorthPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RadioInfo::new);
    }
}