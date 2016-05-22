package ir.pegahtech.connectivity;

import java.util.List;

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
import ir.pegahtech.backtory.models.messages.nested.ExceptionResponse;

/**
 * Created by Mohammad on 5/2/16 AD.
 */
public interface BacktoryRealtimeEventHandler {

    void onOpen();

    void onMessage(String message);

    void onError(String message);

    void onClose();

    void onException(List<ExceptionResponse> exceptions, String exceptionsString);

    void onEvent();

    void onDirectChat();

    void onChallengeChat();

    void onChallengeJoined(ChallengeJoinedMessage joinedMessage);

    void onChallengeStarted(ChallengeStartedMessage startedMessage);

    void onStartedWebhookMessage(StartedWebhookMessage startedMessage);

    void onDirectChatMessage(DirectChatMessage directMessage);

    void onChallengeEventMessage(ChallengeEventMessage challengeEventMessage);

    void onChallengeChatMessage(ChallengeChatMessage chatMessage);

    void onResultReceived(BacktoryRealtimeMessage resultReceived);

    void onMasterMessage(MasterMessage masterMessage);

    void onWebhookErrorMessage(WebhookErrorMessage errorMessage);

    void onJoinedWebhookMessage(JoinedWebhookMessage webhookMessage);

    void onChallengeEndedMessage(ChallengeEndedMessage endedMessage);

    void onChallengeDisconnectMessage(ChallengeDisconnectMessage dcMessage);

    void onChallengeLeavedMessage(ChallengeLeavedMessage leavedMessage);
}
