package com.anashidgames.gerdoo.pages.topic.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 7/11/15.
 */
public class PsychoDataBucket<T>{
    private List<T> data;
    private List<T> newData = null;
    private boolean finished;
    private PsychoDataProvider provider;

    public PsychoDataBucket(PsychoDataProvider<T> provider) {
        this.provider = provider;
        restart();
    }

    public void restart() {
        data = new ArrayList<>();
        provider.restart();
        finished = false;
    }

    public void getNewData(DoneCallback callback){
        if (!isFinished())
            provider.getList(new NewDataCallback(callback));
    }

    public void attachNewData() {
        if (newData != null && !newData.isEmpty())
            data.addAll(newData);
    }

    public void addToStart(T item){
        data.add(0, item);
    }

    public int addToEnd(T item){
        data.add(item);
        return data.size()-1;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCount(){
        return data.size();
    }

    public T getItem(int position){
        return data.get(position);
    }

    public void cancelLoading() {
        provider.cancelLoading();
    }


    private class NewDataCallback implements DataCallback<List<T>> {

        private final DoneCallback callback;

        public NewDataCallback(DoneCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onData(List<T> data, boolean finished) {
            newData = data;
            PsychoDataBucket.this.finished = finished;
            callback.done();
        }

        @Override
        public void onError() {
            callback.done();
        }
    }

    public interface DoneCallback {
        void done();
    }
}
