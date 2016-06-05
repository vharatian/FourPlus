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
import java.util.Arrays;
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
    public static final String CORRECT_ANSWERS = "correctAnswers";
    public static final String TYPE = "type";
    public static final int HINT_TYPE = 1;
    public static final int ANSWER_TYPE = 0;
    public static final String HINTS = "hints";


    private final MatchData matchData;
    private final AuthenticationManager authenticationManager;
    private final String instanceId;
    private RealTimeEventHandler realTimeEventHandler;


    private Gson gson;
    private RealtimeWebSocket socket;

    private List<Question> questions = new ArrayList<>();
    private List<String> myAnswers = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private int myScore;
    private int opponentScore;

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

                try {
                    socket = new RealtimeWebSocket(matchData.getAddress(), instanceId, info.getAccessToken(), matchData.getChallengeId());
                    realTimeEventHandler = new RealTimeEventHandler();
                    realTimeEventHandler.setStartCallback(callBack);
                    socket.setRealtimeEventHandler(realTimeEventHandler);
                    socket.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(e);
                    }
                }
            }
        }).start();
    }

    private boolean setCurrentQuestion(Question currentQuestion) {
        questions.add(currentQuestion);
        if (getQuestionIndex() >= FINISH_INDEX){
            answerToQuestion(null, NOT_ANSWERED);
            return false;
        }

        return true;
    }

    public List<String> getMyAnswers() {
        return myAnswers;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getCurrentQuestion() {
        return questions.get(getQuestionIndex());
    }

    private int getQuestionIndex() {
        return questions.size() - 1;
    }

    public MatchData getMatchData() {
        return matchData;
    }

    public void answerToQuestion(final String option, final int remainingTIme) {
        myAnswers.add(option);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject event = new JSONObject();
                    event.put(CHALLENGE_ID, matchData.getChallengeId());
                    event.put(QUESTION_NUMBER, getAnswerIndex());
                    event.put(TYPE, ANSWER_TYPE);
                    event.put(SELECTED_ANSWER, option);
                    event.put(REMAINING_TIME, remainingTIme);
                    Log.i("psycho", "sending message: " + event.toString());
                    socket.sendToChallenge(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int getAnswerIndex() {
        return myAnswers.size()-1;
    }

    public void setGameEventHandler(GameEventHandler gameEventHandler){
        if (realTimeEventHandler != null){
            realTimeEventHandler.setGameEventHandler(gameEventHandler);
        }
    }

    public void notAnswered() {
        answerToQuestion(null, -1);
    }

    public void close() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getMyScore() {
        return myScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void getHint() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject event = new JSONObject();
                    event.put(CHALLENGE_ID, matchData.getChallengeId());
                    event.put(QUESTION_NUMBER, getQuestionIndex());
                    event.put(TYPE, HINT_TYPE);
                    Log.i("psycho", "sending message: " + event.toString());
                    socket.sendToChallenge(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean completed() {
        return getQuestionIndex() >= 7;
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
                String message = challengeEventMessage.getRawMessage();
                JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

                checkAnswerCorrectness(challengeEventMessage.getUserId(), jsonObject);
                checkQuestion(jsonObject);
                checkScores(challengeEventMessage.getUserId(), jsonObject);
                checkCorrectAnswers(jsonObject);
                checkHint(jsonObject);

            } catch (JsonSyntaxException e) {
                e.printStackTrace();

            }
        }

        private void checkHint(JsonObject jsonObject) {
            if (jsonObject.has(HINTS)){
                final List<String> hints = Arrays.asList(gson.fromJson(jsonObject.get(HINTS), String[].class));

                if (gameEventHandler != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameEventHandler.onHint(hints);
                        }
                    });
                }
            }
        }

        private void checkCorrectAnswers(JsonObject jsonObject) {
            if (jsonObject.has(CORRECT_ANSWERS)){
                correctAnswers = Arrays.asList(gson.fromJson(jsonObject.get(CORRECT_ANSWERS), String[].class));
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

        private void checkScores(String senderUserId, JsonObject jsonObject) {
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

                if (myScore > GameManager.this.myScore){
                    GameManager.this.myScore = myScore;
                }

                if (opponentScore > GameManager.this.opponentScore){
                    GameManager.this.opponentScore = opponentScore;
                }

                if (getQuestionIndex() >= 6){
                    if (myUserId.equals(senderUserId)){
                        myScore -= 20;
                    }else{
                        opponentScore -= 20;
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
                                    gameEventHandler.onNewQuestion(question, getQuestionIndex());
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
        void onNewQuestion(Question question, int questionIndex);
        void onGameFinished();
        void onScore(int myScore, int opponentScore);
        void onAnswer(boolean isMe, boolean isAnswerCorrect);
        void onHint(List<String> hints);
    }

    public interface GameStartCallBack {
        void onStart(GameManager manager);
        void onError(Throwable t);
    }
}
