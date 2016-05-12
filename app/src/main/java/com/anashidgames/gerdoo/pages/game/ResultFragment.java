package com.anashidgames.gerdoo.pages.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.game.view.PlayerView;
import com.anashidgames.gerdoo.pages.home.HomeActivity;

/**
 * Created by psycho on 4/24/16.
 */
public class ResultFragment extends Fragment {

    public static final String OPPONENT = "opponent";
    public static final String ME = "me";


    public static Fragment newInstance(PlayerData myPlayer, PlayerData opponentPlayer) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OPPONENT, opponentPlayer);
        bundle.putSerializable(ME, myPlayer);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private PlayerView opponentView;
    private PlayerView meView;
    private View.OnClickListener clickListener;

    private PlayerData opponent;
    private PlayerData me;

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
        opponentView = (PlayerView) rootView.findViewById(R.id.oponnentView);
        meView = (PlayerView) rootView.findViewById(R.id.meView);

        clickListener = new InnerClickListener();

        rootView.findViewById(R.id.doneButton).setOnClickListener(clickListener);
        rootView.findViewById(R.id.scoresPageButton).setOnClickListener(clickListener);
        rootView.findViewById(R.id.replayButton).setOnClickListener(clickListener);
        rootView.findViewById(R.id.homeButton).setOnClickListener(clickListener);
    }

    private void setData() {
        opponentView.setData(opponent);
        meView.setData(me);

        if (opponent.getScore() > me.getScore()){
            meView.setBanner(PlayerView.LOOS_BANNER);
        }else if (opponent.getScore() < me.getScore()){
            meView.setBanner(PlayerView.WIN_BANNER);
        }else {
            meView.setBanner(PlayerView.TIE_BANNER);
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        opponent = (PlayerData) bundle.getSerializable(OPPONENT);
        me = (PlayerData) bundle.getSerializable(ME);
    }

    private class InnerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.homeButton:
                    getActivity().finish();
                    Intent intent = HomeActivity.newIntent(getActivity());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.replayButton:
                case R.id.doneButton:
                    getActivity().finish();
                    break;
                case R.id.scoresPageButton:
                    ((FragmentContainerActivity) getActivity()).changeFragment(ScoresFragment.newInstance());
                    break;
            }
        }
    }
}
