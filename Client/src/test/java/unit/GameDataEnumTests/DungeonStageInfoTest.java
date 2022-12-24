package unit.GameDataEnumTests;

import crain.client.game.data.DungeonStageInfo;
import crain.client.game.data.ItemInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Dungeon Stage Unit Test")
public class DungeonStageInfoTest {

    @Test
    void itemsShouldMapCorrectly() {
        List<ItemInfo> drcItems = List.of(ItemInfo.DRC_DUNGEON_COMPASS, ItemInfo.DRC_DUNGEON_MAP, ItemInfo.DRC_BIG_KEY, ItemInfo.DRC_SMALL_KEY);
        List<ItemInfo> fwItems = List.of(ItemInfo.FW_DUNGEON_MAP, ItemInfo.FW_DUNGEON_COMPASS, ItemInfo.FW_BIG_KEY, ItemInfo.FW_SMALL_KEY);
        List<ItemInfo> totgItems = List.of(ItemInfo.TOTG_DUNGEON_COMPASS, ItemInfo.TOTG_DUNGEON_MAP, ItemInfo.TOTG_BIG_KEY, ItemInfo.TOTG_SMALL_KEY);
        List<ItemInfo> ffItems = List.of(ItemInfo.FF_DUNGEON_MAP, ItemInfo.FF_DUNGEON_COMPASS);
        List<ItemInfo> etItems = List.of(ItemInfo.ET_DUNGEON_MAP, ItemInfo.ET_DUNGEON_COMPASS, ItemInfo.ET_SMALL_KEY, ItemInfo.ET_BIG_KEY);
        List<ItemInfo> wtItems = List.of(ItemInfo.WT_DUNGEON_MAP, ItemInfo.WT_DUNGEON_COMPASS, ItemInfo.WT_BIG_KEY, ItemInfo.WT_SMALL_KEY);
        for (var info : drcItems) {
            Assertions.assertEquals((byte) 3, DungeonStageInfo.stageIdFromItem(info));
        }
        for (var info : fwItems) {
            Assertions.assertEquals((byte) 4, DungeonStageInfo.stageIdFromItem(info));
        }
        for (var info : totgItems) {
            Assertions.assertEquals((byte) 5, DungeonStageInfo.stageIdFromItem(info));
        }
        for (var info : ffItems) {
            Assertions.assertEquals((byte) 2, DungeonStageInfo.stageIdFromItem(info));
        }
        for (var info : etItems) {
            Assertions.assertEquals((byte) 6, DungeonStageInfo.stageIdFromItem(info));
        }
        for (var info : wtItems) {
            Assertions.assertEquals((byte) 7, DungeonStageInfo.stageIdFromItem(info));
        }
    }
}
