package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 5/20/16.
 */
public class MatchHistoryItem {
    private Question question;
    private String correctAnswer;
    private String myAnswer;


    public MatchHistoryItem(Question question, String correctAnswer, String myAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.myAnswer = myAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getMyAnswer() {
        return myAnswer;
    }
}
