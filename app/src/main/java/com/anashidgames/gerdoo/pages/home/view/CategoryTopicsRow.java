package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryTopicsRow extends LinearLayout {

    private LinearLayout itemsLayout;
    private View progressView;
    private HorizontalScrollView scrollView;
    private LayoutParams layoutParams;


    private GerdooServer server;
    private String iconUrl;
    private String title;

    public CategoryTopicsRow(Context context) {
        super(context);
        init(context);
    }

    public CategoryTopicsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryTopicsRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryTopicsRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_category_topics_row, this);

        itemsLayout = (LinearLayout) findViewById(R.id.itemsLayout);
        progressView = findViewById(R.id.progress);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        server = new GerdooServer();

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public void setData(String title, String iconUrl, String dataUrl){
        this.iconUrl = iconUrl;
        this.title = title;

        itemsLayout.removeAllViews();
        showLoading();

        loadData(dataUrl);
    }

    private void loadData(String dataUrl) {
        Call<List<CategoryTopic>> call = server.getCategoryTopics(dataUrl);
        call.enqueue(new ItemsCallBack());
    }

    private void showLoading() {
        if (progressView.getParent() == null)
            itemsLayout.addView(progressView);

        progressView.setVisibility(VISIBLE);
    }

    private void hideLoading() {
        progressView.setVisibility(GONE);
    }

    private void addItems(List<CategoryTopic> items) {
        for (int i = items.size() - 1; i >= 0; i--){
            CategoryTopic item = items.get(i);
            CategoryTopicView view = new CategoryTopicView(getContext());
            view.setItem(item, title, iconUrl);
            view.setLayoutParams(layoutParams);
            itemsLayout.addView(view);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(FOCUS_RIGHT);
            }
        }, 100);
    }


    private class ItemsCallBack extends CallbackWithErrorDialog<List<CategoryTopic>> {

        public ItemsCallBack() {
            super(getContext());
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            hideLoading();
        }

        @Override
        public void handleSuccessful(List<CategoryTopic> data) {
            addItems(data);
        }
    }
}
