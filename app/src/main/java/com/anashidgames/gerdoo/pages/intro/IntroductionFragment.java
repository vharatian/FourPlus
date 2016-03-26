package com.anashidgames.gerdoo.pages.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 3/21/16.
 */
public class IntroductionFragment extends Fragment {


    public static final String GIF_RESOURCE = "gifResource";
    public static final String TITLE_RESOURCE = "titleResource";
    public static final String DESCRIPTION_RESOURCE = "descriptionResource";
    private int gifResource;

    public static IntroductionFragment newInstance(int gifResource, int titleResource, int descriptionResource){
        Bundle bundle = new Bundle();

        bundle.putInt(GIF_RESOURCE, gifResource);
        bundle.putInt(TITLE_RESOURCE, titleResource);
        bundle.putInt(DESCRIPTION_RESOURCE, descriptionResource);

        IntroductionFragment fragment = new IntroductionFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private GifImageView gifView;
    private TextView titleView;
    private TextView descriptionView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_introduction, container, false);

        gifView = (GifImageView) rootView.findViewById(R.id.gif);
        titleView = (TextView) rootView.findViewById(R.id.title);
        descriptionView = (TextView) rootView.findViewById(R.id.description);

        Bundle bundle = getArguments();
        gifResource = bundle.getInt(GIF_RESOURCE);
        int titleResource = bundle.getInt(TITLE_RESOURCE);
        int descriptionResource = bundle.getInt(DESCRIPTION_RESOURCE);

        titleView.setText(titleResource);
        descriptionView.setText(descriptionResource);
        gifView.setImageResource(gifResource);

        return rootView;
    }

    public void resetAnimation(){
        if (gifView == null)
            return;

        try {
            gifView.setImageDrawable(new GifDrawable( getResources(), gifResource ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
