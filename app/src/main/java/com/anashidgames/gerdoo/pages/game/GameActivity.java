package com.anashidgames.gerdoo.pages.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;

/**
 * Created by psycho on 4/24/16.
 */
public class GameActivity extends FragmentContainerActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }


    public GameActivity() {
        super(R.id.fragmentPlace);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        changeFragment(UserMatchFragment.newInstance());
    }

}
