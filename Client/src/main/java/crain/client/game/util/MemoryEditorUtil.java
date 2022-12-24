package crain.client.game.util;

import crain.client.game.data.StoryFlagInfo;
import crain.client.game.interfaces.MemoryAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryEditorUtil {

    public static Boolean enableBit(MemoryAdapter handler, Integer consoleAddress, Integer bitOffset) {
        return toggleBit(handler, consoleAddress, bitOffset, true);
    }

    public static Boolean disableBit(MemoryAdapter handler, Integer consoleAddress, Integer bitOffset) {
        return toggleBit(handler, consoleAddress, bitOffset, false);
    }


    public static Boolean toggleBit(MemoryAdapter handler, Integer consoleAddress, Integer bitOffset, Boolean enable) {
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

    public static Boolean enableStoryEvent(StoryFlagInfo event, MemoryAdapter handler) {
        return toggleStoryEvent(event, handler, true);
    }

    public static Boolean disableStoryEvent(StoryFlagInfo event, MemoryAdapter handler) {
        return toggleStoryEvent(event, handler, false);
    }

    private static Boolean toggleStoryEvent(StoryFlagInfo event, MemoryAdapter handler, Boolean enable) {
        return toggleBit(handler, event.getMemoryAddress(), event.getBitIndex(), enable);
    }
}
