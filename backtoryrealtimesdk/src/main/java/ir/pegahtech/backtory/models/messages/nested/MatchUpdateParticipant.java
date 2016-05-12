package ir.pegahtech.backtory.models.messages.nested;

public class MatchUpdateParticipant {
    private String userId;
    private Integer skill;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }
}
