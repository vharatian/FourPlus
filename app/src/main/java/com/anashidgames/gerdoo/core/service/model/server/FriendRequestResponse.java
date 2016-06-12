package com.anashidgames.gerdoo.core.service.model.server;

/**
 * Created by psycho on 4/18/16.
 */
public class FriendRequestResponse {
    private boolean done;

    public FriendRequestResponse(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

}
