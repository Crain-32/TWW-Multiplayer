package client.game.handlers;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * To make sure we can have good tests, we
 * need a bucket to dump anything we're not supporting,
 * or is not yet implemented.
 */
@Slf4j
@Service
public class UnsupportedItemHandler extends ItemCategoryHandler {
    private final List<ItemCategory> supported = Arrays.asList(
            ItemCategory.NOT_SUPPORTED, ItemCategory.TRANSLATED_CHARTS,
            ItemCategory.SPOILS_BAG_CONSUMABLE, ItemCategory.BAIT_BAG_CONSUMABLES,
            ItemCategory.BIT_ONLY_ACTION);
    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return supported.stream().anyMatch(category -> (category == itemCategory));
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        return true;
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        return true;
    }
}
