package dev.crain.game.interfaces;

import com.sun.jna.Native;
import dev.crain.exceptions.HandlerValidationException;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.exceptions.memory.MissingGameAdapterException;

import java.util.ArrayList;
import java.util.List;

/**
 * This Interface should define the protocol between the dev.crain and the Game.
 * Primarily Planning to be Dolphin/Websocket. This gets fed into the
 * ItemCategoryHandler/EventHandler to allow us to isolate logic better than we
 * originally did in Python.
 * <p>
 * Protocol Implementations should handle the mapping from Game Address (0x8000000~) to real Address.
 */
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
        return readStringTillNull(consoleAddress, 0x400);
    }

    public String readStringTillNull(Integer consoleAddress, Integer maxLength) throws MemoryHandlerException {
        if (!isConnected()) {
            throw new HandlerValidationException("No Memory Handler to use");
        }
        List<Byte> byteList = new ArrayList<>(maxLength);
        for (int index = 0; index < maxLength; index++) {
            Byte value = readByte(consoleAddress + index);
            if (value == '\0') break;
            byteList.add(value);
        }
        String result;
        if (byteList.isEmpty()) {
            result = "";
        } else {
            var byteArr = new byte[byteList.size()];
            for (int index = 0; index < byteList.size(); index++) {
                byteArr[index] = byteList.get(index);
            }
            result = Native.toString(byteArr);
        }
        return result;
    }

    /**
     * True if the Connection to the Game is current connected and usable.
     */
    public abstract Boolean isConnected();

}
