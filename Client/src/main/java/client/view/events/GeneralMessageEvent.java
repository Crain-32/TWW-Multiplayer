package client.view.events;

public class GeneralMessageEvent {

    public String message;

    public GeneralMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message + "\n";
    }
}
