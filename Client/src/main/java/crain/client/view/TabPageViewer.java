package crain.client.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import crain.client.view.pages.CreateRoomPage;
import crain.client.view.pages.JoinRoomPage;
import crain.client.view.pages.SettingsPage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Slf4j
@Component
public class TabPageViewer {
    private JTabbedPane tabSelector;
    @Getter
    private JPanel basePanel;

    private CreateRoomPage createRoomPage;

    @Autowired
    public void setCreateRoomTab(CreateRoomPage createRoomPage) {
        createRoomPage.setListeners();
        if (tabSelector.getTabCount() == 0) {
            tabSelector.addTab("Create Room", createRoomPage.getBasePanel());
        } else {
            tabSelector.insertTab("Create Room", null, createRoomPage.getBasePanel(), null, 0);
        }

    }

    @Autowired
    public void setJoinRoomTab(JoinRoomPage joinRoomPage) {
        joinRoomPage.setListeners();
        if (tabSelector.getTabCount() == 1 || tabSelector.getTabCount() == 0) {
            tabSelector.addTab("Join Room", joinRoomPage.getBasePanel());
        } else {
            tabSelector.insertTab("Join Room", null, joinRoomPage.getBasePanel(), null, 1);
        }
    }

    @Autowired
    public void setSettingsTab(SettingsPage settingsPage) {
        tabSelector.addTab("Settings", settingsPage.getBasePanel());
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        basePanel = new JPanel();
        basePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 10, 0), -1, -1));
        tabSelector = new JTabbedPane();
        basePanel.add(tabSelector, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, -1), new Dimension(400, 200), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePanel;
    }

    private void createUIComponents() {
        // We don't do anything in here since we're setting the panels through Autowired Setters.
    }
}