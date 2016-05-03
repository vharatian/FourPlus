package com.anashidgames.gerdoo.pages.game;

import java.io.Serializable;
import java.util.List;

/**
 * Created by psycho on 5/1/16.
 */
public class ScorePageInfo implements Serializable{
    private Scores scores;
    private LevelChartInfo levelChartInfo;
    private AdInfo adInfo;
    private List<String> questionsImage;

    public ScorePageInfo(Scores scores, LevelChartInfo levelChartInfo, AdInfo adInfo, List<String> questionsImage) {
        this.scores = scores;
        this.levelChartInfo = levelChartInfo;
        this.adInfo = adInfo;
        this.questionsImage = questionsImage;
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

    public List<String> getQuestionsImage() {
        return questionsImage;
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
        private int existingScore;
        private int thisRoundScore;
        private int remainingScoresToNextLevel;

        public LevelChartInfo(int existingScore, int thisRoundScore, int remainingScoresToNextLevel) {
            this.existingScore = existingScore;
            this.thisRoundScore = thisRoundScore;
            this.remainingScoresToNextLevel = remainingScoresToNextLevel;
        }

        public int getExistingScore() {
            return existingScore;
        }

        public int getThisRoundScore() {
            return thisRoundScore;
        }

        public int getRemainingScoresToNextLevel() {
            return remainingScoresToNextLevel;
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
