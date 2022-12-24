package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.data.StoryFlagInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import crain.client.game.util.MemoryEditorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PearlCategoryHandler extends ItemCategoryHandler {
    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.PEARLS;
    }


    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        boolean setPearl = false;
        try {
            StoryFlagInfo event = convertToEvent(info);
            setPearl = MemoryEditorUtil.enableStoryEvent(event, memoryAdapter);
            if (!setPearl) {
                throw new FailedToGiveItemException("Failed to Enable the Pearl Flag", info);
            }
            checkToTG();
            return true;
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception e) {
            log.debug(setPearl ? "Pearl was Placed" : "Pearl wasn't placed");
            throw new FailedToGiveItemException(e.getMessage(), info, e);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        boolean setPearl = false;
        try {
            StoryFlagInfo event = convertToEvent(info);
            setPearl = MemoryEditorUtil.disableStoryEvent(event, memoryAdapter);
            if (!setPearl) {
                throw new FailedToTakeItemException("Failed to Disable the Pearl Flag", info);
            }
            checkToTG();
            return true;
        } catch (FailedToTakeItemException e) {
            throw e;
        } catch (Exception e) {
            log.debug(setPearl ? "Pearl was Removed" : "Place wasn't Removed");
            throw new FailedToTakeItemException(e.getMessage(), info, e);
        }
    }

    private void checkToTG() {
        Byte pearlStatus = memoryAdapter.readByte(StoryFlagInfo.DINS_PEARL.getMemoryAddress());
        if (pearlStatus == 0x07) {
            MemoryEditorUtil.enableStoryEvent(StoryFlagInfo.RAISE_TOTG, memoryAdapter);
        } else {
            MemoryEditorUtil.disableStoryEvent(StoryFlagInfo.RAISE_TOTG, memoryAdapter);
        }
    }

    private StoryFlagInfo convertToEvent(ItemInfo info) {
        return switch (info) {
            case DINS_PEARL -> StoryFlagInfo.DINS_PEARL;
            case FARORES_PEARL -> StoryFlagInfo.FARORES_PEARL;
            case NAYRUS_PEARL -> StoryFlagInfo.NAYRUS_PEARL;
            default -> throw new IllegalArgumentException("Unsupported Item Provided: " + info);
        };
    }
}
