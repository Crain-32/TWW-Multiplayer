package crain.model.event;

public sealed interface MultiplayerEvent<T> permits
        CoopItemEvent,
        ErrorEvent,
        EventEvent,
        GameRoomMessageEvent,
        MultiworldItemEvent,
        NameEvent {
    String getGameRoomName();

    T getPayload();
}
