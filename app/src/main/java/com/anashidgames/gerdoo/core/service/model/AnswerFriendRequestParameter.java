package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class AnswerFriendRequestParameter {
    @SerializedName("userId")
    private String userId;
    @SerializedName("answer")
    private boolean answer;

    public AnswerFriendRequestParameter(String userId, boolean answer) {
        this.userId = userId;
        this.answer = answer;
    }
}
