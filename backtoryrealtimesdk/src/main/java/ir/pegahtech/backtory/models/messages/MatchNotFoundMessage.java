package ir.pegahtech.backtory.models.messages;

public class MatchNotFoundMessage extends BacktoryConnectivityMessage {
    private String matchmakingName;
    private String requestId;
    private Integer skill;

    public String getMatchmakingName() {
        return matchmakingName;
    }

    public void setMatchmakingName(String matchmakingName) {
        this.matchmakingName = matchmakingName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }
}
