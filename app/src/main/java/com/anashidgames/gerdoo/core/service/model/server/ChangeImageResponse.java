package com.anashidgames.gerdoo.core.service.model.server;

/**
 * Created by psycho on 4/19/16.
 */
public class ChangeImageResponse {
    private boolean done;
    private String newUrl;

    public ChangeImageResponse(boolean done, String newUrl) {
        this.done = done;
        this.newUrl = newUrl;
    }

    public boolean isDone() {
        return done;
    }

    public String getNewUrl() {
        return newUrl;
    }
}
