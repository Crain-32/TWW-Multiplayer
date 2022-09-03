package client.game.util;

import client.game.data.EventInfo;
import client.game.interfaces.MemoryHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryEditorUtil {

    public static Boolean enableBit(MemoryHandler handler, Integer consoleAddress, Integer bitOffset) {
        return toggleBit(handler, consoleAddress, bitOffset, true);
    }

    public static Boolean disableBit(MemoryHandler handler, Integer consoleAddress, Integer bitOffset) {
        return toggleBit(handler, consoleAddress, bitOffset, false);
    }


    public static Boolean toggleBit(MemoryHandler handler, Integer consoleAddress, Integer bitOffset, Boolean enable) {
        Byte shiftedBit = (byte) (1 << bitOffset);
        Byte currFlagVal = handler.readByte(consoleAddress);
        byte nextVal;
        if (enable) {
            nextVal = (byte) (currFlagVal | shiftedBit);
        } else {
            byte bitMask = (byte) (0b11111111 ^ shiftedBit);
            nextVal = (byte) (bitMask & currFlagVal);
        }
        return handler.writeByte(consoleAddress, nextVal);
    }

    public static Boolean enableEvent(EventInfo event, MemoryHandler handler) {
        return toggleEvent(event, handler, true);
    }

    public static Boolean disableEvent(EventInfo event, MemoryHandler handler) {
        return toggleEvent(event, handler, false);
    }

    private static Boolean toggleEvent(EventInfo event, MemoryHandler handler, Boolean enable) {
        return toggleBit(handler, event.getConsoleAddress(), event.getBitIndex(), enable);
    }
}
