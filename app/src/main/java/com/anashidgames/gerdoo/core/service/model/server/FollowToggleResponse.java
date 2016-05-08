package com.anashidgames.gerdoo.core.service.model.server;

/**
 * Created by psycho on 4/18/16.
 */
public class FollowToggleResponse {
    private boolean done;
    private String toggleUrl;

    public FollowToggleResponse(boolean done, String toggleUrl) {
        this.done = done;
        this.toggleUrl = toggleUrl;
    }

    public boolean isDone() {
        return done;
    }

    public String getToggleUrl() {
        return toggleUrl;
    }
}
