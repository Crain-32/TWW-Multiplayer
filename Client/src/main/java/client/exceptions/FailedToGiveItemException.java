package client.exceptions;

import client.game.data.ItemInfo;

public class FailedToGiveItemException extends RuntimeException{

    private final ItemInfo attemptedToGive;
    public FailedToGiveItemException(String message, ItemInfo attemptedToGive) {
        super(message);
        this.attemptedToGive = attemptedToGive;
    }

    public FailedToGiveItemException(String message) {
        super(message);
        this.attemptedToGive = null;
    }
}
