package com.anashidgames.gerdoo.pages.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.pages.game.view.PlayerView;

/**
 * Created by psycho on 5/9/16.
 */
public class MatchPreviewFragment extends Fragment {

    private static final String DATA = "data";



    public static Fragment newInstance(MatchData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA, data);
        MatchPreviewFragment fragment = new MatchPreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private MatchData data;
    private PlayerView myPlayerView;
    private PlayerView opponentPlayerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_preview, container, false);

        data = (MatchData) getArguments().getSerializable(DATA);


        myPlayerView = (PlayerView) rootView.findViewById(R.id.myPlayerView);
        opponentPlayerView = (PlayerView) rootView.findViewById(R.id.opponentPlayerView);

        createConnection(data);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        myPlayerView.setData(new PlayerData(data.getMyInfo()));
        opponentPlayerView.setData(new PlayerData(data.getOpponentInfo()));
    }

    private void createConnection(MatchData data) {
        GameManager gameManager = GerdooServer.INSTANCE.createGameManager(data);
        gameManager.start(new GameConnectionCallBack());
    }

    private class GameConnectionCallBack implements GameManager.GameStartCallBack {
        @Override
        public void onStart(GameManager manager) {
            GameActivity activity = (GameActivity) getActivity();
            activity.setGameManager(manager);
            activity.changeFragment(GameFragment.newInstance());
        }

        @Override
        public void onError(Throwable t) {
            Log.e("psycho", t.getMessage(), t);
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
