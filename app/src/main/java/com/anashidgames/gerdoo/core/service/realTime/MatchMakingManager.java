package com.anashidgames.gerdoo.core.service.realTime;

import android.content.Context;

import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.GetSkillResponse;
import com.anashidgames.gerdoo.core.service.model.MatchData;

import java.net.URI;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchNotFoundMessage;
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
                socket = new ConnectivityWebSocket(URI.create(CONNECTIVITY_URL), instanceId, info.getAccessToken());
                eventHandler = new MatchUserHandler();
                socket.setConnectivityEventHandler(eventHandler);
                try {
                    socket.connect();
                    if (callBack != null) {
                        callBack.onConnected(MatchMakingManager.this);
                    }
                } catch (WebSocketException e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(e);
                    }
                }
            }
        }).start();
    }

    public void matchUser(CallBack<MatchData> callback){
        Call<GetSkillResponse> call = GerdooServer.INSTANCE.getSkill();
        call.enqueue(new SkillCallBack(callback));
    }

    private class SkillCallBack extends CallbackWithErrorDialog<GetSkillResponse> {
        private final CallBack<MatchData> callback;

        public SkillCallBack(CallBack<MatchData> callback) {
            super(context);
            this.callback = callback;
        }

        @Override
        public void handleSuccessful(GetSkillResponse data) {
            final int skill = data.getMyRank();
            eventHandler.setUserMatchedCallback(callback);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socket.matchmakingRequest(matchMakingName, skill);
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
