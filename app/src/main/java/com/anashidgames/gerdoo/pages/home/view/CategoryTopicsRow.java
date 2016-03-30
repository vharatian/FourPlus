package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.CategoryItem;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryTopicsRow extends LinearLayout {

    private TextView titleView;
    private LinearLayout itemsLayout;
    private View progressView;
    private HorizontalScrollView scrollView;
    private LayoutParams layoutParams;


    private GerdooServer server;

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

        titleView = (TextView) findViewById(R.id.title);
        itemsLayout = (LinearLayout) findViewById(R.id.itemsLayout);
        progressView = findViewById(R.id.progress);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        server = new GerdooServer();

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public void setData(String title, String dataUrl){

        itemsLayout.removeAllViews();
        showLoading();

        if (title != null)
            titleView.setText(title + ":");
        else
            titleView.setVisibility(GONE);

        loadData(dataUrl);
    }

    private void loadData(String dataUrl) {
        Call<List<CategoryItem>> call = server.getCategoryItems(dataUrl);
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

    private void addItems(List<CategoryItem> items) {
        for (int i = items.size() - 1; i >= 0; i--){
            CategoryItem item = items.get(i);
            CategoryItemView view = new CategoryItemView(getContext());
            view.setItem(item);
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


    private class ItemsCallBack extends CallbackWithErrorDialog<List<CategoryItem>> {

        public ItemsCallBack() {
            super(getContext());
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            hideLoading();
        }

        @Override
        protected void handleSuccessful(List<CategoryItem> data) {
            addItems(data);
        }
    }
}
