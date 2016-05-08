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

    private boolean canceled = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_match, container, false);

        GifImageView animationView = (GifImageView) rootView.findViewById(R.id.imageView);
        animationView.setImageResource(R.drawable.user_matching);


        rootView.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canceled)
                    return;

                ((FragmentContainerActivity) getActivity()).changeFragment(GameFragment.newInstance());
            }
        }, 5000);

        return rootView;
    }

    private void cancel() {
        canceled = true;
        getActivity().finish();
    }
}
