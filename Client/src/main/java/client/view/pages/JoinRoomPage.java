package client.view.pages;

import client.communication.ServerService;
import client.events.CreateMemoryAdapterEvent;
import client.events.SetConfigEvent;
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class JoinRoomPage {
    private JComboBox<String> gameMode;
    private JPanel basePanel;
    private JButton joinRoomButton;
    private JTextField serverUrlField;
    private JTextField roomNameInput;
    private JTextField playerNameInput;
    private JTextField worldIdInput;
    private JLabel gameTypeLabel;
    private JLabel serverURL;
    private JLabel passwordLabel;
    private JLabel playerNameLabel;
    private JLabel worldId;
    private JLabel roomNameLabel;
    private JCheckBox showPassword;
    private JPasswordField passwordInput;


    @Value("${multiplayer.server.url}")
    private String serverTarget;
    @Value("${multiplayer.server.port}")
    private String serverPort;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private boolean connectedToServer = false;


    public void setListeners() {
        serverUrlField.setText(serverTarget + "  (" + serverPort + ")");
        handleMaxWorldIdChange();
        joinRoomButton.addActionListener(e -> {
            if (!connectedToServer && "Join".equals(e.getActionCommand())) {
                applicationEventPublisher.publishEvent(new ServerService.ConnectToGameRoom());
                applicationEventPublisher.publishEvent(createMemoryHandlerEvent());
            } else {
                log.debug("Unknown Text on Connection Button: {}", joinRoomButton.getText());
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
        worldIdInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                try {
                    Integer worldAmount = Integer.valueOf(worldIdInput.getText());
                    log.debug(String.valueOf(worldAmount));
                    applicationEventPublisher.publishEvent(
                            SetConfigEvent.builder()
                                    .playerAmount(worldAmount)
                                    .worldAmount(worldAmount)
                                    .worldId(worldAmount)
                                    .build());
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Async
    @EventListener(ServerConnectEvent.class)
    public void handleServerConnectEvent() {
        if (!connectedToServer) {
            connectedToServer = true;
            joinRoomButton.setText("Disconnect");
            joinRoomButton.setActionCommand("Disconnect");
        }
    }

    @Async
    @EventListener(ServerDisconnectEvent.class)
    public void handlerServerDisconnectEvent() {
        if (connectedToServer) {
            connectedToServer = false;
        }
    }

    @EventListener
    public void handleSettingsChange(SettingsChangeEvent event) {
    }


    private void handleMaxWorldIdChange() {
        worldIdInput.setEnabled(Objects.equals(gameMode.getSelectedItem().toString(), "Multiworld"));
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
        basePanel.setLayout(new GridLayoutManager(7, 3, new Insets(10, 10, 10, 0), -1, -1));
        basePanel.setMinimumSize(new Dimension(200, -1));
        basePanel.setPreferredSize(new Dimension(336, 100));
        joinRoomButton = new JButton();
        joinRoomButton.setActionCommand("Join");
        joinRoomButton.setText("Join");
        basePanel.add(joinRoomButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));
        basePanel.add(gameMode, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        playerNameInput = new JTextField();
        basePanel.add(playerNameInput, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        worldIdInput = new JTextField();
        basePanel.add(worldIdInput, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(30, -1), null, 0, false));
        roomNameInput = new JTextField();
        basePanel.add(roomNameInput, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        serverUrlField = new JTextField();
        serverUrlField.setEditable(false);
        serverUrlField.setText("twwmultiplayer.com");
        basePanel.add(serverUrlField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(150, -1), null, 0, false));
        gameTypeLabel = new JLabel();
        gameTypeLabel.setHorizontalAlignment(4);
        gameTypeLabel.setHorizontalTextPosition(4);
        gameTypeLabel.setText("Game Mode");
        basePanel.add(gameTypeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        serverURL = new JLabel();
        serverURL.setHorizontalAlignment(4);
        serverURL.setHorizontalTextPosition(4);
        serverURL.setText("Server");
        basePanel.add(serverURL, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        roomNameLabel = new JLabel();
        roomNameLabel.setHorizontalAlignment(4);
        roomNameLabel.setHorizontalTextPosition(4);
        roomNameLabel.setText("Room Name");
        basePanel.add(roomNameLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setHorizontalAlignment(4);
        passwordLabel.setHorizontalTextPosition(4);
        passwordLabel.setText("Password");
        basePanel.add(passwordLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 10), null, 0, false));
        playerNameLabel = new JLabel();
        playerNameLabel.setHorizontalAlignment(4);
        playerNameLabel.setHorizontalTextPosition(4);
        playerNameLabel.setText("Player Name");
        basePanel.add(playerNameLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        worldId = new JLabel();
        worldId.setHorizontalAlignment(4);
        worldId.setHorizontalTextPosition(4);
        worldId.setText("World Id");
        basePanel.add(worldId, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, 1, null, new Dimension(96, 16), null, 0, false));
        showPassword = new JCheckBox();
        showPassword.setText("Unmask");
        basePanel.add(showPassword, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        passwordInput = new JPasswordField();
        passwordInput.setEditable(true);
        basePanel.add(passwordInput, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePanel;
    }

}
