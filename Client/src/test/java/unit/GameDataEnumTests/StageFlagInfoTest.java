package unit.GameDataEnumTests;

import crain.client.game.data.StageFlagInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

@DisplayName("Stage Flag Info Unit Tests")
public class StageFlagInfoTest {

    @Test
    void stageIdMustBeUnique() {
        Set<Byte> stageIdSet = new HashSet<>();
        for (var stageFlag: StageFlagInfo.values()) {
            Assertions.assertTrue(stageIdSet.add(stageFlag.getStageId()));
        }
    }
}
