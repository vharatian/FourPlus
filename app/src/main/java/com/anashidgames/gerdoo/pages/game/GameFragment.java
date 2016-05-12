package com.anashidgames.gerdoo.pages.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Option;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.pages.game.view.OptionView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 5/8/16.
 */
public class GameFragment extends Fragment {

    public static final String DATA = "data";
    private List<Option> options;
    private int questionIndex = 1;

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
        setCurrentQuestion(gameManager.getCurrentQuestion());

        return rootView;
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
        opponentScoreView = (TextView) rootView.findViewById(R.id.opponentNameView);
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
        questionIndex ++;
        setQuestionBody(question.getQuestionImageUrl(), question.getQuestionText());
        setOptions(question.getOptions());
    }

    private void setOptions(List<Option> options) {
        this.options = options;
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
        if (imageUrl != null && !imageUrl.isEmpty()) {
            questionImageView.setVisibility(View.VISIBLE);
            questionTextView.setVisibility(View.GONE);
            Glide.with(getActivity()).
                    load(imageUrl).
                    placeholder(R.drawable.cover_place_holder).
                    crossFade().
                    into(questionImageView);

        }else {
            questionTextView.setVisibility(View.VISIBLE);
            questionImageView.setVisibility(View.GONE);
            questionTextView.setText(questionText);
        }
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

                gameManager.answerToQuestion(questionIndex, 1 + options.indexOf(option), 10);
            answered = true;
        }
    }

    private class GameEventHandler implements GameManager.GameEventHandler {
        @Override
        public void onNewQuestion(Question question) {
            setCurrentQuestion(question);
        }
    }
}
