package com.anashidgames.gerdoo.core.service.call;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by psycho on 4/24/16.
 */
public abstract class ConverterCall<T, E> implements Call<T> {
    private final Call<E> innerCall;

    public ConverterCall(Call<E> innerCall) {
        this.innerCall = innerCall;
    }

    public abstract T convert(E data);

    @Override
    public Response<T> execute() throws IOException {
        return convert(innerCall.execute());
    }

    @NonNull
    private Response<T> convert(Response<E> response){
        Response<T> result;
        if (response.isSuccessful())
            result = Response.success(convert(response.body()), response.raw());
        else
            result = Response.error(response.errorBody(), response.raw());
        return result;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        innerCall.enqueue(new ConverterCallback(callback));
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
        return new ConverterCall<T, E>(innerCall.clone()) {
            @Override
            public T convert(E data) {
                return convert(data);
            }
        };
    }

    @Override
    public Request request() {
        return null;
    }

    private class ConverterCallback implements Callback<E> {
        private final Callback<T> innerCallback;

        public ConverterCallback(Callback<T> callback) {
            this.innerCallback = callback;
        }

        @Override
        public void onResponse(Call<E> call, Response<E> response) {
            innerCallback.onResponse(ConverterCall.this, convert(response));
        }

        @Override
        public void onFailure(Call<E> call, Throwable t) {
            innerCallback.onFailure(ConverterCall.this, t);
        }
    }
}
