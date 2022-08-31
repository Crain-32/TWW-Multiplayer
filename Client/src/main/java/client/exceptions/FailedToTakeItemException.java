package client.exceptions;

import client.game.data.ItemInfo;

public class FailedToTakeItemException extends RuntimeException {

    private final ItemInfo info;

    public FailedToTakeItemException(String message, ItemInfo info) {
        super(message);
        this.info = info;
    }
    public FailedToTakeItemException(String message) {
        this(message, null);
    }
}
