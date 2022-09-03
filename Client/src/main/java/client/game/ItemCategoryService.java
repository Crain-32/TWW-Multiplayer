package client.game;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ItemCategoryService {

    private final Map<ItemCategory, ItemCategoryHandler> resolvedHandlers;

    public ItemCategoryService(List<? extends ItemCategoryHandler> handlers) {
        resolvedHandlers = new HashMap<>();

        for (ItemCategory category : ItemCategory.values()) {
            log.debug("Registering " + category);
            ItemCategoryHandler supported = handlers.stream()
                    .filter(
                            handler -> handler.supports(category)
                    ).findFirst().orElseThrow(IllegalStateException::new);
            log.debug("Registered: " + category + " to " + supported.getClass().getSimpleName());
            resolvedHandlers.put(category, supported);
        }
    }

    public Boolean giveItem(Integer itemId) {
        return giveItem(ItemInfo.getInfoByItemId(itemId));
    }

    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = resolvedHandlers.get(category);
        return categoryHandler.giveItem(info);
    }

    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = resolvedHandlers.get(category);
        return categoryHandler.giveItem(info);
    }

}
