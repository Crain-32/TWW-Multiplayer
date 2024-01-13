package crain.client.view.events;

import records.INFO;

public record GeneralMessageEvent(String message) {

    public String message() {
        return this.message + "\n";
    }

}
