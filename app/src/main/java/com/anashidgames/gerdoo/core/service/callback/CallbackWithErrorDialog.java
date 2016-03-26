package com.anashidgames.gerdoo.core.service.callback;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.anashidgames.gerdoo.R;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by psycho on 3/22/16.
 */
public abstract class CallbackWithErrorDialog<T> extends PsychoCallBack<T>{
    private Context context;
    private AlertDialog dialog;

    public CallbackWithErrorDialog(Context context) {
        this.context = context;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ShowErrorDialog();
    }

    @Override
    protected void handleServerError(Response<T> response) {
        ShowErrorDialog();
    }

    private void ShowErrorDialog() {
        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(R.string.networkError)
                .setPositiveButton(R.string.ok, new CloseClickListener())
                .create();

        dialog.show();
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
