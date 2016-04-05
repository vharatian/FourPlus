package com.anashidgames.gerdoo.pages.topic.list;

import android.content.Context;

import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.callback.PsychoCallBack;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by psycho on 7/21/15.
 */
public abstract class PsychoDataProvider<T> implements Serializable{

    private String nextPage = null;
    private boolean firstTime;
    private Context context;
    private Call<PsychoListResponse<T>> call;


    public PsychoDataProvider(Context context) {
        this.context = context;
        restart();
    }

    protected abstract Call<PsychoListResponse<T>> callServer(String nextPage);


    public void getList(final DataCallback<List<T>> callback){
        if (firstTime || !isFinished()) {
            call = callServer(nextPage);
            call.enqueue(new ResponseCallBack(context, callback));
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    callback.onData(null, true);
                }
            }).start();
        }

        firstTime = false;
    }

    private boolean isFinished() {
        return nextPage == null || nextPage.isEmpty();
    }

    public void restart() {
        nextPage = null;
        firstTime = true;
        if (call != null)
            call.cancel();
    }

    public void cancelLoading() {
        if (call != null)
            call.cancel();
    }

    private class ResponseCallBack extends CallbackWithErrorDialog<PsychoListResponse<T>> {

        private DataCallback<List<T>> callback;

        public ResponseCallBack(Context context, DataCallback<List<T>> callback) {
            super(context);
            this.callback = callback;
        }

        @Override
        public void handleSuccessful(PsychoListResponse<T> data) {
            nextPage = data.getNextPage();
            callback.onData(data.getList(), isFinished());
        }

        @Override
        protected void postError() {
            super.postError();
            callback.onError();
        }
    }
}
