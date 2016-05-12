package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/10/16.
 */
public class MatchMakingExtraMessage {
    @SerializedName("myInfo")
    private ParticipantInfo myInfo;
    @SerializedName("opponentInfo")
    private ParticipantInfo opponentInfo;

    public MatchMakingExtraMessage() {
        myInfo = new ParticipantInfo();
        opponentInfo = new ParticipantInfo();
    }

    public ParticipantInfo getMyInfo() {
        return myInfo;
    }

    public ParticipantInfo getOpponentInfo() {
        return opponentInfo;
    }
}
