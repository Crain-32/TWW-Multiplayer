package utils;

import crain.client.view.events.GeneralMessageEvent;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.event.EventListener;

@TestComponent
public class GeneralMessageListener {
    @EventListener(GeneralMessageEvent.class)
    public void listenToGeneralMessage(GeneralMessageEvent e) {
    }
}
