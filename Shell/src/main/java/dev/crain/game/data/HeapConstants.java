package dev.crain.game.data;

public class HeapConstants {

    public record HeapList(Integer pointer, Integer maxEntries, Integer entryLength) {}
    public static final Integer GAME_HEAP_START_POINTER_ALT = 0x8037161C;
    public static final Integer GAME_HEAP_START_POINTER = 0x803F6920;
    public static final Integer ZELDA_HEAP_START_POINTER = 0x803F6928;
    public static final Integer COMMAND_HEAP_START_POINTER = 0x803F6930;
    public static final Integer ARCHIVE_HEAP_START_POINTER = 0x803F6938;

    public static final Integer DMC_LIST_POINTER_UPPER_HALFWORD = 0x80022818;
    public static final Integer DMC_LIST_POINTER_LOWER_HALFWORD = 0x8002281C + 0x2;
    public static final Integer ACTOR_NUMBER_SHORT = 0x80022852;
    public static final HeapList DYNAMIC_NAME_TABLE = new HeapList(0x803398D8, -1,0x8);
    public static final HeapList ACTOR_PROFILES = new HeapList(0x803F6A68, 0x1F6, 0x4);
}
