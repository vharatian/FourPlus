package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/9/16.
 */
public class GetSkillResponse {
    @SerializedName("mySkill")
    private int mySkill;

    public GetSkillResponse(int mySkill) {
        this.mySkill = mySkill;
    }

    public int getMyScore() {
        return mySkill;
    }
}
