package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class DoneResponse {
    @SerializedName("done")
    private boolean done;

    public DoneResponse(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
