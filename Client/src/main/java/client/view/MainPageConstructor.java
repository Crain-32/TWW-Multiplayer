package client.view;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Component
public class MainPageConstructor {

    private JFrame jFrame;
    private CreateGameRoom createGameRoom;
    private ConfigurableApplicationContext context;

    public MainPageConstructor(CreateGameRoom createGameRoom, ConfigurableApplicationContext context) {
        this.createGameRoom = createGameRoom;
        this.context = context;
        initUI();
    }

    private void initUI() {
        jFrame = new JFrame();

        ClientWrapper clientWrapper = new ClientWrapper();
        MessageWrapper messageDisplay = context.getBean(MessageWrapper.class);
        createGameRoom.setJTextArea(messageDisplay.jTextArea());
        clientWrapper.getLeftPanel().add(createGameRoom.getBasePanel());
        clientWrapper.getRightPanel().add(messageDisplay.getMessagePanel());
        jFrame.add(clientWrapper.getParentPanel());

        jFrame.setSize(600, 600);
        jFrame.setTitle("Testing Wrapper");
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public JFrame getJFrame() {
        return this.jFrame;
    }

}
