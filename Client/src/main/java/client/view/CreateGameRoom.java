package client.view;

import client.dolphin.DolphinCommunicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class CreateGameRoom {
    private JComboBox<String> gameMode;
    private JPanel basePanel;
    private JButton testServerRequestionButton;

    private JTextArea testMessageArea;
    private DolphinCommunicator testClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public CreateGameRoom(DolphinCommunicator testClient) {
        this.testClient = testClient;
        testServerRequestionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
//                    Optional<ROOM.GameRoomRecord> completedData = testClient.getTest().stream().findFirst();
                    System.out.println("GENERAL MESSAGE EVENT CREATED");
//                    applicationEventPublisher.publishEvent(new GeneralMessageEvent(completedData.map(Objects::toString).orElse("Failed to find")));
                });
            }
        });
    }

    public void setJTextArea(JTextArea jTextArea) {
        this.testMessageArea = jTextArea;
    }

    private void createUIComponents() {
        gameMode = new JComboBox<>(new String[]{"Multiworld", "Coop"});
        gameMode.setSelectedIndex(0);
    }

    public JPanel getBasePanel() {
        return this.basePanel;
    }
}
