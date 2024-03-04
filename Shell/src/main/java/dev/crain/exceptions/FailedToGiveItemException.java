package dev.crain.exceptions;

import dev.crain.game.data.ItemInfo;

public class FailedToGiveItemException extends RuntimeException {

    private final ItemInfo attemptedToGive;

    public FailedToGiveItemException(String message, ItemInfo attemptedToGive, Throwable cause) {
        super(message, cause);
        this.attemptedToGive = attemptedToGive;
    }
    public FailedToGiveItemException(String message, ItemInfo attemptedToGive) {
        this(message, attemptedToGive, null);
    }

    public FailedToGiveItemException(String message) {
        this(message, null, null);
    }

    public ItemInfo getAttemptedToGive() {
        return attemptedToGive;
    }
}
