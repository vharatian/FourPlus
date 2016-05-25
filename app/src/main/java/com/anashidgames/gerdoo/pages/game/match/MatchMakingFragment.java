package com.anashidgames.gerdoo.pages.game.match;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.realTime.CallBack;
import com.anashidgames.gerdoo.core.service.realTime.MatchMakingManager;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.game.GameActivity;
import com.google.gson.Gson;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 4/24/16.
 */
public class MatchMakingFragment extends Fragment {

    public static final String MATCH_MAKING_NAME = "matchMakingName";

    public static Fragment newInstance(String matchMakingName) {
        Bundle bundle = new Bundle();
        bundle.putString(MATCH_MAKING_NAME, matchMakingName);
        Fragment fragment = new MatchMakingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean canceled = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_making, container, false);

        String matchMakingName = getArguments().getString(MATCH_MAKING_NAME);

        GifImageView animationView = (GifImageView) rootView.findViewById(R.id.imageView);
        animationView.setImageResource(R.drawable.user_matching);


        rootView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });


        MatchMakingManager matchMakingManager = GerdooServer.INSTANCE.createMatchMakingManager(matchMakingName);
        ((GameActivity) getActivity()).setMatchMakingManager(matchMakingManager);
        matchMakingManager.connect(new MatchMakingConnectionCallBack());


        return rootView;
    }

    private void cancel() {
        canceled = true;
        getActivity().finish();
    }

    private class MatchMadeCallBack implements CallBack<MatchData> {
        @Override
        public void onData(MatchData data) {
            if (!canceled){
                Log.i("Psycho", new Gson().toJson(data));
                ((FragmentContainerActivity) getActivity()).changeFragment(MatchPreviewFragment.newInstance(data));
            }
        }

        @Override
        public void onFailure(String message, @Nullable Throwable t) {
            Log.e("Psycho", "message", t);
            if (!canceled) {
                FragmentActivity activity = getActivity();
                if (activity != null)
                    activity.finish();
            }
        }
    }

    private class MatchMakingConnectionCallBack implements MatchMakingManager.MatchMakingConnectionCallBack {
        @Override
        public void onConnected(MatchMakingManager manager) {
            manager.matchUser(new MatchMadeCallBack());
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
