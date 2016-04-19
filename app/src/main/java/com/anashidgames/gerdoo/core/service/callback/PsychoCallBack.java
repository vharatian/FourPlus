package com.anashidgames.gerdoo.core.service.callback;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by psycho on 3/22/16.
 */
public abstract class PsychoCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        postExecution();

        if(response.isSuccessful() && response.body() != null)
            handleSuccessful(response.body());
        else
            handleServerError(response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        postExecution();

        handleFailure(call, t);
    }

    protected void postExecution(){}

    public abstract void handleSuccessful(T data);
    public abstract void handleFailure(Call<T> call, Throwable t);
    public abstract void handleServerError(Response<T> response);



}
