package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 5/10/16.
 */
public class AnswerToQuestionEvent {
    private String challengeId;
    private String questionId;
    private String optionId;
    private int answeringTime;

    public AnswerToQuestionEvent(String challengeId, String questionId, String optionId, int answeringTime) {
        this.challengeId = challengeId;
        this.questionId = questionId;
        this.optionId = optionId;
        this.answeringTime = answeringTime;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getOptionId() {
        return optionId;
    }

    public int getAnsweringTime() {
        return answeringTime;
    }
}
