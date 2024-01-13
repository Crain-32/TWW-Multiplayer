package crain.client.exceptions;

import crain.client.exceptions.memory.MemoryHandlerException;

public class HandlerValidationException extends MemoryHandlerException {

    public HandlerValidationException(String message) {
        super(message);
    }
}
