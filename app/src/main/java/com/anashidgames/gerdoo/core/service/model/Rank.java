package com.anashidgames.gerdoo.core.service.model;

import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardItem;

import java.util.List;

/**
 * Created by psycho on 3/31/16.
 */
public class Rank {

    private int rank;
    private int score;
    private String name;
    private String userId;
    private int rankChange;

    public Rank(int rank, int score, String name, String userId, int rankChange) {
        this.rank = rank;
        this.score = score;
        this.name = name;
        this.userId = userId;
        this.rankChange = rankChange;
    }

    public Rank(LeaderBoardItem item, int rank) {
        int score = 0;
        List<Integer> scores = item.getScores();
        if(scores != null && !scores.isEmpty())
            score = scores.get(0);

        this.rank = rank;
        this.score = score;
        this.rankChange = 0;

        LeaderBoardItem.UserBriefProfile profile = item.getUserBriefProfile();
        if (profile != null) {
            this.name = profile.getFirstName() + " " + profile.getLastName();
            this.userId = profile.getUserId();
        }
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

    public String getUserId() {
        return userId;
    }

    public int getRankChange() {
        return rankChange;
    }
}
