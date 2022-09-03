package client.view;

import client.dolphin.DolphinHandler;
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
    private JTextField ServerUrlInput;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;

    private JTextArea testMessageArea;
    private DolphinHandler testClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public CreateGameRoom(DolphinHandler testClient) {
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
