package crain.client.view.events;

public record GeneralMessageEvent(String message) {

    @Override
    public String message() {
        return this.message + "\n";
    }
}
