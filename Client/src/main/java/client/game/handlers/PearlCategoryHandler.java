package client.game.handlers;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.EventInfo;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import client.game.util.MemoryEditorUtil;
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
            EventInfo event = convertToEvent(info);
            setPearl = MemoryEditorUtil.enableEvent(event, memoryAdapter);
            if (!setPearl) {
                throw new FailedToGiveItemException("Failed to Enable the Pearl Flag", info);
            }
            checkToTG();
            return true;
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception e) {
            log.debug(setPearl? "Pearl was Placed" : "Pearl wasn't placed");
            throw new FailedToGiveItemException(e.getMessage(), info, e);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        boolean setPearl = false;
        try {
            EventInfo event = convertToEvent(info);
            setPearl = MemoryEditorUtil.disableEvent(event, memoryAdapter);
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
        Byte pearlStatus = memoryAdapter.readByte(EventInfo.DINS_PEARL.getConsoleAddress());
        if (pearlStatus == 0x07) {
            MemoryEditorUtil.enableEvent(EventInfo.RAISE_TOTG, memoryAdapter);
        } else {
            MemoryEditorUtil.disableEvent(EventInfo.RAISE_TOTG, memoryAdapter);
        }
    }

    private EventInfo convertToEvent(ItemInfo info) {
        return switch (info) {
            case DINS_PEARL -> EventInfo.DINS_PEARL;
            case FARORES_PEARL -> EventInfo.FARORES_PEARL;
            case NAYRUS_PEARL -> EventInfo.NAYRUS_PEARL;
            default -> throw new IllegalArgumentException("Unsupported Item Provided: " + info);
        };
    }
}
