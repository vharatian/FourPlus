package com.anashidgames.gerdoo.pages.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.anashidgames.gerdoo.pages.KeyboardHiderFragment;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.pages.auth.view.PsychoChangeable;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by psycho on 3/25/16.
 */
public abstract class FormFragment extends KeyboardHiderFragment {

    public static final int DEFAULT_FORM_ID = -28467;

    private int messageViewId;
    private int submitButtonId;

    private TextView messageView;
    private View submitButton;
    private AuthStateChangeListener stateChangeListener;
    private List<ValidatableInput> inputs;

    private ProgressDialog progressDialog;
    private Call requestCall;

    public FormFragment(int messageViewId, int submitButtonId) {
        this.messageViewId = messageViewId;
        this.submitButtonId = submitButtonId;
    }

    protected abstract void callServer(int formId, Callback callback);
    protected abstract void submitted(Object result);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cancelRequesting();
        stateChangeListener = new AuthStateChangeListener(new InputStateChangeListener());
        inputs = new ArrayList<>();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        for (ValidatableInput input: inputs){
            input.clearText();
        }
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
            submitForm(DEFAULT_FORM_ID);
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

    protected void submitForm(int formId){
        cancelRequesting();
        progressDialog = ProgressDialog.show(getActivity(),
                "",
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

        callServer(formId, new SubmitCallBack());
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
        if (messageView != null) {
            messageView.setText(message);
        }
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
            super(getActivity());
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
