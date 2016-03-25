package com.rahnema.gerdoo.pages.auth;

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
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;

import retrofit2.Call;
import retrofit2.Response;

public class SignInFragment extends Fragment implements PsychoChangeable.StateChangedListener {


    public static SignInFragment newInstance(){
        return new SignInFragment();
    }

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private Button signInBtn;

    private AuthStateChangeListener stateChangeListener;

    private ProgressDialog progressDialog;
    private Call signInCall;

    private GerdooServer server;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        server = new GerdooServer();

        initViews(rootView);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRequesting();
    }

    private void initViews(View rootView) {

        messageView = (TextView) rootView.findViewById(R.id.message);

        stateChangeListener = new AuthStateChangeListener(this);

        initMailInput(rootView);
        initPasswordInput(rootView);

        initSignInBtn(rootView);
        initForgetPassword(rootView);
    }

    private void initForgetPassword(View rootView) {
        rootView.findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity) getActivity()).changeFragment(ForgetPasswordFragment.newInstance());
            }
        });
    }

    private void initSignInBtn(View rootView) {
        signInBtn = (Button) rootView.findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInRequested();
            }
        });
    }

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        stateChangeListener.addEditText(mailInput);
        mailInput.setHintMessage(R.string.email);
    }

    private void initPasswordInput(View rootView) {
        passwordInput = (ValidatableInput) rootView.findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        stateChangeListener.addEditText(passwordInput);
        passwordInput.setHintMessage(R.string.password);
    }


    private void onSignInRequested() {
        if (stateChangeListener.getState() != ValidatableInput.STATE_VALID){
            showErrors();
        }else{
            messageView.setText("");
            signIn();
        }
    }

    private void showErrors() {
        int message = -1;
        if(passwordInput.getState() == ValidatableInput.STATE_EMPTY){
            message = R.string.passwordIsNeeded;
            passwordInput.showErrorStatusIcon();
        }else if(passwordInput.getState() == ValidatableInput.STATE_INVALID){
            message = R.string.passwordSouldBeLonger;
            passwordInput.showErrorStatusIcon();
        }

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

    private void signIn() {
        cancelRequesting();
        progressDialog = ProgressDialog.show(getActivity(),
                getString(R.string.signIn),
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
        String password = passwordInput.getText();

        signInCall = server.signIn(email, password);
        signInCall.enqueue(new SignInCallBack());

    }

    private void setSignInBtnState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            signInBtn.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            signInBtn.setBackgroundResource(R.drawable.dark_button_background_invalid);
        }
    }

    @Override
    public void stateChanged(PsychoChangeable editText, int newState) {
        messageView.setText("");
        setSignInBtnState(newState);
    }

    private void signInFinished(String sessionKey) {
        progressDialog.dismiss();
        progressDialog = null;
        signInCall = null;

        passwordInput.clearText();

        if(sessionKey == null){
            showWrongInputError();
        }else{
            ((AuthenticationActivity) getActivity()).enter(sessionKey);
        }
    }

    private void showWrongInputError() {
        messageView.setText(R.string.wrongMailOrPassword);
    }

    private class SignInCallBack extends CallbackWithErrorDialog<String> {

        public SignInCallBack() {
            super(getActivity());
        }

        @Override
        public void handleSuccessful(Response<String> response) {
            signInFinished(response.body());
        }
    }


}
