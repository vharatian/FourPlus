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
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.MatchHistoryItem;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.pages.game.view.OptionView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 5/20/16.
 */
public class GameHistoryFragment extends Fragment{

    public static final String HISTORY = "history";


    public static Fragment getInstance(MatchHistoryItem history) {
        Bundle bundle = new Bundle();
        String historyString = new Gson().toJson(history);
        bundle.putString(HISTORY, historyString);
        Fragment fragment = new GameHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private MatchHistoryItem history;

    private TextView questionTextView;
    private ImageView questionImageView;

    private LinearLayout optionsLayout;

    private List<OptionView> optionsView;

    private String userId;

    private List<String> options;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_history, container, false);

        Bundle arguments = getArguments();
        String historyString = arguments.getString(HISTORY);
        history = new Gson().fromJson(historyString, MatchHistoryItem.class);


        initQuestionViews(rootView);
        optionsLayout = (LinearLayout) rootView.findViewById(R.id.optionsLayout);

        setQuestion(history.getQuestion());
        setAnswer(history.getCorrectAnswer(), history.getMyAnswer());


        return rootView;
    }

    private void setAnswer(String correctAnswer, String myAnswer) {
        int status;
        for (int i=0; i<options.size(); i++){
            String option = options.get(i);
            if (option.equals(myAnswer)){
                if (myAnswer.equals(correctAnswer)){
                    status = OptionView.STATUS_RIGHT_CORRECT;
                }else {
                    status = OptionView.STATUS_RIGHT_WRONG;
                }
                optionsView.get(i).setStatus(status);
            }else if (option.equals(correctAnswer)){
                optionsView.get(i).setStatus(OptionView.STATUS_LEFT_CORRECT);
            }
        }
    }


    private void initQuestionViews(View rootView) {
        questionTextView = (TextView) rootView.findViewById(R.id.questionTextView);
        questionImageView = (ImageView) rootView.findViewById(R.id.questionImageView);
    }



    private void setQuestion(Question question) {
        setQuestionBody(question.getQuestionImageUrl(), question.getQuestionText());
        setOptions(question.getOptions());
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
}
