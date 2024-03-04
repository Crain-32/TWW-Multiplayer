package dev.crain.game.util;

import dev.crain.game.interfaces.MemoryAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryScanUtil {

    public static Integer findOpenIndex(MemoryAdapter handler, Integer consoleAddress, Integer length) {
        return findByteInList(handler, consoleAddress, length, (byte) 0xFF);
    }

    public static Integer findByteInList(MemoryAdapter handler, Integer consoleAddress, Integer length, Byte targetVal) {
        for (int index = 0; index < length; index++) {
            if (Objects.equals(handler.readByte(consoleAddress + index), targetVal)) {
                return index;
            }
        }
        return -1;
    }
}
