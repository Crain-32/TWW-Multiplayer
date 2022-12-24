package crain.client.exceptions;

import crain.client.game.data.ItemInfo;

public class FailedToTakeItemException extends RuntimeException {

    private final ItemInfo info;

    public FailedToTakeItemException(String message, ItemInfo info, Exception e) {
        super(message, e);
        this.info = info;
    }
    public FailedToTakeItemException(String message, ItemInfo info) {
        this(message, info, null);
    }
    public FailedToTakeItemException(String message) {
        this(message, null, null);
    }

    public ItemInfo getInfo() {
        return info;
    }
}
