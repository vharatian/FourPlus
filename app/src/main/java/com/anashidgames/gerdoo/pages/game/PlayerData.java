package com.anashidgames.gerdoo.pages.game;

import com.anashidgames.gerdoo.core.service.model.ParticipantInfo;

import java.io.Serializable;

/**
 * Created by psycho on 4/24/16.
 */
public class PlayerData implements Serializable {
    public static final int DEFAULT_SCORE = -1;


    private String userId;
    private String imageUrl;
    private String name;
    private int score = DEFAULT_SCORE;

    public PlayerData(String userId, String avatarUrl, String name, int score) {
        this.userId = userId;
        this.imageUrl = avatarUrl;
        this.name = name;
        this.score = score;
    }

    public PlayerData(ParticipantInfo myInfo) {
        userId = myInfo.getUserId();
        imageUrl = myInfo.getImageUrl();
        name = myInfo.getName();
    }

    public PlayerData(ParticipantInfo myInfo, int myScore) {
        this.userId = myInfo.getUserId();
        this.imageUrl = myInfo.getImageUrl();
        this.name = myInfo.getName();
        this.score = myScore;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
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
