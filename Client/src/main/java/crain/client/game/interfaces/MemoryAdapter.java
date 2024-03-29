package crain.client.game.interfaces;

import crain.client.exceptions.memory.MissingGameAdapterException;

/**
 * This Interface should define the protocol between the crain.Client and the Game.
 * Primarily Planning to be Dolphin/Websocket. This gets fed into the
 * ItemCategoryHandler/EventHandler to allow us to isolate logic better than we
 * originally did in Python.
 * <p>
 * Protocol Implementations should handle the mapping from Game Address (0x8000000~) to real Address.
 */
public interface MemoryAdapter {

    /**
     * A call to this function should connect the crain.Client to the Game using the
     * Communication Protocol being implemented.
     * Failure to connect should raise a GameHandlerDisconnectWarning
     */
    void connect() throws MissingGameAdapterException;

    Boolean disconnect();


    Boolean writeByte(Integer consoleAddress, Byte byteVal);

    Byte readByte(Integer consoleAddress);

    Boolean writeShort(Integer consoleAddress, Short shortVal);

    Short readShort(Integer consoleAddress);

    Boolean writeInteger(Integer consoleAddress, Integer integerVal);

    Integer readInteger(Integer consoleAddress);

    Boolean writeString(Integer consoleAddress, String stringVal);

    String readString(Integer consoleAddress, Integer stringLength);

    /**
     * True if the Connection to the Game is current connected and usable.
     */
    Boolean isConnected();

}
