package com.anashidgames.gerdoo.pages.topic.list;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;


/**
 * Created by psycho on 7/9/15.
 */
public abstract class PsychoAdapter<T> extends RecyclerView.Adapter<PsychoViewHolder>{

    public static final String TAG = "PsychoAdapter";
    public static final int LOADING_VIEW_ID = -1;
    public static final int VIEW_LOADING = 1;
    public static final int VIEW_ITEM = 0;
    private Activity activity;

    private PsychoDataBucket<T> dataBucket;
    private boolean loading;

    private DataReadyCallback dataReadyCallback;

    private OnLoadingChangedListener loadingListener;


    public PsychoAdapter(Activity activity, PsychoDataProvider<T> dataProvider) {
        this.activity = activity;
        this.dataBucket = new PsychoDataBucket<>(dataProvider);
        setHasStableIds(true);

        refresh();
    }

    public void refresh() {
        synchronized (dataBucket) {
            loading = false;
            if (dataReadyCallback != null)
                dataReadyCallback.cancel();

            dataBucket.restart();
        }

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (isLoadingView(position))
            return LOADING_VIEW_ID;
        else
            return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingView(position))
            return VIEW_LOADING;
        else
            return VIEW_ITEM;
    }

    public void setLoadingListener(OnLoadingChangedListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    @Override
    public void onBindViewHolder(PsychoViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_LOADING) {
            holder.bind(null);
            if (!dataBucket.isFinished()) {
                attach();
            }
        }else {
            holder.bind(dataBucket.getItem(position));
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (dataBucket == null)
            count = 0;
        else
            count = dataBucket.getCount();

        if (dataBucket != null && !dataBucket.isFinished())
            count++;

        return count;
    }


    private void attach() {
        if (loading || dataBucket.isFinished())
            return;

        Log.i(TAG, "attach more data");
        loading = true;

        if (loadingListener != null) {
            loadingListener.onLoadingChanged(loading);
        }

        if (dataReadyCallback != null)
            dataReadyCallback.cancel();

        dataReadyCallback = new DataReadyCallback();
        dataBucket.getNewData(dataReadyCallback);
    }

    protected Context getContext(){
        return activity;
    }

    protected T getItem(int position){
        return dataBucket.getItem(position);
    }

    public boolean isLoadingView(int position) {
        return position == dataBucket.getCount();
    }

    @Override
    public PsychoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (viewType == VIEW_LOADING){
            PsychoViewHolder holder = new PsychoViewHolder(inflater.inflate(R.layout.view_progress_wheel, parent, false));
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
            return holder;
        }else
            return createViewHolder(inflater, parent, viewType);
    }

    private void addDataToUI() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (dataBucket) {
                    int startCount = dataBucket.getCount();
                    if (loading) {
                        dataBucket.attachNewData();
                    }
                    loading = false;
                    if (loadingListener != null) {
                        loadingListener.onLoadingChanged(loading);
                    }


                    int endCount = dataBucket.getCount();
                    if (startCount != endCount || dataBucket.isFinished())
                        notifyDataSetChanged();
                }
            }
        });
    }

    public abstract PsychoViewHolder<T> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    public void cancelLoading() {
        dataBucket.cancelLoading();
    }


    private class DataReadyCallback implements PsychoDataBucket.DoneCallback {
        private boolean canceled = false;

        public void cancel() {
            canceled = true;
        }

        @Override
        public void done() {
            if (canceled)
                return;

            addDataToUI();
        }
    }

    public interface OnLoadingChangedListener {
        void onLoadingChanged(boolean loading);
    }
}
