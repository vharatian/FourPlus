package com.anashidgames.gerdoo.pages.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;

/**
 * Created by psycho on 4/24/16.
 */
public class GameActivity extends FragmentContainerActivity {

    public static final String MATCH_MAKING_NAME = "matchMakingName";

    public static Intent newIntent(Context context, String matchMakingName) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(MATCH_MAKING_NAME, matchMakingName);
        return intent;
    }


    private GameManager gameManager;

    public GameActivity() {
        super(R.id.fragmentPlace, false);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String matchMakingName = getIntent().getStringExtra(MATCH_MAKING_NAME);

        changeFragment(MatchMakingFragment.newInstance(matchMakingName));
    }


    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
