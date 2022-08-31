package client.game.interfaces;

import client.exceptions.GameHandlerDisconnectException;

public interface ConnectionHandler {

    /**
     * A call to this function should connect the Client to the Game using the
     * Communication Protocol being implemented.
     * Failure to connect should raise a GameHandlerDisconnectWarning
     */
    void connect() throws GameHandlerDisconnectException;

    /**
     * True if the Connection to the Game is current connected and usable.
     */
    Boolean isConnected();

}
