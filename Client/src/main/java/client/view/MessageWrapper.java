package client.view;


import client.view.events.GeneralMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class MessageWrapper {
    private JTextArea mainTextArea;
    private JButton clearText;
    private JPanel messagePanel;
    private JButton button1;

    private int amount;

    public MessageWrapper() {
        clearText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainTextArea.setText("");
                super.mouseClicked(e);
            }
        });

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainTextArea.insert("Test: " + amount + "\n", 0);
                amount += 1;
            }
        });

    }

    @Async
    @EventListener
    public void onGeneralMessageEvent(GeneralMessageEvent e) {
        System.out.println("GENERAL MESSAGE EVENT RECEIVED");
        System.out.println(e.getMessage());
        SwingUtilities.invokeLater(() -> {
                    System.out.println("SWING HANDLING?");
                    System.out.println(mainTextArea.toString());
                    this.jTextArea().insert(e.getMessage(), 0);
                }
        );
    }

    public JPanel getMessagePanel() {
        return this.messagePanel;
    }

    public JTextArea jTextArea() {
        return this.mainTextArea;
    }

}
