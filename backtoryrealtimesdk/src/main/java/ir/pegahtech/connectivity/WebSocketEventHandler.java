package ir.pegahtech.connectivity;

import java.io.IOException;


public interface WebSocketEventHandler {
    public void onOpen();

    public void onMessage(WebsocketMessage message);

    public void onError(IOException exception);

    public void onClose();
}
