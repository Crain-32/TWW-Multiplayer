package client.view.events;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Map;

public class TestEvent extends AWTEvent {
    @Getter
    @Setter
    private Map<String, String> data;

    public TestEvent(Object source, int id) {
        super(source, id);
    }
}
