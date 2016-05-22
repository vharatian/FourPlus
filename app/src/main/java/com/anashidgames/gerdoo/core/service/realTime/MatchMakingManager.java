package com.anashidgames.gerdoo.core.service.realTime;

import android.content.Context;

import com.anashidgames.gerdoo.core.LevelCalculator;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.GetSkillResponse;
import com.anashidgames.gerdoo.core.service.model.MatchData;

import java.util.List;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchNotFoundMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionResponse;
import ir.pegahtech.connectivity.ConnectivityWebSocket;
import ir.pegahtech.connectivity.WebSocketException;
import retrofit2.Call;

/**
 * Created by psycho on 5/9/16.
 */
public class MatchMakingManager {


    public static final String CONNECTIVITY_URL = "wss://ws.backtory.com/connectivity/ws";

    private ConnectivityWebSocket socket;
    private AuthenticationManager authenticationManager;

    private final String instanceId;

    private Context context;
    private MatchUserHandler eventHandler;
    private final String matchMakingName;

    private String requestId;
    private int score;

    public MatchMakingManager(Context context, AuthenticationManager authenticationManager, String instanceId, String matchMakingName) {
        this.context = context;
        this.instanceId = instanceId;
        this.authenticationManager = authenticationManager;
        this.matchMakingName = matchMakingName;
    }

    public void connect(final MatchMakingConnectionCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthenticationInfo info = authenticationManager.checkInfo(true);
                try {
                    socket = new ConnectivityWebSocket(instanceId, info.getAccessToken());
                    eventHandler = new MatchUserHandler();
                    socket.setConnectivityEventHandler(eventHandler);
                    socket.connect();
                    if (callBack != null) {
                        callBack.onConnected(MatchMakingManager.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(e);
                    }
                }
            }
        }).start();
    }

    public void matchUser(CallBack<MatchData> callback){
        Call<GetSkillResponse> call = GerdooServer.INSTANCE.getScore(matchMakingName);
        call.enqueue(new SkillCallBack(callback));
    }

    public void close() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.matchmakingCancellationRequest(matchMakingName, requestId);
                    socket.close();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getScore() {
        return score;
    }

    private class SkillCallBack extends CallbackWithErrorDialog<GetSkillResponse> {
        private final CallBack<MatchData> callback;

        public SkillCallBack(CallBack<MatchData> callback) {
            super(context);
            this.callback = callback;
        }

        @Override
        public void handleSuccessful(GetSkillResponse data) {
            score = data.getMyScore();

            eventHandler.setUserMatchedCallback(callback);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socket.matchmakingRequest(matchMakingName, LevelCalculator.calculateLevel(score));
                }
            }).start();

        }
    }

    private class MatchUserHandler extends SimpleConnectivityEventHandler {
        private CallBack<MatchData> userMatchCallBack;

        public MatchUserHandler() {
        }

        public void setUserMatchedCallback(CallBack<MatchData> callback) {
            this.userMatchCallBack = callback;
        }

        @Override
        public void onMatchFound(MatchFoundMessage matchFoundMessage) {
            super.onMatchFound(matchFoundMessage);
            if (userMatchCallBack != null) {
                userMatchCallBack.onData(new MatchData(matchFoundMessage));
            }
        }

        @Override
        public void onMatchNotFound(MatchNotFoundMessage matchNotFoundMessage) {
            super.onMatchNotFound(matchNotFoundMessage);
            if (userMatchCallBack != null){
                userMatchCallBack.onFailure("Match not Found", null);
            }
        }

        @Override
        public void onMatchResponse(String requestId) {
            super.onMatchResponse(requestId);
            MatchMakingManager.this.requestId = requestId;
        }

        @Override
        public void onException(List<ExceptionResponse> exceptions, String exceptionsString) {
            super.onException(exceptions, exceptionsString);
            if (userMatchCallBack != null){
                userMatchCallBack.onFailure(exceptionsString, null);
            }
        }

        @Override
        public void onError(String message) {
            super.onError(message);
            if (userMatchCallBack != null){
                userMatchCallBack.onFailure(message, null);
            }
        }


    }

    private class MatchDataCallBack extends CallbackWithErrorDialog<MatchData> {
        private final CallBack<MatchData> callback;

        public MatchDataCallBack(CallBack<MatchData> callback) {
            super(context);
            this.callback = callback;
        }

        @Override
        public void handleSuccessful(MatchData data) {
            callback.onData(data);
        }
    }

    public interface MatchMakingConnectionCallBack {
        void onConnected(MatchMakingManager manager);
        void onError(Throwable t);
    }
}
