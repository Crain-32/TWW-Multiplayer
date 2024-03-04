package dev.crain.exceptions;

import dev.crain.exceptions.memory.MemoryHandlerException;

public class HandlerValidationException extends MemoryHandlerException {

    public HandlerValidationException(String message) {
        super(message);
    }
}
