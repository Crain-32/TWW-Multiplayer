package crain.client.view;

import crain.client.view.events.ServerDisconnectEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@Component
public class MainPageConstructor {

    private JFrame jFrame;
    private final TabPageViewer tabPageViewer;
    private final ConfigurableApplicationContext context;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MainPageConstructor(TabPageViewer tabPageViewer, ConfigurableApplicationContext context, ApplicationEventPublisher applicationEventPublisher) {
        this.tabPageViewer = tabPageViewer;
        this.context = context;
        this.applicationEventPublisher = applicationEventPublisher;
        initUI();
    }

    private void initUI() {
        jFrame = new JFrame();

        ClientWrapper clientWrapper = new ClientWrapper();
        MessageWrapper messageDisplay = context.getBean(MessageWrapper.class);
        messageDisplay.postConstruct();
        clientWrapper.getLeftPanel().add(tabPageViewer.getBasePanel());
        clientWrapper.getRightPanel().add(messageDisplay.getMessagePanel());
        jFrame.add(clientWrapper.getParentPanel());

        jFrame.setSize(875, 350);
        jFrame.setTitle("The Wind Waker Multiplayer Project");
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                applicationEventPublisher.publishEvent(new ServerDisconnectEvent());
            }
        });
    }

    public JFrame getJFrame() {
        return this.jFrame;
    }

}
