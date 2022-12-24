package crain.client.game;


import crain.client.game.interfaces.MemoryAdapter;

public class GameInterfaceEvents {
    public record MemoryHandlerEvent(MemoryAdapter memoryAdapter){}

}
