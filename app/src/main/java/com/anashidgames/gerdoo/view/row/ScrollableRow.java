package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.HListView;
import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class ScrollableRow<V extends View, T> extends FrameLayout {

    private View progressView;
    private AbsHListView.LayoutParams layoutParams;


    private DataProvider dataProvider;
    private RowAdapter<V, T> rowAdapter;
    private HListView listView;

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

        listView = (HListView) findViewById(R.id.listView);
        progressView = findViewById(R.id.progress);

        layoutParams = new AbsHListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setExpansionHeight(int height){

        listView.getLayoutParams().height = height;
    }


    public void setData(){

        listView.setAdapter(null);
        showLoading();

        loadData();
    }

    private void loadData() {
        Call<List<T>> call = dataProvider.getItems();
        call.enqueue(new ItemsCallBack());
    }

    private void showLoading() {
        progressView.setVisibility(VISIBLE);
    }

    private void hideLoading() {
        progressView.setVisibility(GONE);
    }

    private void addItems(final List<T> items) {
        listView.setAdapter(new ItemsAdapter(items));
//        for (int i = items.size() - 1; i >= 0; i--){
//            RowItem item = items.get(i);
//            RowItemView view = new RowItemView(getContext());
//            view.setItem(item);
//            view.setLayoutParams(layoutParams);
//            itemsLayout.addView(view);
//        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(items.size());
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
            addItems(data);
        }
    }

    public void setRowAdapter(RowAdapter<V, T> rowAdapter) {
        this.rowAdapter = rowAdapter;
    }

    public interface DataProvider<T> {
        Call<List<T>> getItems();
    }

    public interface RowAdapter<V extends View, T>{
        V newItemView(Context context);
        void bind(V view, T data);
    }

    private class ItemsAdapter extends BaseAdapter {
        private final List<T> items;

        public ItemsAdapter(List<T> items) {
            if (items == null){
                items = new ArrayList<>();
            }

            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null || !(convertView instanceof SimpleRowItemView)){
                view = rowAdapter.newItemView(getContext());
            }else {
                view = convertView;
            }

            rowAdapter.bind((V) view, items.get(position));
            view.setLayoutParams(layoutParams);
            return view;
        }
    }
}
