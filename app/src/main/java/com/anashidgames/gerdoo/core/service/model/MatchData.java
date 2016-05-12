package com.anashidgames.gerdoo.core.service.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;

/**
 * Created by psycho on 5/9/16.
 */
public class MatchData implements Serializable{
    @SerializedName("myInfo")
    private ParticipantInfo myInfo;
    @SerializedName("opponentInfo")
    private ParticipantInfo opponentInfo;
    @SerializedName("challengeId")
    private String challengeId;
    @SerializedName("address")
    private String address;
    @SerializedName("port")
    private int port;
    @SerializedName("requestId")
    private String requestId;

    private String ownerUserId;

    public MatchData(MatchFoundMessage message) {
        this.challengeId = message.getRealtimeChallengeId();
        this.port = message.getPort();
        this.requestId = message.getRequestId();
        this.address = message.getAddress();
        this.ownerUserId = message.getOwnerUserId();

        String extraMessage = message.getExtraMessage();
        Log.i("psycho", "extraMessage : " + extraMessage);
        if (extraMessage == null || extraMessage.isEmpty()) {
            extraMessage = "[]";
        }
        List<ParticipantInfo> participantList = new Gson().fromJson(extraMessage,
                new TypeToken<ArrayList<ParticipantInfo>>() {
                }.getType());

        if (participantList != null){
            for(ParticipantInfo info : participantList){
                if (info.getUserId() != null && info.getUserId().equals(ownerUserId)){
                    myInfo = info;
                }else {
                    opponentInfo = info;
                }
            }
        }

        if (myInfo == null) {
            myInfo = new ParticipantInfo();
        }

        if (opponentInfo == null){
            opponentInfo = new ParticipantInfo();
        }
    }


    public ParticipantInfo getMyInfo() {
        return myInfo;
    }

    public ParticipantInfo getOpponentInfo() {
        return opponentInfo;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }
}
