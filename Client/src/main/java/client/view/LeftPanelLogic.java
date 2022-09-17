package client.view;

import client.communication.ServerService;
import client.events.CreateMemoryAdapterEvent;
import client.events.SetConfigEvent;
import client.view.events.GeneralMessageEvent;
import client.view.events.ServerConnectEvent;
import client.view.events.ServerDisconnectEvent;
import client.view.events.SettingsChangeEvent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class LeftPanelLogic {
    private JComboBox<String> gameMode;
    private JPanel basePanel;
    private JButton createGameRoomButton;
    private JTextField serverUrlField;
    private JTextField roomNameInput;
    private JTextField playerNameInput;
    private JTextField maxPlayerInput;
    private JLabel gameTypeLabel;
    private JLabel serverURL;
    private JLabel passwordLabel;
    private JLabel playerNameLabel;
    private JLabel worldAmount;
    private JLabel roomNameLabel;
    private JCheckBox showPassword;
    private JPasswordField passwordInput;
    private JComboBox<String> changeContext;
    private JButton settingsButton;

    private JFrame settingsPage;

    @Value("${multiplayer.server.url}")
    private String serverTarget;
    @Value("${multiplayer.server.port}")
    private String serverPort;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SettingsPage settingsPageBean;

    private boolean connectedToServer = false;


    public void setListeners() {
        serverUrlField.setText(serverTarget + "  (" + serverPort + ")");
        settingsButton.setActionCommand("Settings");
        settingsButton.setFocusable(false);
        handleCreateJoinChange();
        handleMaxWorldIdChange();
        createGameRoomButton.addActionListener(e -> {
            if (!connectedToServer && "Create".equals(e.getActionCommand())) {
                applicationEventPublisher.publishEvent(new ServerService.CreateRoomEvent());
            } else if (!connectedToServer && "Join".equals(e.getActionCommand())) {
                applicationEventPublisher.publishEvent(new ServerService.ConnectToGameRoom());
                applicationEventPublisher.publishEvent(createMemoryHandlerEvent());
            } else if (connectedToServer && "Disconnect".equals(e.getActionCommand())) {
                applicationEventPublisher.publishEvent(new ServerService.DisconnectFromGameRoom());
            } else if (connectedToServer && !StringUtils.equalsIgnoreCase(createGameRoomButton.getText(), "Disconnect")) {
                String output = StringUtils.equals(createGameRoomButton.getText(), "Create") ? "create a room" : "join a room";
                applicationEventPublisher.publishEvent(new GeneralMessageEvent("Cannot " + output + " while in a room!"));
            } else {
                log.debug("Unknown Text on Connection Button: " + createGameRoomButton.getText());
            }
        });


        showPassword.addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                passwordInput.setEchoChar('\u0000');
            } else if (state == ItemEvent.DESELECTED) {
                passwordInput.setEchoChar('*');
            }
        });
        changeContext.addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                handleCreateJoinChange();
            }
        });
        gameMode.addItemListener(e -> {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                handleMaxWorldIdChange();
                WorldType worldType = StringUtils.equalsIgnoreCase(gameMode.getSelectedItem().toString(), "Multiworld") ? WorldType.MULTIWORLD : WorldType.COOP;
                applicationEventPublisher.publishEvent(SetConfigEvent.builder().worldType(worldType).build());
            }
        });
        roomNameInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                applicationEventPublisher.publishEvent(SetConfigEvent.builder().gameRoomName(roomNameInput.getText()).build());
            }
        });

        passwordInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                applicationEventPublisher.publishEvent(SetConfigEvent.builder().password(Arrays.toString(passwordInput.getPassword())).build());
            }
        });
        playerNameInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                applicationEventPublisher.publishEvent(SetConfigEvent.builder().playerName(playerNameInput.getText()).build());
            }
        });
        maxPlayerInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    Integer worldAmount = Integer.valueOf(maxPlayerInput.getText());
                    log.debug(String.valueOf(worldAmount));
                    applicationEventPublisher.publishEvent(
                            SetConfigEvent.builder()
                                    .playerAmount(worldAmount)
                                    .worldAmount(worldAmount)
                                    .worldId(worldAmount)
                                    .build());
                } catch (Exception ex) {
                    return;
                }
            }
        });
        settingsButton.addActionListener(e -> {
            if ("Settings".equals(e.getActionCommand())) {
                toggleEditing(false);
                JFrame settingsFrame = new JFrame();
                settingsFrame.setSize(500, 200);
                settingsFrame.setTitle("Client Settings");
                settingsFrame.add(settingsPageBean.getParentPanel());
                settingsPageBean.setListeners(settingsFrame);
                settingsFrame.setLocationRelativeTo(settingsButton);
                settingsFrame.setVisible(true);
                settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                settingsFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        toggleEditing(true);
                    }
                });
            }
        });
    }

    private void toggleEditing(Boolean enable) {
        settingsButton.setEnabled(enable);
        gameMode.setEnabled(enable);
        changeContext.setEnabled(enable);
        playerNameInput.setEnabled(enable);
        roomNameInput.setEnabled(enable);
        passwordInput.setEnabled(enable);
        createGameRoomButton.setEnabled(enable);
        showPassword.setEnabled(enable);
        if (enable) {
            handleMaxWorldIdChange();
        } else {
            maxPlayerInput.setEnabled(false);
        }
    }

    @Async
    @EventListener(ServerConnectEvent.class)
    public void handleServerConnectEvent() {
        if (!connectedToServer) {
            connectedToServer = true;
            createGameRoomButton.setText("Disconnect");
            createGameRoomButton.setActionCommand("Disconnect");
        }
    }

    @Async
    @EventListener(ServerDisconnectEvent.class)
    public void handlerServerDisconnectEvent() {
        if (connectedToServer) {
            connectedToServer = false;
            handleCreateJoinChange();
        }
    }

    @EventListener
    public void handleSettingsChange(SettingsChangeEvent event) {
        return;
    }

    private void handleCreateJoinChange() {
        if (connectedToServer) {
            return;
        }
        if (Objects.equals(Objects.requireNonNull(changeContext.getSelectedItem()).toString(), "Connect to Room")) {
            createGameRoomButton.setText("Join");
            createGameRoomButton.setActionCommand("Join");
            worldAmount.setText("World Id");
        } else {
            createGameRoomButton.setText("Create");
            createGameRoomButton.setActionCommand("Create");
            worldAmount.setText("World Amount");
        }
    }

    private void handleMaxWorldIdChange() {
        maxPlayerInput.setEnabled(Objects.equals(gameMode.getSelectedItem().toString(), "Multiworld"));
    }

    public JPanel getBasePanel() {
        return this.basePanel;
    }

    private CreateMemoryAdapterEvent createMemoryHandlerEvent() {
        return CreateMemoryAdapterEvent.builder()
                // We're always going to handle it like this.
                .memoryAdapterType(CreateMemoryAdapterEvent.MemoryAdapterType.DOLPHIN)
                .build();
    }

    private void createUIComponents() {
        gameMode = new JComboBox<>(new String[]{"Multiworld", "Coop"});
        changeContext = new JComboBox<>(new String[]{"Connect to Room", "Create Room"});
        gameMode.setSelectedIndex(0);
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
        createUIComponents();
        basePanel = new JPanel();
        basePanel.setLayout(new GridLayoutManager(8, 3, new Insets(10, 10, 10, 0), -1, -1));
        basePanel.setMinimumSize(new Dimension(200, -1));
        basePanel.setPreferredSize(new Dimension(336, 100));
        createGameRoomButton = new JButton();
        createGameRoomButton.setText("Create");
        basePanel.add(createGameRoomButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));
        gameMode.setToolTipText("The Room Type to Create, Coop or Multiworld");
        basePanel.add(gameMode, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        playerNameInput = new JTextField();
        playerNameInput.setToolTipText("The name you want other Players to see.");
        basePanel.add(playerNameInput, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        maxPlayerInput = new JTextField();
        maxPlayerInput.setToolTipText("How many Worlds or Players to allow in the Room.");
        basePanel.add(maxPlayerInput, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(20, -1), null, 0, false));
        roomNameInput = new JTextField();
        roomNameInput.setToolTipText("The Room name other players will connect to.");
        basePanel.add(roomNameInput, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        serverUrlField = new JTextField();
        serverUrlField.setEditable(false);
        serverUrlField.setText("twwmultiplayer.com");
        serverUrlField.setToolTipText("Server Connection Location");
        basePanel.add(serverUrlField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        gameTypeLabel = new JLabel();
        gameTypeLabel.setHorizontalAlignment(4);
        gameTypeLabel.setHorizontalTextPosition(4);
        gameTypeLabel.setText("Game Mode");
        basePanel.add(gameTypeLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        serverURL = new JLabel();
        serverURL.setHorizontalAlignment(4);
        serverURL.setHorizontalTextPosition(4);
        serverURL.setText("Server");
        basePanel.add(serverURL, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        roomNameLabel = new JLabel();
        roomNameLabel.setHorizontalAlignment(4);
        roomNameLabel.setHorizontalTextPosition(4);
        roomNameLabel.setText("Room Name");
        basePanel.add(roomNameLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setHorizontalAlignment(4);
        passwordLabel.setHorizontalTextPosition(4);
        passwordLabel.setText("Password");
        basePanel.add(passwordLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        playerNameLabel = new JLabel();
        playerNameLabel.setHorizontalAlignment(4);
        playerNameLabel.setHorizontalTextPosition(4);
        playerNameLabel.setText("Player Name");
        basePanel.add(playerNameLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        worldAmount = new JLabel();
        worldAmount.setHorizontalAlignment(4);
        worldAmount.setHorizontalTextPosition(4);
        worldAmount.setText("World Amount");
        basePanel.add(worldAmount, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        showPassword = new JCheckBox();
        showPassword.setText("Unmask");
        basePanel.add(showPassword, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        passwordInput = new JPasswordField();
        passwordInput.setEditable(true);
        passwordInput.setToolTipText("Room Password Input");
        basePanel.add(passwordInput, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        basePanel.add(changeContext, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        settingsButton = new JButton();
        settingsButton.setHorizontalAlignment(11);
        settingsButton.setHorizontalTextPosition(11);
        settingsButton.setText("Settings");
        settingsButton.setToolTipText("");
        basePanel.add(settingsButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePanel;
    }

}
