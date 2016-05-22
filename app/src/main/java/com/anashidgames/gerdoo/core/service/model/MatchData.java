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
    private ParticipantInfo myInfo;
    private ParticipantInfo opponentInfo;
    private String challengeId;
    private String address;
    private int port;
    private String requestId;
    private String ownerUserId;
    private String matchMakingName;

    public MatchData(MatchFoundMessage message) {
        this.challengeId = message.getRealtimeChallengeId();
        this.port = message.getPort();
        this.requestId = message.getRequestId();
        this.address = message.getAddress();
        this.ownerUserId = message.getOwnerUserId();
        this.matchMakingName = message.getMatchmakingName();

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

    public String getMatchMakingName() {
        return matchMakingName;
    }
}
