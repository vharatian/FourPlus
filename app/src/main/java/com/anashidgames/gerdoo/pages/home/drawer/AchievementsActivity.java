package com.anashidgames.gerdoo.pages.home.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.AchievementItem;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.home.view.AchievementItemViewHolder;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 6/5/16.
 */
public class AchievementsActivity extends GerdooActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AchievementsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivments);

        findViewById(R.id.backButton).setOnClickListener(new BackListener());
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new ShopAdapter());
    }

    private static class AchievementsProvider extends PsychoDataProvider<AchievementItem> {
        public AchievementsProvider(Context context) {
            super(context);
        }

        @Override
        protected Call<PsychoListResponse<AchievementItem>> callServer(String nextPage) {
            Call<List<AchievementItem>> call = GerdooServer.INSTANCE.getAllAchievements();
            return new ConverterCall<PsychoListResponse<AchievementItem>, List<AchievementItem>>(call) {
                @Override
                public PsychoListResponse<AchievementItem> convert(List<AchievementItem> data) {
                    return new PsychoListResponse<>(data);
                }
            };
        }
    }

    private class ShopAdapter extends PsychoAdapter<AchievementItem> {
        public ShopAdapter() {
            super(AchievementsActivity.this, new AchievementsProvider(AchievementsActivity.this));
        }

        @Override
        public PsychoViewHolder<AchievementItem> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            AchievementItemViewHolder holder = AchievementItemViewHolder.createHolder(inflater, parent);
            return holder;
        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }
}
