package client.view.events;

public class GeneralMessageEvent {

    public final String message;

    public GeneralMessageEvent(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message + "\n";
    }
}
