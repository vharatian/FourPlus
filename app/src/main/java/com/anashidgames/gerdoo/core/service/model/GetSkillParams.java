package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 5/9/16.
 */
public class GetSkillParams {
    private String userId;

    public GetSkillParams(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
