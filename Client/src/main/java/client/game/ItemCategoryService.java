package client.game;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemCategoryService {

    private List<? extends ItemCategoryHandler> handlers;

    public Boolean giveItem(Integer itemId) {
        return giveItem(ItemInfo.getInfoByItemId(itemId));
    }

    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = handlers.stream()
                .filter(handler -> handler.supports(category))
                .findFirst()
                .orElseThrow(() -> new FailedToGiveItemException("No Handler could be found", info));
        return categoryHandler.giveItem(info);
    }

    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = handlers.stream()
                .filter(handler -> handler.supports(category))
                .findFirst()
                .orElseThrow(() -> new FailedToTakeItemException("No Handler could be found", info));
        return categoryHandler.giveItem(info);
    }

}
