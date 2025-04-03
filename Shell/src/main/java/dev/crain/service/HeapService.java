package dev.crain.service;

import dev.crain.exceptions.memory.MemoryHandlerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

import static dev.crain.game.data.HeapConstants.*;


@Slf4j
@Service
public class HeapService extends MemoryAwareService {

    public HeapService() {
        super();
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    public Map<Short, Integer> getRelLocations() throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        final int TOTAL_ACTORS = memoryAdapter.readInteger(ACTOR_NUMBER_SHORT - 2) & 0x0000_FFFF;
        final int DMC_BASE_ADDRESS = ((memoryAdapter.readInteger(DMC_LIST_POINTER_UPPER_HALFWORD) & 0xFFFF) << 16) + memoryAdapter.readShort(DMC_LIST_POINTER_LOWER_HALFWORD);
        Map<Short, Integer> relLocations = new TreeMap<>(Short::compare);
        for (int refActorId = 0; refActorId < TOTAL_ACTORS; refActorId++) {
            var dmcPointer = memoryAdapter.readInteger(DMC_BASE_ADDRESS + refActorId * 4);
            if (dmcPointer == 0) continue;
            var relPointer = memoryAdapter.readInteger(dmcPointer + 0x10);
            if (relPointer == 0) continue;
            relLocations.put((short) refActorId, relPointer);
        }
        return relLocations;
    }

    public Map<Short, String> getRelFileNameMap() throws MemoryHandlerException {
        Map<Short, String> shortStringMap = new TreeMap<>();
        var memoryAdapter = getMemoryAdapter();
        for (short whoKnows = 0; whoKnows < 0x300; whoKnows++) {
            var checkActor = memoryAdapter.readShort(DYNAMIC_NAME_TABLE.pointer() + whoKnows * DYNAMIC_NAME_TABLE.entryLength());
            if (checkActor == -1) break;
            var nextNamePointerPointer = DYNAMIC_NAME_TABLE.pointer() + whoKnows * DYNAMIC_NAME_TABLE.entryLength() + 4;
            var relFilenamePointer = memoryAdapter.readInteger(DYNAMIC_NAME_TABLE.pointer() + whoKnows * DYNAMIC_NAME_TABLE.entryLength() + 4);
            log.atDebug().setMessage("relFilenamePointer: 0x{}, nextNamePointerPointer: 0x{}")
                    .addArgument(() -> Integer.toHexString(relFilenamePointer))
                    .addArgument(() -> Integer.toHexString(nextNamePointerPointer))
                    .log();
            var relName = memoryAdapter.readStringTillNull(relFilenamePointer);
            shortStringMap.put(checkActor, StringUtils.hasLength(relName) ? relName : "[unknown : custom]");
        }
        return shortStringMap;
    }
}
