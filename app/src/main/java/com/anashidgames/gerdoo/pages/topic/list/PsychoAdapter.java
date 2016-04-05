package com.anashidgames.gerdoo.pages.topic.list;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public void addToStart(T item){
        dataBucket.addToStart(item);
        notifyItemInserted(0);
    }

    public void addToEnd(T item){
        int index = dataBucket.addToEnd(item);
        notifyItemInserted(index);
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
            return new PsychoViewHolder(inflater.inflate(R.layout.view_progress_wheel, parent, false));
        }else
            return createViewHolder(inflater, parent, viewType);
    }

    private void addDataToUI() {
        final boolean[] done = {false};
        while (!done[0]) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (dataBucket) {
                        if (loading) {
                            dataBucket.attachNewData();
                        }
                        loading = false;
                        try {
                            notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                        done[0] = true;
                    }
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }
        }

        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {


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
}