package client.game;


import client.game.interfaces.MemoryAdapter;

public class GameInterfaceEvents {
    public record MemoryHandlerEvent(MemoryAdapter memoryAdapter){}

}
