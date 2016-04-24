package com.anashidgames.gerdoo.core.service.call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by psycho on 4/23/16.
 */
public class PsychoCall<T> implements Call<T> {

    private Call<T> innerCall;
    private List<Callback<T>> callbacks;

    public PsychoCall(Call<T> innerCall) {
        this.innerCall = innerCall;
    }

    @Override
    public Response<T> execute() throws IOException {
        return innerCall.execute();
    }

    @Override
    public void enqueue(Callback<T> callback) {
        addCallback(callback);
        innerCall.enqueue(new InnerCallback(callbacks));
    }

    public void addCallback(Callback<T> callback){
        if (callbacks == null)
            callbacks = new ArrayList<>();

        callbacks.add(callback);
    }

    @Override
    public boolean isExecuted() {
        return innerCall.isExecuted();
    }

    @Override
    public void cancel() {
        innerCall.cancel();
    }

    @Override
    public boolean isCanceled() {
        return innerCall.isCanceled();
    }

    @Override
    public Call<T> clone() {
        return innerCall.clone();
    }

    @Override
    public Request request() {
        return innerCall.request();
    }

    private class InnerCallback implements Callback<T> {
        private final List<Callback<T>> callbacks;

        public InnerCallback(List<Callback<T>> callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            for (Callback<T> callback : callbacks)
                if (callback != null)
                    callback.onResponse(call, response);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            for (Callback<T> callback : callbacks)
                if (callback != null)
                    callback.onFailure(call, t);
        }
    }
}
