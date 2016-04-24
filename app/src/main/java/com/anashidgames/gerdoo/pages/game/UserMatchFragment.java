package com.anashidgames.gerdoo.pages.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 4/24/16.
 */
public class UserMatchFragment extends Fragment {
    public static Fragment newInstance() {
        return new UserMatchFragment();
    }

    private GifImageView animationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        animationView = new GifImageView(getActivity());
        animationView.setImageResource(R.drawable.user_matching);


        animationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((FragmentContainerActivity) getActivity()).changeFragment(ResultFragment.newInstance());
            }
        }, 5000);


        return animationView;
    }
}
