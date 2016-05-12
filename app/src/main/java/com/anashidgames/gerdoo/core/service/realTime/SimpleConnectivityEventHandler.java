package com.anashidgames.gerdoo.core.service.realTime;

import android.util.Log;

import java.util.List;

import ir.pegahtech.backtory.models.messages.ChatMessage;
import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchNotFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchUpdateMessage;
import ir.pegahtech.backtory.models.messages.PushNotifMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionResponse;
import ir.pegahtech.connectivity.BacktoryConnectivityEventHandler;

/**
 * Created by psycho on 5/9/16.
 */
public class SimpleConnectivityEventHandler implements BacktoryConnectivityEventHandler {
    @Override
    public void onMatchFound(MatchFoundMessage matchFoundMessage) {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onException(List<ExceptionResponse> exceptions, String exceptionsString) {
        Log.e("psycho", exceptionsString);
        for (ExceptionResponse response : exceptions){
            Log.e("psycho", response.getKey() + " : " + response.getValue());
        }
    }

    @Override
    public void onError(String message) {
        Log.e("psycho", message);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onMatchNotFound(MatchNotFoundMessage matchNotFoundMessage) {

    }

    @Override
    public void onMatchUpdate(MatchUpdateMessage matchUpdateMessage) {

    }

    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPushMessage(PushNotifMessage pushNotifMessage) {

    }
}
