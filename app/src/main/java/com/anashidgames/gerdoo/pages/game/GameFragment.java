package com.anashidgames.gerdoo.pages.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.util.Log;
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
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Option;
import com.anashidgames.gerdoo.core.service.model.ParticipantInfo;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.game.view.OptionView;
import com.anashidgames.gerdoo.utils.PsychoUtils;
import com.bumptech.glide.Glide;

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

    private TextView timeBar;
    private LinearLayout optionsLayout;

    private GifImageView hintButton;
    private TextView hintTextView;

    private GameManager gameManager;

    private boolean answered;
    private Question currentQuestion;
    private Option selectedOption;

    private List<OptionView> optionsView;
    private TimerAnimation timerAnimation;

    private int remainingTime;
    private int myScore;
    private int opponentScore;

    private boolean finished;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        initGameManager();

        initHeader(rootView);
        timeBar = (TextView) rootView.findViewById(R.id.timeBar);
        initQuestionViews(rootView);
        optionsLayout = (LinearLayout) rootView.findViewById(R.id.optionsLayout);
        initHintViews(rootView);

        setMatchData(gameManager.getMatchData());
        setScores(0, 0);
        setCurrentQuestion(gameManager.getCurrentQuestion());

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
        hintTextView = (TextView) rootView.findViewById(R.id.hintTextView);
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
        Glide.with(getActivity()).load(data.getMyInfo().getImageUrl()).placeholder(R.drawable.user_image_place_holder).into(myImageView);
        Glide.with(getActivity()).load(data.getOpponentInfo().getImageUrl()).placeholder(R.drawable.user_image_place_holder).into(opponentImageView);

        myNameView.setText(data.getMyInfo().getName());
        opponentNameView.setText(data.getOpponentInfo().getName());
    }

    private void setCurrentQuestion(Question question) {
        this.currentQuestion = question;
        answered = false;
        remainingTime = 10;
        setQuestionBody(question.getQuestionImageUrl(), question.getQuestionText());
        setOptions(question.getOptions());
        resetTimer();
    }

    private void resetTimer() {
        if (timerAnimation != null){
            timerAnimation.cancel();
        }


        timerAnimation = new TimerAnimation(PsychoUtils.getScreenWidth(getActivity()));
        timeBar.startAnimation(timerAnimation);
    }

    private void setOptions(List<Option> options) {
        optionsLayout.removeAllViews();
        optionsView = new ArrayList<>();
        if (options == null){
            options = new ArrayList<>();
        }

        for (Option option : options){
            OptionView view = new OptionView(getActivity());
            view.setTitle(option.getTitle());
            view.setOnClickListener(new AnswerQuestion(option));
            optionsView.add(view);
            optionsLayout.addView(view);
        }
    }

    private void setQuestionBody(String imageUrl, String questionText) {
        questionTextView.setText(questionText);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            questionImageView.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).
                    load(imageUrl).
                    placeholder(R.drawable.cover_place_holder).
                    crossFade().
                    into(questionImageView);

        }else {
            questionImageView.setVisibility(View.GONE);
        }
    }

    private void setScores(int myScore, int opponentScore) {
        this.myScore = myScore;
        this.opponentScore = opponentScore;
        myScoreView.setText(getResources().getString(R.string.score).replace(SCORE, "" + myScore));
        opponentScoreView.setText(getResources().getString(R.string.score).replace(SCORE, "" + opponentScore));
    }

    private void showResultPage() {
        finished = true;
        MatchData matchdata = gameManager.getMatchData();
        PlayerData myPlayer = new PlayerData(matchdata.getMyInfo(), myScore);
        PlayerData opponentPlayer = new PlayerData(matchdata.getOpponentInfo(), opponentScore);
        ((FragmentContainerActivity) getActivity()).changeFragment(ResultFragment.newInstance(myPlayer, opponentPlayer));
    }

    private class AnswerQuestion implements View.OnClickListener {

        private Option option;

        public AnswerQuestion(Option option) {
            this.option = option;
        }

        @Override
        public void onClick(View v) {
            if(answered || option == null || currentQuestion == null){
                return;
            }

            ((OptionView) v).setStatus(OptionView.STATUS_DISABLED);
            selectedOption = option;
            gameManager.answerToQuestion(option, remainingTime);
            answered = true;
        }
    }

    private class GameEventHandler implements GameManager.GameEventHandler {
        @Override
        public void onNewQuestion(Question question) {
            setCurrentQuestion(question);
        }

        @Override
        public void onGameFinished() {
            showResultPage();
        }

        @Override
        public void onScore(int myScore, int opponentScore) {
            setScores(myScore, opponentScore);
        }
    }

    private class TimerAnimation extends Animation {
        int startWidth;

        public TimerAnimation(int startWidth) {
            this.startWidth = startWidth;
            setDuration(QUESTION_TIME);
            setInterpolator(new LinearInterpolator());
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (finished) {
                return;
            }

            super.applyTransformation(interpolatedTime, t);
            interpolatedTime = (1 - interpolatedTime);

            setBarSize(interpolatedTime);
            setTime(interpolatedTime);
            setBarColor(interpolatedTime);

            if (interpolatedTime == 0){
                if(!answered){
                    gameManager.notAnswered();
                }

                timerAnimation.cancel();
                timerAnimation = null;
            }
        }

        private void setTime(float interpolatedTime) {
            remainingTime = (int) (interpolatedTime * 10);
            timeBar.setText("" + remainingTime);
        }

        private void setBarSize(float interpolatedTime) {
            timeBar.getLayoutParams().width = (int) (startWidth * interpolatedTime);
            timeBar.requestLayout();
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
            timeBar.setBackgroundColor(getResources().getColor(backgroundColorResource));
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
