package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class ScrollableRow<T> extends LinearLayout {

    private LinearLayout itemsLayout;
    private View progressView;
    private HorizontalScrollView scrollView;
    private LayoutParams layoutParams;

    private DataProvider dataProvider;

    public ScrollableRow(Context context) {
        super(context);
        init(context);
    }

    public ScrollableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollableRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollableRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_category_topics_row, this);

        itemsLayout = (LinearLayout) findViewById(R.id.itemsLayout);
        progressView = findViewById(R.id.progress);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setExpansionHeight(int height){
        itemsLayout.getLayoutParams().height = height;
    }


    public void setData(){

        itemsLayout.removeAllViews();
        showLoading();

        loadData();
    }

    private void loadData() {
        Call<List<T>> call = dataProvider.getItems();
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

    private void addItems(List<RowItem> items) {
        for (int i = items.size() - 1; i >= 0; i--){
            RowItem item = items.get(i);
            RowItemView view = new RowItemView(getContext());
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

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    private class ItemsCallBack extends CallbackWithErrorDialog<List<T>> {

        public ItemsCallBack() {
            super(getContext());
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            hideLoading();
        }

        @Override
        public void handleSuccessful(List<T> data) {
            addItems(dataProvider.convert(data));
        }
    }

    public static interface DataProvider<T> {
        Call<List<T>> getItems();
        List<RowItem> convert(List<T> items);
    }
}
