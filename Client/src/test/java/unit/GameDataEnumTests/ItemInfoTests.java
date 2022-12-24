package unit.GameDataEnumTests;


import crain.client.game.data.ItemInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@DisplayName("ItemInfo Enum Tests")
public class ItemInfoTests {

    @Test //All Item Ids Should be Unique
    public void verifyAllItemsIdsAreUnique(){
        List<Byte> itemIds =  Arrays.stream(ItemInfo.values()).map(ItemInfo::getItemId).distinct().toList();
        Assertions.assertEquals(itemIds.size(), ItemInfo.values().length);
    }

    @Test //All Item Names Should be Unique
    public void verifyAllItemsNamesAreUnique(){
        List<String> itemIds =  Arrays.stream(ItemInfo.values()).map(ItemInfo::getDisplayName).distinct().toList();
        Assertions.assertEquals(itemIds.size(), ItemInfo.values().length);
    }

    @Test //All Maximum Values should be Non-Zero or -1
    public void verifyAllMaximumValues() {
        for (ItemInfo info: ItemInfo.values()) {
            Integer maxAmount = info.getMaxAmount();
            Assertions.assertTrue(maxAmount > 0 || maxAmount == -1, info.getDisplayName() + " has an invalid amount");
        }
    }

    @Test //Retrieving by Item ID should work
    public void getItemInfoByItemID() {
        ItemInfo info = ItemInfo.getInfoByItemId(0xFF);
        Assertions.assertEquals(info, ItemInfo.INVALID_ID);
    }
}
