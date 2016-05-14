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
import com.google.gson.JsonSyntaxException;
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
    public static final int FINISH_INDEX = 7;
    public static final String IS_ANSWER_CORRECT = "isAnswerCorrect";


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
                URI uri = URI.create("wss://" + matchData.getAddress() + "/ws");
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

    private boolean setCurrentQuestion(Question currentQuestion) {
        questionIndex ++;
        if (questionIndex == FINISH_INDEX){
            answerToQuestion(null, NOT_ANSWERED);
            return false;
        }

        this.currentQuestion = currentQuestion;
        return true;

    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public MatchData getMatchData() {
        return matchData;
    }

    public void answerToQuestion(final Option option, final int remainingTIme) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    e.printStackTrace();
                }
            }
        }).start();
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
        public void onChallengeEventMessage(final ChallengeEventMessage challengeEventMessage) {
            try {
                super.onChallengeEventMessage(challengeEventMessage);
                String message = challengeEventMessage.getMessage();
                JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

                checkAnswerCorrectness(challengeEventMessage.getUserId(), jsonObject);
                checkQuestion(jsonObject);
                checkScores(jsonObject);

            } catch (JsonSyntaxException e) {
                e.printStackTrace();

            }
        }

        private void checkAnswerCorrectness(final String userId, JsonObject jsonObject) {
            if (jsonObject.has(IS_ANSWER_CORRECT)){
                final boolean isAnswerCorrect = jsonObject.get(IS_ANSWER_CORRECT).getAsBoolean();
                if (gameEventHandler != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (matchData.getOwnerUserId().equals(userId)) {
                                gameEventHandler.onAnswer(true, isAnswerCorrect);
                            }else {
                                gameEventHandler.onAnswer(false, isAnswerCorrect);
                            }
                        }
                    });
                }
            }
        }

        private void checkScores(JsonObject jsonObject) {
            if (jsonObject.has(SCORES)){
                Score[] scores = gson.fromJson(jsonObject.get(SCORES), Score[].class);
                Log.i("psycho", "Scores: " + scores);
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

        private void checkQuestion(JsonObject jsonObject) {
            if (jsonObject.has(QUESTION)){
                final Question question = gson.fromJson(jsonObject.get(QUESTION), Question.class);

                if (showQuestion) {
                    if(setCurrentQuestion(question)) {
                        if (gameEventHandler != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gameEventHandler.onNewQuestion(question);
                                }
                            });
                        }
                    }
                }
                showQuestion = !showQuestion;
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
        void onAnswer(boolean isMe, boolean isAnswerCorrect);
    }

    public interface GameStartCallBack {
        void onStart(GameManager manager);
        void onError(Throwable t);
    }
}
