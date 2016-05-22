package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 5/10/16.
 */
public class Question {
    @SerializedName("questionId")
    private String questionId;
    @SerializedName("questionText")
    private String questionText;
    @SerializedName("questionImageUrl")
    private String questionImageUrl;
    @SerializedName("options")
    private List<String> options = new ArrayList<>();

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getQuestionId() {
        return questionId;
    }

}
