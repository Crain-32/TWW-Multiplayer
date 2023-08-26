package crain.client.service;

import crain.client.events.GameFlagToggleEvent;
import crain.client.game.data.StageFlagInfo;
import crain.client.game.data.StoryFlagInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import records.INFO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GameFlagService extends MemoryAwareService {

    private final Map<StageFlagInfo, byte[]> stageFlags;
    private final Map<StoryFlagInfo, Boolean> storyFlags;

    public GameFlagService() {
        stageFlags = createStageFlags();
        storyFlags = createStoryFlags();
    }

    private Map<StoryFlagInfo, Boolean> createStoryFlags() {
        Map<StoryFlagInfo, Boolean> tempMap = new HashMap<>();
        for(var info : StoryFlagInfo.values()) {
            tempMap.put(info, false);
        }
        return tempMap;
    }

    private Map<StageFlagInfo, byte[]> createStageFlags() {
        Map<StageFlagInfo, byte[]> tempMap = new HashMap<>();
        for (var info: StageFlagInfo.values()) {
            tempMap.put(info, new byte[26]);
        }
        tempMap.remove(StageFlagInfo.CURRENT);
        return tempMap;
    }

    @Async
    @EventListener(GameFlagToggleEvent.class)
    public void handleGameToggle(GameFlagToggleEvent event) {
        if (event.type() == GameFlagToggleEvent.FlagType.STORY) {
            handleStoryFlag(event);
        } else {
            handleStageFlag(event);
        }
    }

    private void handleStoryFlag(GameFlagToggleEvent event) {
        StoryFlagInfo info = StoryFlagInfo.fromMemoryAddressAndBitIndex(event.memoryAddress(), event.bitOffset());
        storyFlags.put(info, event.state());
    }

    private void handleStageFlag(GameFlagToggleEvent event) {
        StageFlagInfo info = StageFlagInfo.fromMemoryAddress(event.memoryAddress());
        var buffer = stageFlags.get(info);

        if (event.byteOffset() >= buffer.length || event.byteOffset() < 0) {
            log.error("Invalid Event Provided! Byte Offset of {}", event.byteOffset());
            return;
        } else if (event.bitOffset() >= 8 || event.bitOffset() < 0) {
            log.error("Invalid Event Provided! Bit Offset of {}", event.bitOffset());
            return;
        }

        var flagState = buffer[event.byteOffset()]; // Get the Byte Representing the state.
        var byteMask = (byte) (1 << event.bitOffset()); // Create Byte Mask

        if (!event.state()) {
            byteMask = (byte) ~byteMask;   // Invert the Mask for the AND
            flagState = (byte) (flagState & byteMask);
        } else {
            flagState = (byte) (flagState | byteMask); // OR is a safe operation.
        }
        // Going to do a full reassignment to be safe about the Map's Object State.
        buffer[event.byteOffset()] = flagState;
        stageFlags.put(info, buffer);
    }


    @Async
    @EventListener(INFO.EventRecord.class)
    public void handleEventRecord(INFO.EventRecord event) {

    }


    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
