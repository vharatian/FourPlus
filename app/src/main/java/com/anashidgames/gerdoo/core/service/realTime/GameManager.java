package com.anashidgames.gerdoo.core.service.realTime;

import android.os.Handler;
import android.os.Looper;

import com.anashidgames.gerdoo.core.service.SimpleRealTimeEventHandler;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import ir.pegahtech.backtory.models.messages.ChallengeEventMessage;
import ir.pegahtech.backtory.models.messages.StartedWebhookMessage;
import ir.pegahtech.connectivity.RealtimeWebSocket;
import ir.pegahtech.connectivity.WebSocketException;

/**
 * Created by psycho on 5/9/16.
 */
public class GameManager {

    public static final String CHALLENGE_ID = "challengeId";
    public static final String QUESTION_NUMBER = "questionNumber";
    public static final String SELECTED_ANSWER = "selectedAnswer";
    public static final String ANSWER_TIME = "answerTime";
    public static final int NEW_QUESTION = 13;


    private final MatchData matchData;
    private final AuthenticationManager authenticationManager;
    private final String instanceId;
    private RealTimeEventHandler realTimeEventHandler;


    private Gson gson;



    private Question currentQuestion;
    private RealtimeWebSocket socket;

    public GameManager(AuthenticationManager authenticationManager, String instanceId, MatchData matchData) {
        this.matchData = matchData;
        this.authenticationManager = authenticationManager;
        this.instanceId = instanceId;
        gson = new Gson();
    }

    public void start(final GameStartCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthenticationInfo info = authenticationManager.checkInfo(true);
                URI uri = URI.create("ws://" + matchData.getAddress() + ":" + matchData.getPort() + "/ws");
                socket = new RealtimeWebSocket(uri, instanceId, info.getAccessToken(), matchData.getChallengeId());
                realTimeEventHandler = new RealTimeEventHandler();
                realTimeEventHandler.setStartCallback(callBack);
                socket.setRealtimeEventHandler(realTimeEventHandler);
                try {
                    socket.connect();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                    callBack.onError(e);
                }
            }
        }).start();
    }

    private void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public MatchData getMatchData() {
        return matchData;
    }

    public void answerToQuestion(int questionNumber, int selectedOption, int answerTime) {
        try {
            JSONObject event = new JSONObject();
            event.put(CHALLENGE_ID, matchData.getChallengeId());
            event.put(QUESTION_NUMBER, questionNumber);
            event.put(SELECTED_ANSWER, selectedOption);
            event.put(ANSWER_TIME, answerTime);
            socket.sendToChallenge(event);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGameEventHandler(GameEventHandler gameEventHandler){
        if (realTimeEventHandler != null){
            realTimeEventHandler.setGameEventHandler(gameEventHandler);
        }
    }

    private class RealTimeEventHandler extends SimpleRealTimeEventHandler {
        private GameStartCallBack startCallback;
        private GameEventHandler gameEventHandler;

        @Override
        public void onStartedWebhookMessage(StartedWebhookMessage startedMessage) {
            super.onStartedWebhookMessage(startedMessage);
            String message = "{}";
            if (startedMessage != null && startedMessage.getMessage() != null && !startedMessage.getMessage().isEmpty()) {
                message = startedMessage.getMessage();
            }

            Question question = new Gson().fromJson(message, Question.class);
            setCurrentQuestion(question);
            startCallback.onStart(GameManager.this);
        }

        @Override
        public void onChallengeEventMessage(ChallengeEventMessage challengeEventMessage) {
            super.onChallengeEventMessage(challengeEventMessage);
            String message = challengeEventMessage.getMessage();
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            JsonElement typeElement = jsonObject.get("type");
            if (typeElement == null)
                return;

            int type = typeElement.getAsInt();
            switch (type){
                case NEW_QUESTION:
                    final Question question = gson.fromJson(message, Question.class);
                    setCurrentQuestion(question);
                    if (gameEventHandler != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameEventHandler.onNewQuestion(question);
                            }
                        });
                    }
                    break;
            }

        }

        private void runOnUiThread(Runnable runnable) {
            if (runnable != null) {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }

        public void setStartCallback(GameStartCallBack startCallback) {
            this.startCallback = startCallback;
        }

        public void setGameEventHandler(GameEventHandler gameEventHandler) {
            this.gameEventHandler = gameEventHandler;
        }
    }

    public interface GameEventHandler {
        void onNewQuestion(Question question);
    }

    public interface GameStartCallBack {
        void onStart(GameManager manager);
        void onError(Throwable t);
    }
}
