package crain.client.view.events;

public record GeneralMessageEvent(String message) {

    public String message() {
        return this.message + "\n";
    }
}
