package ir.pegahtech.backtory.models.messages.nested;

public class ExceptionResponse {
    private String key;
    private String value;

    public ExceptionResponse(String field, String defaultMessage) {
        setKey(field);
        setValue(defaultMessage);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
