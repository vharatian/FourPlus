package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class ChatMessage extends BacktoryConnectivityMessage {

    private String message;
    private String senderId;
    private long date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
