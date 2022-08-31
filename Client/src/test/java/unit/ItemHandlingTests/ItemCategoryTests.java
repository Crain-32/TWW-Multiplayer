package unit.ItemHandlingTests;

import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DisplayName("Item Category Unit Tests")
public class ItemCategoryTests {


    @Test // Only one Category per Item
    public void verifyNoDuplicates() {
        ArrayList<ItemInfo> items = new ArrayList<>();
        Arrays.stream(ItemCategory.values())
                .map(ItemCategory::getItems)
                .map(Arrays::asList)
                .forEach(items::addAll);
        Assertions.assertTrue(items.size() != 0);
        List<ItemInfo> filteredItems = items.stream().distinct().toList();
        Assertions.assertEquals(filteredItems.size(), items.size());
    }

    @Test //Every Item needs to have a Category for the Flow to work
    public void verifyAllItemsHaveCategory() {
        ArrayList<ItemInfo> catItems = new ArrayList<>();
        Arrays.stream(ItemCategory.values())
                .map(ItemCategory::getItems)
                .map(Arrays::asList)
                .forEach(catItems::addAll);
        Assertions.assertEquals(catItems.size(), Arrays.asList(ItemInfo.values()).size());
    }

    @Test
    public void verifyCategoryByItemInfo() {
        ItemCategory category = ItemCategory.getInfoCategory(ItemInfo.GRAPPLING_HOOK);
        Assertions.assertEquals(category, ItemCategory.INVENTORY_ITEM);
    }
}
