package com.anashidgames.gerdoo.core.service.model.server;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by psycho on 4/24/16.
 */
public class LeaderBoardItem {
    @SerializedName("userBriefProfile")
    private UserBriefProfile userBriefProfile;
    @SerializedName("scores")
    private List<Integer> scores;

    public LeaderBoardItem(String firstName, String lastName, String userId, int scores) {
        this.userBriefProfile = new UserBriefProfile(firstName, lastName, userId);
        this.scores = Arrays.asList(scores);
    }

    public UserBriefProfile getUserBriefProfile() {
        return userBriefProfile;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public static class UserBriefProfile {
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("userId")
        private String userId;

        public UserBriefProfile(String firstName, String lastName, String userId) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUserId() {
            return userId;
        }
    }
}
