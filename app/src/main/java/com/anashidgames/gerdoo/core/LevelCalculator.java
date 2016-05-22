package com.anashidgames.gerdoo.core;

/**
 * Created by psycho on 5/20/16.
 */
public class LevelCalculator {
    public static int calculateLevel(int score){
        int level;
        for (level = 0; score >= levelScore(level + 1); level++);

        return level;
    }

    public static int calculateRemainingScore(int score){
        int level = calculateLevel(score);
        return levelScore(level + 1) - score;
    }

    public static int calculateOverLevelScore(int score){
        int level = calculateLevel(score);
        return score - levelScore(level);
    }

    private static int levelScore(int level){
        if (level == 0)
            return 0;

        return 50 * level * (level+1) + 100;
    }
}
