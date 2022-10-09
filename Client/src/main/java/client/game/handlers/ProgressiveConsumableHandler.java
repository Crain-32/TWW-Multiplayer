package client.game.handlers;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProgressiveConsumableHandler extends ItemCategoryHandler {


    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.PROGRESSIVE_CONSUMABLE;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            Integer maxAmountAddress = maxAmountAddress(info);
            Integer currAmountAddress = currentAmountAddress(info);
            Byte currMaxAmount = memoryAdapter.readByte(maxAmountAddress);
            byte nextMaxAmount;
            if (currMaxAmount == 99) {
                return true;
            } else if (currMaxAmount == 60) {
                nextMaxAmount = 99;
            } else if (currMaxAmount == 30) {
                nextMaxAmount = 60;
            } else {
                nextMaxAmount = 30;
            }
            Boolean updateMaxAmount = memoryAdapter.writeByte(maxAmountAddress, nextMaxAmount);
            if (!updateMaxAmount) {
                throw new FailedToGiveItemException("Failed to Upgrade Max Amount from " + currMaxAmount + " to " + nextMaxAmount, info);
            }
            Boolean playerGotMaxAmount = memoryAdapter.writeByte(currAmountAddress, nextMaxAmount);
            if (!playerGotMaxAmount) {
                log.debug("Failed to Set Curr Amount to {}", nextMaxAmount);
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
            Integer maxAmountAddress = maxAmountAddress(info);
            Integer currAmountAddress = currentAmountAddress(info);
            Byte currMaxAmount = memoryAdapter.readByte(maxAmountAddress);
            byte nextMaxAmount;
            if (currMaxAmount == 30 || currMaxAmount == 0) {
                return true;
            } else if (currMaxAmount == 99) {
                nextMaxAmount = 60;
            } else {
                nextMaxAmount = 30;
            }
            Boolean updateMaxAmount = memoryAdapter.writeByte(maxAmountAddress, nextMaxAmount);
            if (!updateMaxAmount) {
                throw new FailedToTakeItemException("Failed to Downgrade Max Amount from " + currMaxAmount + " to " + nextMaxAmount, info);
            }
            Boolean playerGotMaxAmount = memoryAdapter.writeByte(currAmountAddress, nextMaxAmount);
            if (!playerGotMaxAmount) {
                log.debug("Failed to Set Curr Amount to {}", nextMaxAmount);
            }
            return true;
        } catch (FailedToTakeItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToTakeItemException(e.getMessage(), info, e);
        }
    }

    private Integer maxAmountAddress(ItemInfo info) {
        return switch (info) {
            case SIXTY_QUIVER, MAX_QUIVER -> 0x803C4C77;
            case SIXTY_BOMB_BAG, MAX_BOMB_BAG -> 0x803C4C78;
            default -> throw new IllegalArgumentException("Unsupported Item Provided: " + info);
        };
    }

    private Integer currentAmountAddress(ItemInfo info) {
        return switch (info) {
            case SIXTY_QUIVER, MAX_QUIVER -> 0x803C4C71;
            case SIXTY_BOMB_BAG, MAX_BOMB_BAG -> 0x803C4C7;
            default -> throw new IllegalArgumentException("Unsupported Item Provided: " + info);
        };
    }
}
