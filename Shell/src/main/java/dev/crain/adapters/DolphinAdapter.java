package dev.crain.adapters;

import dev.crain.exceptions.memory.MissingGameAdapterException;
import dev.crain.game.interfaces.MemoryAdapter;
import dolphin.DolphinEngine;
import dolphin.DolphinFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Lazy
@Slf4j
@Component
public class DolphinAdapter extends MemoryAdapter {

    private final DolphinEngine engine;
    private boolean isHooked;

    public DolphinAdapter() throws IOException {
        this.engine = DolphinFactory.createEngine();
    }

    @Override
    public void connect() throws MissingGameAdapterException {
        engine.hook(); // Integer == int -> Boxed refA != refB
        if (Boolean.FALSE.equals(engine.getStatus())) {
            log.debug("No Emulator was found.");
            throw new MissingGameAdapterException("Failed to Find Dolphin, Please Open Dolphin and Restart the Client.");
        } else if (Boolean.TRUE.equals(engine.getStatus())) {
            log.debug("Hooked to Dolphin\nPublishing Self to Context...");
            isHooked = true;
        }
    }

    @Override
    public Boolean disconnect() {
        return true;
    }

    @Override
    public Boolean isConnected() {
        if (Boolean.FALSE.equals(engine.getStatus())) {
            engine.hook();
        }
        isHooked = engine.getStatus();
        return isHooked;
    }

    @Override
    public Boolean writeByte(Integer consoleAddress, Byte byteVal) {
        return engine.writeByteToRAM(consoleAddress.longValue(), byteVal);
    }

    @Override
    public Byte readByte(Integer consoleAddress) {
        return engine.readByteFromRAM(consoleAddress.longValue());
    }

    @Override
    public Boolean writeShort(Integer consoleAddress, Short shortVal) {
        return engine.writeShortToRAM(consoleAddress.longValue(), shortVal);
    }

    @Override
    public Short readShort(Integer consoleAddress) {
        return engine.readShortFromRAM(consoleAddress.longValue());
    }

    @Override
    public Boolean writeInteger(Integer consoleAddress, Integer integerVal) {
        return engine.writeIntegerToRAM(consoleAddress.longValue(), integerVal);
    }

    @Override
    public Integer readInteger(Integer consoleAddress) {
        return engine.readIntegerFromRAM(consoleAddress.longValue());
    }

    @Override
    public Boolean writeString(Integer consoleAddress, String stringVal) {
        return engine.writeStringToRAM(consoleAddress.longValue(), stringVal);
    }

    @Override
    public String readString(Integer consoleAddress, Integer stringLength) {
        return engine.readStringFromRAM(consoleAddress.longValue(), stringLength);
    }
}
