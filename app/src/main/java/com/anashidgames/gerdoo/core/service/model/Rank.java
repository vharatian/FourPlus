package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 3/31/16.
 */
public class Rank {

    private int rank;
    private int score;
    private String name;
    private String userProfileUrl;
    private int rankChange;

    public Rank(int rank, int score, String name, String userProfileUrl, int rankChange) {
        this.rank = rank;
        this.score = score;
        this.name = name;
        this.userProfileUrl = userProfileUrl;
        this.rankChange = rankChange;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public int getRankChange() {
        return rankChange;
    }
}
