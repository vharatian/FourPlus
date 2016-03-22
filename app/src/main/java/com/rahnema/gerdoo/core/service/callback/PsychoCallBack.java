package com.rahnema.gerdoo.core.service.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by psycho on 3/22/16.
 */
public abstract class PsychoCallBack<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful())
            handleSuccessful(response);
        else
            handleServerError(response);
    }

    protected abstract void handleServerError(Response<T> response);

    abstract public void handleSuccessful(Response<T> response);
}
