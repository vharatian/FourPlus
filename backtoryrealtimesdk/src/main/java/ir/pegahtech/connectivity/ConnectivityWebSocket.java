package ir.pegahtech.connectivity;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import ir.pegahtech.backtory.http.GsonHelper;
import ir.pegahtech.backtory.models.messages.BacktoryConnectivityMessage;
import ir.pegahtech.backtory.models.messages.ChatMessage;
import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchNotFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchUpdateMessage;
import ir.pegahtech.backtory.models.messages.MatchmakingCancellationResponseMessage;
import ir.pegahtech.backtory.models.messages.MatchmakingResponseMessage;
import ir.pegahtech.backtory.models.messages.BacktoryRealtimeMessage;
import ir.pegahtech.backtory.models.messages.PushNotifMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionListResponse;

/**
 * Created by Mohammad on 5/2/16 AD.
 */
public class ConnectivityWebSocket extends WebSocket {

    private BacktoryConnectivityEventHandler connectivityEventHandler = null;

    @Override
    protected URI generateURI() throws URISyntaxException {
//        return new URI("wss://" + CommonConfiguration.WS_BASE_URL + "/ws");
        return new URI("wss://" + CommonConfiguration.WS_BASE_URL + "/ws");
    }

    public ConnectivityWebSocket(String connectivityInstanceId, String token) throws URISyntaxException {
        this(null, null, connectivityInstanceId, token);
    }

    public ConnectivityWebSocket(String protocol, String connectivityInstanceId,
                                 String token) throws URISyntaxException {
        this(protocol, null, connectivityInstanceId, token);
    }

    public ConnectivityWebSocket(String protocol, Map<String, String> extraHeaders,
                                 String connectivityInstanceId, String token) throws URISyntaxException {
        super(protocol, extraHeaders, connectivityInstanceId, token);

        setEventHandler(new WebSocketEventHandler() {
            public void onMessage(WebsocketMessage message) {
                String messageText = message.getText();

                if (messageText.equals("")) {
                    Log.i("backtory websocket", "--empty web socket message. maybe ping response");
                    return;
                }

                if (messageText.getBytes()[0] == '\n' && messageText.length() == 1) {
                    Log.i("backtory websocket", "--received rabbit heartbeat");
                    return;
                }

                if (messageText.getBytes()[0] == 3
                        && messageText.getBytes()[1] == -17
                        && messageText.length() == 2) {
                    Log.i("backtory websocket", "--received server dc message");
                    return;
                }

                String[] splitedText = messageText.split("\n\n");
                String[] headers = splitedText[0].split("\n");

                if (headers[0].equals("CONNECTED")) {
                    Log.i("backtory websocket", "--stomp connected: " + headers[1] + " " + headers[2]);
                    return;
                }

                if (headers.length == 3 && headers[0].equals("ERROR")) {
                    Log.i("backtory websocket", "--ERROR: " + headers[1] + " " + headers[2]);
                    return;
                }

                if (splitedText.length == 1) {
                    Log.i("backtory websocket", "--unknown at main: " + messageText);
                    return;
                }

                String body = splitedText[1];

                String command = headers[0];

                if (command.equals("MESSAGE")) {
                    for (String header : headers) {
                        String[] splitedHeader = header.split(":");
                        if (splitedHeader[0].equals("delivery-id")) {
                            send("ACK\n" +
                                    "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                                    "delivery-id:" + splitedHeader[1] + "\n" +
                                    "\n\0");
                        }
                    }

                    body = body.substring(0, body.length() - 1);
                    Gson gson = GsonHelper.getCustomGson();
                    BacktoryRealtimeMessage backtoryRealtimeMessage;
                    try {
                        backtoryRealtimeMessage = gson.fromJson(body, BacktoryRealtimeMessage.class);
                    } catch (JsonParseException e) {
                        Log.i("backtory websocket", "--Json Parse Exception: " + body);
                        return;
                    }

                    String _class = backtoryRealtimeMessage.get_type();
                    if (_class == null) {
                        Log.i("backtory websocket", messageText);
                        getConnectivityEventHandler().onMessage(messageText);
                    } else if (_class.equals(BacktoryConnectivityMessage.PUSH)) {
                        PushNotifMessage pushNotifMessage = gson.fromJson(body, PushNotifMessage.class);
                        getConnectivityEventHandler().onPushMessage(pushNotifMessage);
                    } else if (_class.equals(BacktoryConnectivityMessage.CHAT_MESSAGE)) {
                        ChatMessage chatMessage = gson.fromJson(body, ChatMessage.class);
                        getConnectivityEventHandler().onChatMessage(chatMessage);
                    } else if (_class.equals(BacktoryConnectivityMessage.EXCEPTION)) {
                        ExceptionListResponse response = gson.fromJson(body, ExceptionListResponse.class);
                        getConnectivityEventHandler().onException(response.getExceptions(),
                                response.exceptionsToString());
                    } else if (_class.equals(BacktoryConnectivityMessage.MATCHMAKING_CANCELLATION_RESPONSE)) {
                        MatchmakingCancellationResponseMessage responseMessage =
                                gson.fromJson(body, MatchmakingCancellationResponseMessage.class);
                        requestsList.remove(responseMessage.getRequestId());
                        Log.i("backtory websocket", "--Matchmaking cancellation done for requestId: " +
                                responseMessage.getRequestId());
                        getConnectivityEventHandler().onMatchCancellationResponse(responseMessage.getRequestId());
                    } else if (_class.equals(BacktoryConnectivityMessage.MATCHMAKING_RESPONSE)) {
                        MatchmakingResponseMessage responseMessage =
                                gson.fromJson(body, MatchmakingResponseMessage.class);
                        requestsList.add(responseMessage.getRequestId());
                        Log.i("backtory websocket", "--Matchmaking request sent and id " +
                                responseMessage.getRequestId() + " received");
                        getConnectivityEventHandler().onMatchResponse(responseMessage.getRequestId());
                    } else if (_class.equals(BacktoryConnectivityMessage.MATCH_FOUND_MESSAGE)) {
                        MatchFoundMessage matchFoundMessage =
                                gson.fromJson(body, MatchFoundMessage.class);
                        Log.i("backtory websocket", "--Matchmaking with id " +
                                matchFoundMessage.getRequestId() +
                                " and challenge id " +
                                matchFoundMessage.getRealtimeChallengeId() + " found");

                        if (requestsList.contains(matchFoundMessage.getRequestId())) {
                            getConnectivityEventHandler().onMatchFound(matchFoundMessage);
                        } else {
                            Log.i("backtory websocket", "--Matchmaking for another device of user");
                        }
                    } else if (_class.equals(BacktoryConnectivityMessage.MATCH_NOT_FOUND_MESSAGE)) {
                        MatchNotFoundMessage matchNotFoundMessage =
                                gson.fromJson(body, MatchNotFoundMessage.class);
                        Log.i("backtory websocket", "--Matchmaking with id " +
                                matchNotFoundMessage.getRequestId() + " not found");

                        if (requestsList.contains(matchNotFoundMessage.getRequestId())) {
                            if (getConnectivityEventHandler() != null)
                                getConnectivityEventHandler().onMatchNotFound(matchNotFoundMessage);
                        } else {
                            Log.i("backtory websocket", "--Matchmaking not found for another device of user");
                        }

                    } else if (_class.equals(BacktoryConnectivityMessage.MATCH_UPDATE_MESSAGE)) {
                        MatchUpdateMessage matchUpdateMessage =
                                gson.fromJson(body, MatchUpdateMessage.class);
                        Log.i("backtory websocket", "--Matchmaking with id "
                                + matchUpdateMessage.getRequestId() + " updated");

                        getConnectivityEventHandler().onMatchUpdate(matchUpdateMessage);
                    } else {
                        Log.i("backtory websocket", "--received message: " + body);
                        System.out.println("--received message: " + body);

                        getConnectivityEventHandler().onMessage(body);
                    }
                } else {
                    Log.i("backtory websocket", "--received message: " + body);
                    System.out.println("--received message: " + body);
                    // ToDo: what to do with these messages?
                }
            }

            public void onOpen() {
                send("CONNECT\n" +
                        "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                        "receipt:77\n" +
                        "\n\0");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(getHeartBeat() * 1000);
                                send("STOMP\n" +
                                        "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                                        "\n\0");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (WebSocketException e) {
                                Log.i("backtory websocket", "--no connected web socket");
                                break;
                            }
                        }
                    }
                }).start();

                Log.i("backtory websocket", "--open");

                getConnectivityEventHandler().onOpen();
            }

            public void onError(IOException exception) {
                Log.i("backtory websocket", "--error");
                getConnectivityEventHandler().onError(exception.getMessage());
            }

            public void onClose() {
                Log.i("backtory websocket", "--close");
                getConnectivityEventHandler().onClose();
            }
        });

    }

    public BacktoryConnectivityEventHandler getConnectivityEventHandler() {
        return connectivityEventHandler;
    }

    public void setConnectivityEventHandler(BacktoryConnectivityEventHandler connectivityEventHandler) {
        this.connectivityEventHandler = connectivityEventHandler;
    }

    public void matchmakingRequest(String matchmakingName, int skill) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchmakingName", matchmakingName);
            jsonObject.put("skill", skill);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        send("SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/MatchmakingRequest" + "\n\n" +
                jsonObject.toString()
                + "\n\0");
    }

    public void matchmakingCancellationRequest(String matchmakingName, String requestId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchmakingName", matchmakingName);
            jsonObject.put("requestId", requestId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        send("SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/MatchmakingCancellation" + "\n\n" +
                jsonObject.toString()
                + "\n\0");
    }
}
