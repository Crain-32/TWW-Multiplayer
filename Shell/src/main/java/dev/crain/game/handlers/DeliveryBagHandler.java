package dev.crain.game.handlers;

import dev.crain.exceptions.FailedToGiveItemException;
import dev.crain.exceptions.FailedToTakeItemException;
import dev.crain.game.data.ItemCategory;
import dev.crain.game.data.ItemInfo;
import dev.crain.game.interfaces.ItemCategoryHandler;
import dev.crain.game.util.MemoryScanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryBagHandler extends ItemCategoryHandler {

    private final Integer deliveryBagAddress = 0x803C4C8E;
    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.DELIVERY_BAG_ITEMS;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            var memoryAdapter = getMemoryAdapter();
            Integer openIndex = MemoryScanUtil.findOpenIndex(memoryAdapter, deliveryBagAddress, 9);
            if (openIndex < 0) {
                log.debug("Delivery Bag is Full? {}", memoryAdapter.readString(deliveryBagAddress, 9));
                throw new FailedToGiveItemException("Failed to find an open space", info);
            }
            Boolean placedItem = memoryAdapter.writeByte(deliveryBagAddress + openIndex, info.getItemId());
            if (!placedItem) {
                log.debug("Failed to write Item, Memory Handler Issue?");
                throw new FailedToGiveItemException("Failed to write to memory", info);
            }
            return true;
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToGiveItemException(e.getMessage(), info, e);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        try {
            var memoryAdapter = getMemoryAdapter();
            Integer openIndex = MemoryScanUtil.findByteInList(memoryAdapter, deliveryBagAddress, 9, info.getItemId());
            if (openIndex < 0) {
                log.debug("Player doesn't have {}, Index: {}", info.getDisplayName(), memoryAdapter.readString(deliveryBagAddress, 9));
                return true;
            }
            Boolean placedItem = memoryAdapter.writeByte(deliveryBagAddress + openIndex, (byte) 0xFF);
            if (!placedItem) {
                log.debug("Failed to Overwrite Item, Memory Handler Issue?");
                throw new FailedToTakeItemException("Failed to erase Item", info);
            }
            return true;
        } catch (FailedToTakeItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToTakeItemException(e.getMessage(), info, e);
        }
    }


    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
