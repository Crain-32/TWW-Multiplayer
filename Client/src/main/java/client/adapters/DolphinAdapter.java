package client.adapters;

import Dolphin.DolphinEngine;
import Dolphin.DolphinFactory;
import Dolphin.DolphinStatus;
import client.exceptions.GameHandlerDisconnectException;
import client.game.interfaces.MemoryAdapter;
import client.view.events.GeneralMessageEvent;
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
    public void connect() throws GameHandlerDisconnectException {
        engine.hook();
        if (engine.getStatus() == DolphinStatus.NO_EMU) {
            log.debug("No emulator was found.");
            throw new GameHandlerDisconnectException("Failed to Find Dolphin, Please Open Dolphin and Restart the Client.");
        } else if (engine.getStatus() == DolphinStatus.HOOKED) {
            log.debug("Hooked to Dolphin\nPublishing Self to Context...");
            isHooked = true;
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Successfully Hooked to Dolphin"));
        }
    }

    @Override
    public Boolean disconnect() {
        return engine.unhook();
    }

    @Override
    public Boolean isConnected() {
        if (engine.getStatus() != DolphinStatus.HOOKED) {
            engine.hook();
        }
        isHooked = engine.getStatus() == DolphinStatus.HOOKED;
        return isHooked;
    }

    @Override
    public Boolean writeByte(Integer consoleAddress, Byte byteVal) {
        return engine.writeByteToRAM(consoleAddress, byteVal);
    }

    @Override
    public Byte readByte(Integer consoleAddress) {
        return engine.readByteFromRAM(consoleAddress);
    }

    @Override
    public Boolean writeShort(Integer consoleAddress, Short shortVal) {
        return engine.writeShortToRAM(consoleAddress, shortVal);
    }

    @Override
    public Short readShort(Integer consoleAddress) {
        return engine.readShortFromRAM(consoleAddress);
    }

    @Override
    public Boolean writeInteger(Integer consoleAddress, Integer integerVal) {
        return engine.writeIntegerToRAM(consoleAddress, integerVal);
    }

    @Override
    public Integer readInteger(Integer consoleAddress) {
        return engine.readIntegerFromRAM(consoleAddress);
    }

    @Override
    public Boolean writeString(Integer consoleAddress, String stringVal) {
        return engine.writeStringToRAM(consoleAddress, stringVal);
    }

    @Override
    public String readString(Integer consoleAddress, Integer stringLength) {
        return engine.readStringFromRAM(consoleAddress, stringLength);
    }
}
