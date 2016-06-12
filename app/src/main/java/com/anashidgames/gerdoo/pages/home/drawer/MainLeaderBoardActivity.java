package com.anashidgames.gerdoo.pages.home.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.LeaderBoardView;

/**
 * Created by psycho on 5/20/16.
 */
public class MainLeaderBoardActivity extends GerdooActivity{

    private LeaderBoardView leaderBoardView;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainLeaderBoardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_leader_board);

        leaderBoardView = (LeaderBoardView) findViewById(R.id.leaderBoardView);
        leaderBoardView.setActivity(this);
        leaderBoardView.setLeaderBoardId("");

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        leaderBoardView.reload();
    }
}
