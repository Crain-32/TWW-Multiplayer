package unit.GameDataEnumTests;

import crain.client.game.data.ChartInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

@DisplayName("Chart Info Unit Test")
public class ChartInfoTest {

    @Test
    void itemIdsShouldBeUnique() {
        Set<Byte> itemIdSet = new HashSet<>();
        for (var stageFlag : ChartInfo.values()) {
            Assertions.assertTrue(itemIdSet.add(stageFlag.getItemId()));
        }
    }
}
