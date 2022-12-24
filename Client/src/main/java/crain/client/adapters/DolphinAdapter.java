package crain.client.adapters;

import Dolphin.DolphinEngine;
import Dolphin.DolphinFactory;
import crain.client.exceptions.MemoryAdapterDisconnectException;
import crain.client.game.interfaces.MemoryAdapter;
import crain.client.view.events.GeneralMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Lazy
@Slf4j
@Component
public class DolphinAdapter implements MemoryAdapter {

    private final DolphinEngine engine;

    private final ApplicationEventPublisher applicationEventPublisher;
    private boolean isHooked;

    public DolphinAdapter(ApplicationEventPublisher applicationEventPublisher) throws IOException {
        this.engine = DolphinFactory.createEngine();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void connect() throws MemoryAdapterDisconnectException {
        engine.hook();
        if (engine.getStatus() == Boolean.FALSE) {
            log.debug("No emulator was found.");
            throw new MemoryAdapterDisconnectException("Failed to Find Dolphin, Please Open Dolphin and Restart the crain.Client.");
        } else if (engine.getStatus() == Boolean.TRUE) {
            log.debug("Hooked to Dolphin\nPublishing Self to Context...");
            isHooked = true;
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Successfully Hooked to Dolphin"));
        }
    }

    @Override
    public Boolean disconnect() {
        return true;
    }

    @Override
    public Boolean isConnected() {
        if (engine.getStatus() == Boolean.FALSE) {
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
