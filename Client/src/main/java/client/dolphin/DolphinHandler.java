package client.dolphin;

import Dolphin.DolphinEngine;
import Dolphin.DolphinFactory;
import Dolphin.DolphinStatus;
import client.exceptions.GameHandlerDisconnectException;
import client.game.GameInterfaceEvents;
import client.game.interfaces.ConnectionHandler;
import client.game.interfaces.MemoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Lazy
@Component
public class DolphinHandler implements ConnectionHandler, MemoryHandler {

    private final DolphinEngine engine;

    private final ApplicationEventPublisher applicationEventPublisher;
    private boolean isHooked;

    public DolphinHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.engine = DolphinFactory.createEngine();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @EventListener(HookEvent.class)
    public void connect() throws GameHandlerDisconnectException {
        engine.hook();
        if (engine.getStatus() == DolphinStatus.NO_EMU) {
            log.debug("No emulator was found.");
        } else if (engine.getStatus() == DolphinStatus.HOOKED) {
            log.debug("Hooked to Dolphin");
            log.debug("Publishing Self to Context...");
            isHooked = true;
            applicationEventPublisher.publishEvent(new GameInterfaceEvents.MemoryHandlerEvent(this));
        }
    }

    @Override
    public Boolean isConnected() {
        if (!isHooked) {
            engine.hook();
        }
        return engine.getStatus() == DolphinStatus.HOOKED;
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


    public static class HookEvent {
    }
}
