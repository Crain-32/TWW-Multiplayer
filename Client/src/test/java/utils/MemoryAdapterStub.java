package utils;

import crain.client.exceptions.memory.MemoryAdapterDisconnectException;
import crain.client.exceptions.memory.MissingGameAdapterException;
import crain.client.game.interfaces.MemoryAdapter;

public class MemoryAdapterStub implements MemoryAdapter {
    @Override
    public void connect() throws MissingGameAdapterException {
    }

    @Override
    public Boolean disconnect() {
        return null;
    }

    @Override
    public Boolean writeByte(Integer consoleAddress, Byte byteVal) {
        return null;
    }

    @Override
    public Byte readByte(Integer consoleAddress) {
        return null;
    }

    @Override
    public Boolean writeShort(Integer consoleAddress, Short shortVal) {
        return null;
    }

    @Override
    public Short readShort(Integer consoleAddress) {
        return null;
    }

    @Override
    public Boolean writeInteger(Integer consoleAddress, Integer integerVal) {
        return null;
    }

    @Override
    public Integer readInteger(Integer consoleAddress) {
        return null;
    }

    @Override
    public Boolean writeString(Integer consoleAddress, String stringVal) {
        return null;
    }

    @Override
    public String readString(Integer consoleAddress, Integer stringLength) {
        return null;
    }

    @Override
    public Boolean isConnected() {
        return null;
    }
}
