package com.anashidgames.gerdoo.pages.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.anashidgames.gerdoo.pages.KeyboardHiderFragment;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.pages.auth.view.PsychoChangeable;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by psycho on 3/25/16.
 */
public abstract class FormFragment extends KeyboardHiderFragment {

    private int messageViewId;
    private int submitButtonId;

    private TextView messageView;
    private View submitButton;
    private AuthStateChangeListener stateChangeListener;
    private List<ValidatableInput> inputs = new ArrayList<>();

    private ProgressDialog progressDialog;
    private Call requestCall;

    public FormFragment(int messageViewId, int submitButtonId) {
        this.messageViewId = messageViewId;
        this.submitButtonId = submitButtonId;

        stateChangeListener = new AuthStateChangeListener(new InputStateChangeListener());
    }

    protected abstract Call callServer();
    protected abstract void submitted(Object result);




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public void onPause() {
        super.onPause();
        cancelRequesting();
    }

    private void init(View view) {
        messageView = (TextView) view.findViewById(messageViewId);
        initSubmitButton(view);
    }

    private void initSubmitButton(View view) {
        submitButton = view.findViewById(submitButtonId);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitRequested();
            }
        });
    }

    private void onSubmitRequested() {
        if (stateChangeListener.isValid()){
            showMessage("");
            submitForm();
        }else{
            showErrors();
        }
    }

    private void showErrors() {
        int messageResource = -1;
        for (ValidatableInput input : inputs){
            if(!input.isValid()){
                if(messageResource <= 0)
                    messageResource = input.getErrorMessage();

                input.showErrorStatusIcon();
            }
        }

        showMessage(messageResource);
    }

    protected void submitForm(){
        cancelRequesting();
        progressDialog = ProgressDialog.show(getActivity(),
                getString(R.string.sendMail),
                getString(R.string.pleaseWait),
                false,
                true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelRequesting();
                    }
                }
        );

        requestCall = callServer();
        requestCall.enqueue(new SubmitCallBack());
    }



    private void cancelRequesting(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }

        if(requestCall != null){
            requestCall.cancel();
        }
    }

    public void showMessage(int messageResource){
        messageView.setText(messageResource);
    }

    public void showMessage(String message){
        messageView.setText(message);
    }

    protected void addInput(ValidatableInput input) {
        inputs.add(input);
        stateChangeListener.addEditText(input);
    }

    private void setMainButtonState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            submitButton.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            submitButton.setBackgroundResource(R.drawable.dark_button_background_invalid);
        }
    }



    private class InputStateChangeListener implements PsychoChangeable.StateChangedListener {
        @Override
        public void stateChanged(PsychoChangeable editText, int newState) {
            setMainButtonState(newState);
        }
    }


    private class SubmitCallBack extends CallbackWithErrorDialog {

        public SubmitCallBack() {
            super(getContext());
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            cancelRequesting();
        }

        @Override
        public void handleSuccessful(Object data) {
            submitted(data);
        }
    }
}
