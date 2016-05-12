package com.anashidgames.gerdoo.core.service;

import android.util.Log;

import java.util.List;

import ir.pegahtech.backtory.models.messages.BacktoryRealtimeMessage;
import ir.pegahtech.backtory.models.messages.ChallengeChatMessage;
import ir.pegahtech.backtory.models.messages.ChallengeDisconnectMessage;
import ir.pegahtech.backtory.models.messages.ChallengeEndedMessage;
import ir.pegahtech.backtory.models.messages.ChallengeEventMessage;
import ir.pegahtech.backtory.models.messages.ChallengeJoinedMessage;
import ir.pegahtech.backtory.models.messages.ChallengeLeavedMessage;
import ir.pegahtech.backtory.models.messages.ChallengeStartedMessage;
import ir.pegahtech.backtory.models.messages.DirectChatMessage;
import ir.pegahtech.backtory.models.messages.JoinedWebhookMessage;
import ir.pegahtech.backtory.models.messages.MasterMessage;
import ir.pegahtech.backtory.models.messages.StartedWebhookMessage;
import ir.pegahtech.backtory.models.messages.WebhookErrorMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionResponse;
import ir.pegahtech.connectivity.BacktoryRealtimeEventHandler;

/**
 * Created by psycho on 5/9/16.
 */
public class SimpleRealTimeEventHandler implements BacktoryRealtimeEventHandler {
    @Override
    public void onOpen() {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onError(String message) {
        Log.e("psycho", message);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onException(List<ExceptionResponse> exceptions, String exceptionsString) {
        Log.e("psycho", exceptionsString);
        for (ExceptionResponse response : exceptions){
            Log.e("psycho", response.getKey() + " : " + response.getValue());
        }
    }

    @Override
    public void onEvent() {

    }

    @Override
    public void onDirectChat() {

    }

    @Override
    public void onChallengeChat() {

    }

    @Override
    public void onChallengeJoined(ChallengeJoinedMessage joinedMessage) {
        Log.i("psycho", "userId: " + joinedMessage.getUserId() + " joined");
    }

    @Override
    public void onChallengeStarted(ChallengeStartedMessage startedMessage) {
        Log.i("psycho", "challenge started at : " + startedMessage.getStartedDate());
    }

    @Override
    public void onStartedWebhookMessage(StartedWebhookMessage startedMessage) {
        Log.i("psycho", "Started webhook message : " + startedMessage.getMessage());
    }

    @Override
    public void onDirectChatMessage(DirectChatMessage directMessage) {

    }

    @Override
    public void onChallengeEventMessage(ChallengeEventMessage challengeEventMessage) {
        Log.i("psycho", "Challenge event message received : " + challengeEventMessage.getMessage());
    }

    @Override
    public void onChallengeChatMessage(ChallengeChatMessage chatMessage) {

    }

    @Override
    public void onResultReceived(BacktoryRealtimeMessage resultReceived) {
        Log.i("psycho", "result received : " + resultReceived.get_class());
    }

    @Override
    public void onMasterMessage(MasterMessage masterMessage) {

    }

    @Override
    public void onWebhookErrorMessage(WebhookErrorMessage errorMessage) {

    }

    @Override
    public void onJoinedWebhookMessage(JoinedWebhookMessage webhookMessage) {

    }

    @Override
    public void onChallengeEndedMessage(ChallengeEndedMessage endedMessage) {
        Log.i("psycho", "challenge end received : " + endedMessage.getWinners());
    }

    @Override
    public void onChallengeDisconnectMessage(ChallengeDisconnectMessage dcMessage) {

    }

    @Override
    public void onChallengeLeavedMessage(ChallengeLeavedMessage leavedMessage) {

    }
}
