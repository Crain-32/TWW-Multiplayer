package constants;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 * World Type can apply to both the Game Room, and each Player's World.
 * Game Rooms can only have MULTIWORLD || COOP.
 * Players can have all 3 of them.
 * If GameRoom.WorldType == COOP, then the Player's World Type must be COOP
 * If GameRoom.WorldType == MULTIWORLD, then the Player's World Type must be SHARED || MULTIWORLD
 */
@Getter
public enum WorldType {
    @JsonEnumDefaultValue
    SHARED("Shared"),
    MULTIWORLD("Multiworld"),
    COOP("Coop");

    private final String displayName;

    WorldType(String displayName) {
        this.displayName = displayName;
    }

}

