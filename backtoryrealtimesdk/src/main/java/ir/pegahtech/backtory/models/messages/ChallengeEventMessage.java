package ir.pegahtech.backtory.models.messages;

import com.google.gson.Gson;

import ir.pegahtech.backtory.http.GsonHelper;
import ir.pegahtech.connectivity.WebSocketException;

/**
 * Created by Mohammad on 5/4/16 AD.
 */
public class ChallengeEventMessage extends BacktoryRealtimeMessage {
    private String message;
    private String userId;

    public String getRawMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public <T> T getConverted(Class<T> cls) throws Exception{
        Gson gson = GsonHelper.getCustomGson();
        T result;
        try {
            result = gson.fromJson(message, cls);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
}
