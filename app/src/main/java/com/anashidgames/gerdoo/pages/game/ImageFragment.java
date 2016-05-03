package com.anashidgames.gerdoo.pages.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 5/1/16.
 */
public class ImageFragment extends Fragment{

    public static final String IMAGE_PATH = "imagePath";

    public static Fragment newInstance(String imagePath) {
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH, imagePath);
        Fragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        Bundle bundle = getArguments();
        String imagePath = bundle.getString(IMAGE_PATH);
        Bitmap image = BitmapFactory.decodeFile(imagePath);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        return rootView;
    }
}
