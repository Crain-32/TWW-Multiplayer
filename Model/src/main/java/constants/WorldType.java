package constants;

import lombok.Getter;

/**
 * World Type can apply to both the Game Room, and each Player's World.
 * Game Rooms can only have MULTIWORLD || COOP.
 * Players can have all 3 of them.
 * If GameRoom.WorldType == COOP, then the Player's World Type must be COOP
 * If GameRoom.WorldType == MULTIWORLD, then the Player's World Type must be SHARED || COOP
 */
@Getter
public enum WorldType {
    SHARED("Shared"),
    MULTIWORLD("Multiworld"),
    COOP("Coop");

    private final String displayName;

    WorldType(String displayName) {
        this.displayName = displayName;
    }

}

