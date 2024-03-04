package dev.crain.game;


import dev.crain.game.interfaces.MemoryAdapter;

public class GameInterfaceEvents {
    public record MemoryHandlerEvent(MemoryAdapter memoryAdapter){}

}
