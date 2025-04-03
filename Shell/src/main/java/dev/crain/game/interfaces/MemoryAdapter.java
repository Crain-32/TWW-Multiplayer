package dev.crain.game.interfaces;

import dev.crain.exceptions.HandlerValidationException;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.exceptions.memory.MissingGameAdapterException;
import lombok.extern.slf4j.Slf4j;

/**
 * This Interface should define the protocol between the dev.crain and the Game.
 * Primarily Planning to be Dolphin/Websocket. This gets fed into the
 * ItemCategoryHandler/EventHandler to allow us to isolate logic better than we
 * originally did in Python.
 * <p>
 * Protocol Implementations should handle the mapping from Game Address (0x8000000~) to real Address.
 */
@Slf4j
public abstract class MemoryAdapter {

    /**
     * A call to this function should connect the dev.crain to the Game using the
     * Communication Protocol being implemented.
     * Failure to connect should raise a GameHandlerDisconnectWarning
     */
    public abstract void connect() throws MissingGameAdapterException;

    public abstract Boolean disconnect();


    public abstract Boolean writeByte(Integer consoleAddress, Byte byteVal);

    public abstract Byte readByte(Integer consoleAddress);

    public abstract Boolean writeShort(Integer consoleAddress, Short shortVal);

    public abstract Short readShort(Integer consoleAddress);

    public abstract Boolean writeInteger(Integer consoleAddress, Integer integerVal);

    public abstract Integer readInteger(Integer consoleAddress);

    public abstract Boolean writeString(Integer consoleAddress, String stringVal);

    public abstract String readString(Integer consoleAddress, Integer stringLength);

    public String readStringTillNull(Integer consoleAddress) throws MemoryHandlerException {
        return readStringTillNull(consoleAddress, 0x50);
    }

    public String readStringTillNull(Integer consoleAddress, Integer maxLength) throws MemoryHandlerException {
        if (!isConnected()) {
            throw new HandlerValidationException("No Memory Handler to use");
        }
        try {
            var potentialString = readString(consoleAddress, maxLength);
            var nullIndex = potentialString.indexOf('\0');
            if (nullIndex <= 0 && !potentialString.isEmpty()) {
                return potentialString;
            } else if (nullIndex <= 0) {
                return null;
            }
            return potentialString.substring(0, nullIndex);
        } catch (Exception e) {
            log.error("Failed to search for substring? {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * True if the Connection to the Game is current connected and usable.
     */
    public abstract Boolean isConnected();

}
