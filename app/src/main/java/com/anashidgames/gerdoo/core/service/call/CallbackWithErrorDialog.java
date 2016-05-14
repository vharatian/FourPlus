package com.anashidgames.gerdoo.core.service.call;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.anashidgames.gerdoo.R;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by psycho on 3/22/16.
 */
public abstract class CallbackWithErrorDialog<T> extends PsychoCallBack<T>{
    private static Long lastErrorTime = 0l;

    private Context context;
    private AlertDialog dialog;

    public CallbackWithErrorDialog(Context context) {
        this.context = context;
    }

    @Override
    public void handleFailure(Call<T> call, Throwable t) {
        Log.e("Four+", "error", t);
        ShowErrorDialog(R.string.networkError);
    }

    @Override
    public void handleServerError(Response<T> response) {
        ShowErrorDialog(R.string.serverError);
    }

    private void ShowErrorDialog(int errorResource) {
        long currentTime = System.currentTimeMillis();
        synchronized (lastErrorTime){
            if (currentTime - lastErrorTime < 1000){
                return;
            }

            lastErrorTime = currentTime;
        }

        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(errorResource)
                .setPositiveButton(R.string.ok, new CloseClickListener())
                .create();

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void postError(){}

    private class CloseClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            postError();
        }
    }
}
