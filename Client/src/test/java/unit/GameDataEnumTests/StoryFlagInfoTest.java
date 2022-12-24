package unit.GameDataEnumTests;

import crain.client.game.data.StoryFlagInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Story Flag Info Unit Tests")
public class StoryFlagInfoTest {

    @Test
    void storyFlagsBitIndexBelowEight() {
        for (var storyFlag: StoryFlagInfo.values()) {
            Assertions.assertTrue(storyFlag.getBitIndex() <= 7);
        }
    }
}
