package ir.pegahtech.connectivity;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import ir.pegahtech.backtory.http.GsonHelper;
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
import ir.pegahtech.backtory.models.messages.BacktoryRealtimeMessage;
import ir.pegahtech.backtory.models.messages.WebhookErrorMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionListResponse;

/**
 * Created by Mohammad on 5/2/16 AD.
 */
public class RealtimeWebSocket extends WebSocket {

    private String realtimeChallengeId;
    private BacktoryRealtimeEventHandler realtimeEventHandler;

    public RealtimeWebSocket(URI url, String connectivityInstanceId, String token, String realtimeChallengeId) {
        this(url, null, null, connectivityInstanceId, token, realtimeChallengeId);
    }

    public RealtimeWebSocket(URI url, String protocol, String connectivityInstanceId, String token, String realtimeChallengeId) {
        this(url, protocol, null, connectivityInstanceId, token, realtimeChallengeId);
    }

    public RealtimeWebSocket(URI url, String protocol, Map<String, String> extraHeaders, String connectivityInstanceId, String token, String realtimeChallengeId) {
        super(url, protocol, extraHeaders, connectivityInstanceId, token);
        setRealtimeChallengeId(realtimeChallengeId);

        setEventHandler(new WebSocketEventHandler() {
            public void onMessage(WebsocketMessage message) {
                String messageText = message.getText();
                if (messageText.equals("")) {
                    Log.i("backtory websocket", "--empty web socket message");
                    return;
                }

                if (messageText.getBytes()[0] == '\n' && messageText.length() == 1) {
                    Log.i("backtory websocket", "--received empty message");
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
                    Log.i("backtory websocket", "--unknown at realtime: " + messageText);
                    return;
                }

                String body = splitedText[1];

                String command = headers[0];

                if (command.equals("MESSAGE")) {
                    body = body.substring(0, body.length() - 1);
                    Gson gson = GsonHelper.getCustomGson();
                    BacktoryRealtimeMessage backtoryRealtimeMessage = null;
                    try {
                        backtoryRealtimeMessage = gson.fromJson(body, BacktoryRealtimeMessage.class);
                    } catch (JsonParseException e) {
                        Log.i("backtory websocket", "--Json Parse Exception: " + body);
                        return;
                    }

                    String _class = backtoryRealtimeMessage.get_class();
                    if (_class == null) {
                        Log.i("backtory websocket", messageText);
                        getRealtimeEventHandler().onMessage(messageText);
                    } else if (_class.equals(".Exception")) {
                        ExceptionListResponse response =
                                gson.fromJson(body, ExceptionListResponse.class);
                        Log.i("backtory websocket", "--exception: " + response.exceptionsToString());
                        getRealtimeEventHandler().onException(response.getExceptions(),
                                response.exceptionsToString());
                    } else if (_class.equals(BacktoryRealtimeMessage.DIRECT)) {
                        DirectChatMessage directMessage = gson.fromJson(body, DirectChatMessage.class);
                        getRealtimeEventHandler().onDirectChatMessage(directMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE)) {
                        ChallengeEventMessage challengeEventMessage = gson.fromJson(body, ChallengeEventMessage.class);
                        getRealtimeEventHandler().onChallengeEventMessage(challengeEventMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHAT)) {
                        ChallengeChatMessage chatMessage = gson.fromJson(body, ChallengeChatMessage.class);
                        getRealtimeEventHandler().onChallengeChatMessage(chatMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE_JOINED)) {
                        ChallengeJoinedMessage joinedMessage = gson.fromJson(body, ChallengeJoinedMessage.class);
                        getRealtimeEventHandler().onChallengeJoined(joinedMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.RESPONSE_RECEIVED)) {
                        BacktoryRealtimeMessage resultReceived = gson.fromJson(body, BacktoryRealtimeMessage.class);
                        getRealtimeEventHandler().onResultReceived(resultReceived);
                    } else if (_class.equals(BacktoryRealtimeMessage.MASTER)) {
                        MasterMessage masterMessage = gson.fromJson(body, MasterMessage.class);
                        getRealtimeEventHandler().onMasterMessage(masterMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.JOINED_WEBHOOK)) {
                        JoinedWebhookMessage webhookMessage = gson.fromJson(body, JoinedWebhookMessage.class);
                        getRealtimeEventHandler().onJoinedWebhookMessage(webhookMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE_ENDED)) {
                        ChallengeEndedMessage endedMessage = gson.fromJson(body, ChallengeEndedMessage.class);
                        getRealtimeEventHandler().onChallengeEndedMessage(endedMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE_LEAVED)) {
                        ChallengeLeavedMessage leavedMessage = gson.fromJson(body, ChallengeLeavedMessage.class);
                        getRealtimeEventHandler().onChallengeLeavedMessage(leavedMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE_DISCONNECT)) {
                        ChallengeDisconnectMessage dcMessage = gson.fromJson(body, ChallengeDisconnectMessage.class);
                        getRealtimeEventHandler().onChallengeDisconnectMessage(dcMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.WEBHOOK_ERROR)) {
                        WebhookErrorMessage errorMessage = gson.fromJson(body, WebhookErrorMessage.class);
                        getRealtimeEventHandler().onWebhookErrorMessage(errorMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.CHALLENGE_STARTED)) {
                        ChallengeStartedMessage startedMessage = gson.fromJson(body, ChallengeStartedMessage.class);
                        getRealtimeEventHandler().onChallengeStarted(startedMessage);
                    } else if (_class.equals(BacktoryRealtimeMessage.STARTED_WEBHOOK)) {
                        StartedWebhookMessage startedMessage = gson.fromJson(body, StartedWebhookMessage.class);
                        getRealtimeEventHandler().onStartedWebhookMessage(startedMessage);
                    } else {
                        Log.i("backtory websocket", "--received message: " + body);
                        System.out.println("--received message: " + body);
                        getRealtimeEventHandler().onMessage(body);
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
                        "X-Backtory-Challenge-Id:" + getRealtimeChallengeId() + "\n" +
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
            }

            public void onError(IOException exception) {
                Log.i("backtory websocket", "--error");
                getRealtimeEventHandler().onError(exception.getMessage());
            }

            public void onClose() {
                Log.i("backtory websocket", "--close");
                getRealtimeEventHandler().onClose();
            }
        });
    }

    public String getRealtimeChallengeId() {
        return realtimeChallengeId;
    }

    public void setRealtimeChallengeId(String realtimeChallengeId) {
        this.realtimeChallengeId = realtimeChallengeId;
    }

    public BacktoryRealtimeEventHandler getRealtimeEventHandler() {
        return realtimeEventHandler;
    }

    public void setRealtimeEventHandler(BacktoryRealtimeEventHandler realtimeEventHandler) {
        this.realtimeEventHandler = realtimeEventHandler;
    }

    public void sendToChallenge(JSONObject jsonObject) {
        String data = "SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/challenge." + realtimeChallengeId + "\n\n" +
                "{\"message\":" + jsonObject.toString() + "}" + "\n\0";
        send(data);
    }

    public void sendToChallenge(String message) {
        String data = "SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/challenge." + realtimeChallengeId + "\n\n" +
                "{\"message\":\"" + message + "\"}" + "\n\0";
        send(data);
    }

    public void sendDirectToUser(String userId, String message) {
        send("SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "X-Backtory-Challenge-Id:" + realtimeChallengeId + "\n" +
                "destination:" + "/app/direct." + userId + "\n\n" +
                "{\"message\":\"" + message + "\"}" + "\n\0");
    }

    public void sendChatToChallenge(String message) {
        send("SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/chat." + realtimeChallengeId + "\n\n" +
                "{\"message\":\"" + message + "\"}" + "\n\0");
    }

    public void sendChallengeResult(List<String> winnersList) {
        JSONArray winners = new JSONArray();
        for (String winner : winnersList) {
            winners.put(winner);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("winners", winners);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        send("SEND\n" +
                "X-Backtory-Connectivity-Id:" + getInstanceId() + "\n" +
                "destination:" + "/app/challenge.result." + realtimeChallengeId + "\n\n" +
                jsonObject.toString() +
                "\n\0");
    }
}
