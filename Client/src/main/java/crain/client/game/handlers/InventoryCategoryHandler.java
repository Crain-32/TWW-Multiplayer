package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.exceptions.memory.MissingMemoryAdapterException;
import crain.client.game.data.InventoryLocations;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InventoryCategoryHandler extends ItemCategoryHandler {

    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.INVENTORY_ITEM;
    }

    // Bows and Picto boxes are handled by the ProgressiveCategoryHandler.
    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            verifyHandler();
            Integer inventoryAddress = InventoryLocations.getInventoryLocation(info);
            Integer ownedBitLocation = InventoryLocations.getItemOwnedMemoryLocation(info);
            Boolean giveItem = memoryAdapter.writeByte(inventoryAddress, info.getItemId());
            if (!giveItem) {
                throw new FailedToGiveItemException("Failed to set Inventory Value", info);
            }
            return memoryAdapter.writeByte(ownedBitLocation, (byte) 1);
        } catch (MissingMemoryAdapterException memoryHandlerException) {
            throw new FailedToGiveItemException("No MemoryHandler Present");
        } catch (Exception exception) {
            throw new FailedToGiveItemException(exception.getMessage(), info);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        try {
            verifyHandler();
            Integer inventoryAddress = InventoryLocations.getInventoryLocation(info);
            Integer ownedBitLocation = InventoryLocations.getItemOwnedMemoryLocation(info);
            Boolean giveItem = memoryAdapter.writeByte(inventoryAddress, ItemInfo.INVALID_ID.getItemId());
            if (!giveItem) {
                throw new FailedToTakeItemException("Failed to set Inventory Value", info);
            }
            return memoryAdapter.writeByte(ownedBitLocation, (byte) 0);
        } catch (MissingMemoryAdapterException memoryHandlerException) {
            throw new FailedToTakeItemException("No MemoryHandler Present");
        } catch (Exception exception) {
            throw new FailedToTakeItemException(exception.getMessage(), info);
        }
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
