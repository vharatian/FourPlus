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
import com.anashidgames.gerdoo.core.service.model.Option;
import com.anashidgames.gerdoo.pages.game.view.OptionView;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 5/8/16.
 */
public class GameFragment extends Fragment {

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

    private GifImageView guideButton;
    private TextView guideTextView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        myImageView = (ImageView) rootView.findViewById(R.id.myImageView);
        myNameView = (TextView) rootView.findViewById(R.id.myNameView);
        myScoreView = (TextView) rootView.findViewById(R.id.myScoreView);

        opponentImageView = (ImageView) rootView.findViewById(R.id.opponentImageView);
        opponentNameView = (TextView) rootView.findViewById(R.id.opponentNameView);
        opponentScoreView = (TextView) rootView.findViewById(R.id.opponentNameView);

        timeBar = (TextView) rootView.findViewById(R.id.timeBar);
        optionsLayout = (LinearLayout) rootView.findViewById(R.id.optionsLayout);

        guideButton = (GifImageView) rootView.findViewById(R.id.hintButton);
        guideTextView = (TextView) rootView.findViewById(R.id.hintTextView);

        questionTextView = (TextView) rootView.findViewById(R.id.questionTextView);
        questionImageView = (ImageView) rootView.findViewById(R.id.questionImageView);


        setData();

        return rootView;
    }

    private void setData() {
        String image = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";
        Glide.with(getActivity()).load(image).placeholder(R.drawable.user_image_place_holder).into(myImageView);
        Glide.with(getActivity()).load(image).placeholder(R.drawable.user_image_place_holder).into(opponentImageView);

        String name = "کیان اشرفی";
        myNameView.setText(name);
        opponentNameView.setText(name);

        List<Option> options = Arrays.asList(
            new Option("", "دانته"),
            new Option("", "لوسیا"),
            new Option("", "ویرژیل"),
            new Option("", "اسپارتا")
        );

        for (Option option : options){
            OptionView view = new OptionView(getActivity());
            view.setTitle(option.getTitle());
            optionsLayout.addView(view);
        }

        questionTextView.setText("شخصیت اصلی بازی دویل می کرای کیه ؟");
    }

}
