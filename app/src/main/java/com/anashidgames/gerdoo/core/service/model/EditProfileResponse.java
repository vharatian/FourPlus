package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 6/12/16.
 */
public class EditProfileResponse {
    private boolean done;

    public EditProfileResponse(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
