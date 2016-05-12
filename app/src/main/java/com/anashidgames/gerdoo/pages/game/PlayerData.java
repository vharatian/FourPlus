package com.anashidgames.gerdoo.pages.game;

import com.anashidgames.gerdoo.core.service.model.ParticipantInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by psycho on 4/24/16.
 */
public class PlayerData implements Serializable {
    public static final int DEFAULT_SCORE = -1;


    private String userId;
    private String avatarUrl;
    private String name;
    private int score = DEFAULT_SCORE;

    public PlayerData(String userId, String avatarUrl, String name, int score) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.score = score;
    }

    public PlayerData(ParticipantInfo myInfo) {
        userId = myInfo.getUserId();
        avatarUrl = myInfo.getImageUrl();
        name = myInfo.getName();
    }

    public String getUserId() {
        return userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean hasScore(){
        return score >= 0;
    }
}
