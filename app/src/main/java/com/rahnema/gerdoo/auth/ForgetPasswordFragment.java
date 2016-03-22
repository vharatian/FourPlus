package com.rahnema.gerdoo.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.core.service.GerdooServer;
import com.rahnema.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;

import retrofit2.Call;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment implements PsychoChangeable.StateChangedListener {

    public static ForgetPasswordFragment newInstance(){
        return new ForgetPasswordFragment();
    }

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private Button sendMailBtn;

    private AuthStateChangeListener stateChangeListener;

    private ProgressDialog progressDialog;
    private Call signInCall;
    private AlertDialog okDialog;

    private GerdooServer server;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);

        server = new GerdooServer();

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {

        messageView = (TextView) rootView.findViewById(R.id.message);

        stateChangeListener = new AuthStateChangeListener(this);

        initMailInput(rootView);

        initSignInBtn(rootView);
    }

    private void initSignInBtn(View rootView) {
        sendMailBtn = (Button) rootView.findViewById(R.id.sendMailBtn);
        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMailRequested();
            }
        });
    }

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        stateChangeListener.addEditText(mailInput);
        mailInput.setHintMessage(R.string.email);
    }


    private void onSendMailRequested() {
        if (stateChangeListener.getState() != ValidatableInput.STATE_VALID){
            showErrors();
        }else{
            messageView.setText("");
            sendMail();
        }
    }

    private void showErrors() {
        int message = -1;

        if(mailInput.getState() == ValidatableInput.STATE_EMPTY){
            message = R.string.emailIsNeeded;
            mailInput.showErrorStatusIcon();
        }else if(mailInput.getState() == ValidatableInput.STATE_INVALID){
            message = R.string.mailIsInvalid;
            mailInput.showErrorStatusIcon();
        }

        messageView.setText(message);
    }

    private void cancelRequesting(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }

        if(signInCall != null){
            signInCall.cancel();
        }
    }

    private void sendMail() {
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

        String email = mailInput.getText();

        signInCall = server.sendForgetPasswordMail(email);
        signInCall.enqueue(new SendMailCallBack());
    }

    private void setSendMailBtnState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            sendMailBtn.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            sendMailBtn.setBackgroundResource(R.drawable.dark_button_background_invalid);
        }
    }

    @Override
    public void stateChanged(PsychoChangeable editText, int newState) {
        setSendMailBtnState(newState);
    }

    private void sentMail(Boolean result) {
        progressDialog.dismiss();
        progressDialog = null;
        signInCall = null;

        if(result == null){
            showWrongInputError();
        }else{
            okDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.sendMail)
                    .setMessage(R.string.sentForgetPasswordMail)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            okDialog.dismiss();
                            getActivity().onBackPressed();
                        }
                    }).show();
        }
    }

    private void showWrongInputError() {
        messageView.setText(R.string.wrongMail);
    }

    private class SendMailCallBack extends CallbackWithErrorDialog<Boolean> {

        public SendMailCallBack() {
            super(getActivity());
        }

        @Override
        public void handleSuccessful(Response<Boolean> response) {

            sentMail(response.body());
        }
    }


}
