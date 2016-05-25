package com.anashidgames.gerdoo.pages.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.core.service.realTime.MatchMakingManager;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.game.match.MatchMakingFragment;

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
    private MatchMakingManager matchMakingManager;

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

    public MatchMakingManager getMatchMakingManager() {
        return matchMakingManager;
    }

    public void setMatchMakingManager(MatchMakingManager matchMakingManager) {
        this.matchMakingManager = matchMakingManager;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (matchMakingManager != null){
            matchMakingManager.close();
        }

        if (gameManager != null){
            gameManager.close();
        }

        finish();
    }
}
