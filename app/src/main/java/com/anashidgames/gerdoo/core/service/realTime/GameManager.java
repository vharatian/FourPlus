package com.anashidgames.gerdoo.core.service.realTime;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anashidgames.gerdoo.core.service.PsychoRunnable;
import com.anashidgames.gerdoo.core.service.SimpleRealTimeEventHandler;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Option;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.model.Score;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ir.pegahtech.backtory.models.messages.BacktoryRealtimeMessage;
import ir.pegahtech.backtory.models.messages.ChallengeEndedMessage;
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
    public static final String REMAINING_TIME = "remainingTime";
    public static final String QUESTION = "question";
    public static final int NOT_ANSWERED = -1;
    public static final String SCORES = "scores";


    private final MatchData matchData;
    private final AuthenticationManager authenticationManager;
    private final String instanceId;
    private RealTimeEventHandler realTimeEventHandler;


    private Gson gson;



    private Question currentQuestion;
    private RealtimeWebSocket socket;
    private int questionIndex = -1;

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
                URI uri = URI.create("ws://" + matchData.getAddress() + "/ws");
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
        questionIndex ++;

    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public MatchData getMatchData() {
        return matchData;
    }

    public void answerToQuestion(Option option, int remainingTIme) {
        try {
            int optionIndex = NOT_ANSWERED;
            if (option != null){
                optionIndex = 1 + currentQuestion.getOptions().indexOf(option);
            }

            JSONObject event = new JSONObject();
            event.put(CHALLENGE_ID, matchData.getChallengeId());
            event.put(QUESTION_NUMBER, questionIndex);
            event.put(SELECTED_ANSWER, optionIndex);
            event.put(REMAINING_TIME, remainingTIme);
            Log.i("psycho", "sending message: " + event.toString());
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

    public void notAnswered() {
        answerToQuestion(null, -1);
    }

    private class RealTimeEventHandler extends SimpleRealTimeEventHandler {
        private GameStartCallBack startCallback;
        private GameEventHandler gameEventHandler;
        private boolean showQuestion = false;

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
            if (jsonObject.has(QUESTION)){
                final Question question = gson.fromJson(jsonObject.get(QUESTION), Question.class);

                if (showQuestion) {
                    setCurrentQuestion(question);
                    if (gameEventHandler != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameEventHandler.onNewQuestion(question);
                            }
                        });
                    }
                }
                showQuestion = !showQuestion;
            }

            if (jsonObject.has(SCORES)){
                Score[] scores = gson.fromJson(jsonObject.get(SCORES), Score[].class);
                int myScore = 0, opponentScore = 0;
                String myUserId = matchData.getOwnerUserId();
                if (scores != null) {
                    for (Score score : scores) {
                        if (score.getUserId().equals(myUserId)){
                            myScore = score.getScore();
                        }else {
                            opponentScore = score.getScore();
                        }
                    }
                }

                if (gameEventHandler != null){
                    runOnUiThread(new PsychoRunnable<Integer>(myScore, opponentScore) {
                        @Override
                        public void run(Integer[] inputs) {
                            gameEventHandler.onScore(inputs[0], inputs[1]);
                        }
                    });
                }

            }
        }

        @Override
        public void onChallengeEndedMessage(ChallengeEndedMessage endedMessage) {
            super.onChallengeEndedMessage(endedMessage);
            if (gameEventHandler != null){
                gameEventHandler.onGameFinished();
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
        void onGameFinished();
        void onScore(int myScore, int opponentScore);
    }

    public interface GameStartCallBack {
        void onStart(GameManager manager);
        void onError(Throwable t);
    }
}
