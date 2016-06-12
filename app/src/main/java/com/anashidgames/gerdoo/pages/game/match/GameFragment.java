package com.anashidgames.gerdoo.pages.game.match;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.game.GameActivity;
import com.anashidgames.gerdoo.pages.game.result.ResultFragment;
import com.anashidgames.gerdoo.pages.game.view.OptionView;
import com.anashidgames.gerdoo.pages.game.view.ScoreClock;
import com.anashidgames.gerdoo.utils.PsychoUtils;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 5/8/16.
 */
public class GameFragment extends Fragment {

    public static final String DATA = "data";
    public static final int QUESTION_TIME = 10 * 1000;
    public static final String SCORE = "score";
    public static final int NEXT_ROUND_DELAY = 2000;
    public static final int QURTER_CIRCLE = 90;


    public static Fragment newInstance() {
        return new GameFragment();
    }

    private ImageView myImageView;
    private TextView myNameView;
    private TextView myScoreView;

    private ImageView opponentImageView;
    private TextView opponentNameView;
    private TextView opponentScoreView;

    private TextView questionTextView;
    private ImageView questionImageView;

    private TextView timerBar;
    private LinearLayout optionsLayout;

    private GifImageView hintButton;
    private TextView hintTextView;
    private TextView roundTextView;

    private GameManager gameManager;

    private boolean answered;
    private Question currentQuestion;
    private String selectedOption;

    private List<OptionView> optionsView;
    private TimerAnimation timerAnimation;

    private int remainingTime;
    private int myScore;
    private int opponentScore;

    private boolean finished;

    private List<String> options;
    private boolean gotHint;

    private FlashingAnimation flashingAnimation;
    private ScoreClock scoreClock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        initGameManager();

        initHeader(rootView);
        timerBar = (TextView) rootView.findViewById(R.id.timeBar);
        initQuestionViews(rootView);
        optionsLayout = (LinearLayout) rootView.findViewById(R.id.optionsLayout);
        roundTextView = (TextView) rootView.findViewById(R.id.roundText);
        initHintViews(rootView);
        scoreClock = (ScoreClock) rootView.findViewById(R.id.scoreClock);

        setMatchData(gameManager.getMatchData());
        setScores(0, 0);
        setQuestion(gameManager.getCurrentQuestion(), 0);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timerAnimation != null) {
            timerAnimation.cancel();
            timerAnimation = null;
        }
    }

    private void initGameManager() {
        this.gameManager = ((GameActivity) getActivity()).getGameManager();
        gameManager.setGameEventHandler(new GameEventHandler());
    }


    private void initQuestionViews(View rootView) {
        questionTextView = (TextView) rootView.findViewById(R.id.questionTextView);
        questionImageView = (ImageView) rootView.findViewById(R.id.questionImageView);
    }

    private void initHintViews(View rootView) {
        hintButton = (GifImageView) rootView.findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHint();
            }
        });
        hintTextView = (TextView) rootView.findViewById(R.id.hintTextView);
    }

    private void getHint() {
        if (gotHint || answered)
            return;

        gotHint = true;
        gameManager.getHint();
    }

    private void initHeader(View rootView) {
        myImageView = (ImageView) rootView.findViewById(R.id.myImageView);
        myNameView = (TextView) rootView.findViewById(R.id.myNameView);
        myScoreView = (TextView) rootView.findViewById(R.id.myScoreView);

        opponentImageView = (ImageView) rootView.findViewById(R.id.opponentImageView);
        opponentNameView = (TextView) rootView.findViewById(R.id.opponentNameView);
        opponentScoreView = (TextView) rootView.findViewById(R.id.opponentScoreView);
    }

    private void setMatchData(MatchData data) {
        PsychoImageLoader.loadImage(getActivity(), data.getMyInfo().getImageUrl(), R.drawable.user_image_place_holder, myImageView);
        PsychoImageLoader.loadImage(getActivity(), data.getOpponentInfo().getImageUrl(), R.drawable.user_image_place_holder, opponentImageView);

        myNameView.setText(data.getMyInfo().getName());
        opponentNameView.setText(data.getOpponentInfo().getName());
    }

    private void setQuestion(final Question question, final int questionNumber) {
        stopTimer();
        Runnable setQuestionRunnable = new Runnable() {
            @Override
            public void run() {
                clearPage();

                String roundText = getRoundString(questionNumber);
                roundTextView.setText(roundText);
                AppearAndDisappearAnimation roundTextAnimation = new AppearAndDisappearAnimation(roundTextView);
                roundTextAnimation.setOnFinishedListener(new AppearAndDisappearAnimation.OnFinishedListener() {
                    @Override
                    public void onFinished(View view) {
                        setCurrentQuestion(question);
                    }
                });
                roundTextView.startAnimation(roundTextAnimation);
            }
        };

        if (questionNumber != 0) {
            new Handler().postDelayed(setQuestionRunnable, NEXT_ROUND_DELAY);
        }else {
            setQuestionRunnable.run();
        }
    }

    private String getRoundString(int questionNumber) {
        String numberString = "";
        switch (questionNumber){
            case 0:
                numberString = "اول";
                break;
            case 1:
                numberString = "دوم";
                break;
            case 2:
                numberString = "سوم";
                break;
            case 3:
                numberString = "چهارم";
                break;
            case 4:
                numberString = "پنجم";
                break;
            case 5:
                numberString = "ششم";
                break;
            case 6:
                numberString = "هفتم";
                break;
        }
        String result = getString(R.string.roundText).replace("roundNumber", "" + numberString);
        if (questionNumber == 6){
            result += "\n";
            result += "\n";
            result += getString(R.string.doubleScore);
        }
        return result;
    }

    private void clearPage() {
        questionTextView.setText("");
        questionImageView.setImageDrawable(null);
        optionsLayout.removeAllViews();
        if (flashingAnimation != null) {
            flashingAnimation.cancel();
            flashingAnimation = null;
        }
        opponentImageView.setColorFilter(null);
        stopTimer();
    }



    private void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
        answered = false;
        gotHint = false;
        remainingTime = 10;
        hintButton.setImageResource(R.drawable.hint);
        setQuestionBody(question.getQuestionImageUrl(), question.getQuestionText());
        setOptions(question.getOptions());
        resetTimer();
    }

    private void stopTimer() {
        if (timerAnimation != null){
            timerAnimation.cancel();
            timerAnimation = null;
        }
    }

    private void resetTimer() {
        stopTimer();
        timerAnimation = new TimerAnimation(PsychoUtils.getScreenWidth(getActivity()));
        timerBar.startAnimation(timerAnimation);
    }

    private void setOptions(List<String> options) {
        this.options = options;
        optionsLayout.removeAllViews();
        optionsView = new ArrayList<>();
        if (options == null){
            options = new ArrayList<>();
        }

        for (String option : options){
            OptionView view = new OptionView(getActivity());
            view.setTitle(option);
            view.setOnClickListener(new AnswerQuestion(option));
            optionsView.add(view);
            optionsLayout.addView(view);
        }
    }

    private void setQuestionBody(String imageUrl, String questionText) {
        questionTextView.setText(questionText);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            questionImageView.setVisibility(View.VISIBLE);
            PsychoImageLoader.loadImage(getActivity(), imageUrl, R.drawable.cover_place_holder, questionImageView);

        }else {
            questionImageView.setVisibility(View.GONE);
        }
    }

    private synchronized void setScores(int myScore, int opponentScore) {
        if (myScore > this.myScore) {
            this.myScore = myScore;
        }

        if (opponentScore > this.opponentScore) {
            this.opponentScore = opponentScore;
        }

        myScoreView.setText(getResources().getString(R.string.score).replace(SCORE, "" + this.myScore));
        opponentScoreView.setText(getResources().getString(R.string.score).replace(SCORE, "" + this.opponentScore));
        setScoreClock();
    }

    private void setScoreClock() {
        int totalScores = myScore + opponentScore;
        int angle;
        if (totalScores == 0){
            angle = 0;
        }else {
            angle = (int) ((((myScore - opponentScore)*1.0) / totalScores) * QURTER_CIRCLE);
        }
        scoreClock.setAngle(angle);
    }

    private void showResultPage() {
        finished = true;
        MatchData matchdata = gameManager.getMatchData();
        PlayerData myPlayer = new PlayerData(matchdata.getMyInfo(), myScore);
        PlayerData opponentPlayer = new PlayerData(matchdata.getOpponentInfo(), opponentScore);
        ((FragmentContainerActivity) getActivity()).changeFragment(ResultFragment.newInstance(myPlayer, opponentPlayer, matchdata.getMatchMakingName()));
    }

    private void setAnswer(boolean isMe, boolean isAnswerCorrect) {
        if (isMe){
            OptionView optionView = getOptionView(selectedOption);
            if (optionView != null) {
                if (isAnswerCorrect) {
                    optionView.setStatus(OptionView.STATUS_RIGHT_CORRECT);
                } else {
                    optionView.setStatus(OptionView.STATUS_RIGHT_WRONG);
                }
            }
        }else{

            int colorResource;
            if (isAnswerCorrect){
                colorResource = R.color.green;
            }else {
                colorResource = R.color.red;
            }


            flashingAnimation = new FlashingAnimation(opponentImageView, getResources().getColor(colorResource));
            opponentImageView.startAnimation(flashingAnimation);
        }
    }

    private OptionView getOptionView(String option) {
        if (option != null && options.indexOf(option) >= 0) {
            return optionsView.get(options.indexOf(option));
        }

        return null;
    }

    private class AnswerQuestion implements View.OnClickListener {

        private String option;

        public AnswerQuestion(String option) {
            this.option = option;
        }

        @Override
        public void onClick(View v) {
            if(answered || option == null || currentQuestion == null){
                return;
            }

            answered = true;
            ((OptionView) v).setStatus(OptionView.STATUS_DISABLED);
            selectedOption = option;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        gameManager.answerToQuestion(option, remainingTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        getActivity().finish();
                    }
                }
            }).start();
        }
    }

    private class GameEventHandler implements GameManager.GameEventHandler {
        @Override
        public void onNewQuestion(Question question, int questionIndex) {
            setQuestion(question, questionIndex);
        }

        @Override
        public void onGameFinished() {
            showResultPage();
        }

        @Override
        public void onScore(int myScore, int opponentScore) {
            setScores(myScore, opponentScore);
        }

        @Override
        public void onAnswer(boolean isMe, boolean isAnswerCorrect) {
            setAnswer(isMe, isAnswerCorrect);
        }

        @Override
        public void onHint(List<String> hints) {
            setHint(hints);
        }
    }

    private void setHint(List<String> hints) {
        if (hints == null || hints.isEmpty())
            return;

        hintButton.setImageResource(R.drawable.no_hint);

        for (int i=0; i<options.size(); i++){
            if (hints.contains(options.get(i))){
                optionsView.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    private class TimerAnimation extends Animation {
        private boolean finished;
        private int startWidth;

        public TimerAnimation(int startWidth) {
            this.startWidth = startWidth;
            setDuration(QUESTION_TIME);
            setInterpolator(new LinearInterpolator());
        }


        @Override
        public void cancel() {
            super.cancel();
            finished = true;
            hideTimer();
        }

        private void hideTimer() {
            setBarSize(0);
            timerBar.setVisibility(View.INVISIBLE);
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (finished || GameFragment.this.finished) {
                timerBar.setVisibility(View.INVISIBLE);
                return;
            }

            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime == 0) {
                timerBar.setVisibility(View.VISIBLE);
            }

            interpolatedTime = (1 - interpolatedTime);

            setBarSize(interpolatedTime);
            setTime(interpolatedTime);
            setBarColor(interpolatedTime);

            if (interpolatedTime == 0){
                if(!answered){
                    answered = true;
                    gameManager.notAnswered();
                }

                timerAnimation.cancel();
                timerAnimation = null;
            }
        }

        private void setTime(float interpolatedTime) {
            remainingTime = (int) (interpolatedTime * 10);
            timerBar.setText("" + remainingTime);
        }

        private void setBarSize(float interpolatedTime) {
            timerBar.getLayoutParams().width = (int) (startWidth * interpolatedTime);
            timerBar.requestLayout();
        }

        private void setBarColor(float interpolatedTime) {
            int backgroundColorResource;
            if (interpolatedTime > 0.66){
                backgroundColorResource = R.color.green;
            }else if (interpolatedTime > 0.33){
                backgroundColorResource = R.color.yellow;
            }else {
                backgroundColorResource = R.color.red;
            }
            timerBar.setBackgroundColor(getResources().getColor(backgroundColorResource));
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
