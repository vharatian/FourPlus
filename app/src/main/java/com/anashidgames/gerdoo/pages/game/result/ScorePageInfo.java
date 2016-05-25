package com.anashidgames.gerdoo.pages.game.result;

import com.anashidgames.gerdoo.core.LevelCalculator;
import com.anashidgames.gerdoo.core.service.model.MatchHistoryItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by psycho on 5/1/16.
 */
public class ScorePageInfo implements Serializable{
    private Scores scores;
    private LevelChartInfo levelChartInfo;
    private AdInfo adInfo;
    private List<MatchHistoryItem> histories;
    private String userId;

    public ScorePageInfo(String userId, Scores scores, LevelChartInfo levelChartInfo, AdInfo adInfo, List<MatchHistoryItem> histories) {
        this.userId = userId;
        this.scores = scores;
        this.levelChartInfo = levelChartInfo;
        this.adInfo = adInfo;
        this.histories = histories;
    }

    public String getUserId() {
        return userId;
    }

    public Scores getScores() {
        return scores;
    }

    public LevelChartInfo getLevelChartInfo() {
        return levelChartInfo;
    }

    public AdInfo getAdInfo() {
        return adInfo;
    }

    public List<MatchHistoryItem> getHistories() {
        return histories;
    }

    public static class Scores implements Serializable{
        private int thisRoundScore;
        private int boostFactor;
        private int finishing;
        private int totalScore;

        public Scores(int thisRoundScore, int boostFactor, int finishing, int totalScore) {
            this.thisRoundScore = thisRoundScore;
            this.boostFactor = boostFactor;
            this.finishing = finishing;
            this.totalScore = totalScore;
        }

        public int getThisRoundScore() {
            return thisRoundScore;
        }

        public int getBoostFactor() {
            return boostFactor;
        }

        public int getFinishing() {
            return finishing;
        }

        public int getTotalScore() {
            return totalScore;
        }
    }

    public static class LevelChartInfo implements Serializable{
        private int currentScore;
        private int remainingScoresToNextLevel;
        private int level;

        public LevelChartInfo(int score) {
            this.level = LevelCalculator.calculateLevel(score);
            this.currentScore = LevelCalculator.calculateOverLevelScore(score);
            this.remainingScoresToNextLevel = LevelCalculator.calculateRemainingScore(score);
        }

        public int getCurrentScore() {
            return currentScore;
        }

        public int getRemainingScoresToNextLevel() {
            return remainingScoresToNextLevel;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class AdInfo implements Serializable{
        private String bannerUrl;
        private double aspectRatio;
        private String clickUrl;


        public AdInfo(String bannerUrl, double aspectRatio, String clickUrl) {
            this.bannerUrl = bannerUrl;
            this.aspectRatio = aspectRatio;
            this.clickUrl = clickUrl;
        }

        public String getBannerUrl() {
            return bannerUrl;
        }

        public double getAspectRatio() {
            return aspectRatio;
        }

        public String getClickUrl() {
            return clickUrl;
        }
    }
}
