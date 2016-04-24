package com.anashidgames.gerdoo.pages.game;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.game.view.PlayerView;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 4/24/16.
 */
public class ResultFragment extends Fragment {

    public static final String FIRST_PLAYER = "firstPlayer";
    public static final String SECOND_PLAYER = "secondPlayer";


    public static Fragment newInstance() {
        String proPicUrl = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";
        String coverUrl = "https://i.imgsafe.org/c77e5e5.jpg";
        String firstName = "کیان اشرفی";
        String secondName = "وحید هراتیان";
        PlayerData firstPlayer = new PlayerData("", proPicUrl, coverUrl, firstName, 782);
        PlayerData secondPlayer = new PlayerData("", proPicUrl, coverUrl, secondName, 56);

        Bundle bundle = new Bundle();
        bundle.putSerializable(FIRST_PLAYER, firstPlayer);
        bundle.putSerializable(SECOND_PLAYER, secondPlayer);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private PlayerView firstPlayerView;
    private PlayerView secondPlayerView;
    private View doneButton;
    private View.OnClickListener clickListener;

    private PlayerData firstPlayer;
    private PlayerData secondPLayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        initData();
        initViews(rootView);
        setData();

        return rootView;
    }

    private void initViews(View rootView) {
        firstPlayerView = (PlayerView) rootView.findViewById(R.id.firstPlayerView);
        secondPlayerView = (PlayerView) rootView.findViewById(R.id.secondPlayerView);
        doneButton = rootView.findViewById(R.id.doneButton);

        clickListener = new InnerClickListener();

        doneButton.setOnClickListener(clickListener);
    }

    private void setData() {
        firstPlayerView.setData(firstPlayer);
        secondPlayerView.setData(secondPLayer);

        if (firstPlayer.getScore() > secondPLayer.getScore()){
            firstPlayerView.setWinner(true);
        }else if (firstPlayer.getScore() < secondPLayer.getScore()){
            secondPlayerView.setWinner(true);
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        firstPlayer = (PlayerData) bundle.getSerializable(FIRST_PLAYER);
        secondPLayer = (PlayerData) bundle.getSerializable(SECOND_PLAYER);
    }

    private class InnerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.doneButton:
                    getActivity().finish();
                    break;
            }
        }
    }
}
