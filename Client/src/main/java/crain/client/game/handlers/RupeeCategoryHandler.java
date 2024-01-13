package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.exceptions.memory.MissingMemoryAdapterException;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import org.springframework.stereotype.Service;

@Service
public class RupeeCategoryHandler extends ItemCategoryHandler {

    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.RUPEES;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            // Add Rupoors (Be Cringe)
            verifyHandler();
            return this.memoryAdapter.writeInteger(0x803CA768, getRupeeAmount(info));
        } catch (MissingMemoryAdapterException memoryHandlerException) {
            throw new FailedToGiveItemException("No MemoryHandler Present");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new FailedToGiveItemException("Failed to Map the Rupee Value", info);
        } catch (Exception e) {
            throw new FailedToGiveItemException("Failed to give the following item", info);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        // We'll worry about Taking items later.
        return true;
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    private Integer getRupeeAmount(ItemInfo info) throws IllegalArgumentException {
        return switch (info) {
            case GREEN_RUPEE -> 0x1;
            case BLUE_RUPEE -> 0x5;
            case YELLOW_RUPEE -> 0xA;
            case RED_RUPEE -> 0x14;
            case PURPLE_RUPEE -> 0x32;
            case ORANGE_RUPEE -> 0x64;
            case SILVER_RUPEE -> 0xC8;
            case TINGLE_RUPEE -> 0x1F4;
            default -> throw new IllegalArgumentException("The Provided ItemInfo was not a rupee!");
        };
    }

}
