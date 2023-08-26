package utils;

import crain.client.view.events.GeneralMessageEvent;
import lombok.Getter;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

@TestComponent
public class GeneralMessageListener {

    @Getter
    List<GeneralMessageEvent> generalMessageEventList = new ArrayList<>();
    @EventListener(GeneralMessageEvent.class)
    public void listenToGeneralMessage(GeneralMessageEvent e) {
        generalMessageEventList.add(e);
    }
}
