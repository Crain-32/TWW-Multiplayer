package dev.crain.events;

public record GameFlagToggleEvent(FlagType type, Integer memoryAddress, Integer byteOffset, Integer bitOffset, Boolean state) {

    public enum FlagType {
        STAGE,
        STORY
    }
}
