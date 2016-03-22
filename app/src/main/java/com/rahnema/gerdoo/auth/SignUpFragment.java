package com.rahnema.gerdoo.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.core.DataHelper;
import com.rahnema.gerdoo.core.service.GerdooServer;
import com.rahnema.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;
import com.rahnema.gerdoo.view.validation.validator.RepeatPasswordValidator;

import retrofit2.Call;
import retrofit2.Response;


public class SignUpFragment extends Fragment implements PsychoChangeable.StateChangedListener {



    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private ValidatableInput passwordRepeatInput;
    private Button signUpBtn;

    private AuthStateChangeListener stateChangeListener;

    private DataHelper dataHelper;
    private GerdooServer server;

    private ProgressDialog progressDialog;
    private Call<String> signUpCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        dataHelper = new DataHelper(getActivity());
        server = new GerdooServer();

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {

        messageView = (TextView) rootView.findViewById(R.id.message);

        stateChangeListener = new AuthStateChangeListener(this);

        initMailInput(rootView);
        initPasswordInput(rootView);
        initPasswordRepeat(rootView);

        initSignUpBtn(rootView);
        initSignInBtn(rootView);

        rootView.findViewById(R.id.enterAnonymous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAnonymous();
            }
        });

    }

    private void enterAnonymous() {
        String sessionKey = dataHelper.createAnonymousSessionKey();
        ((AuthenticationActivity) getActivity()).enter(sessionKey);
    }

    private void initSignInBtn(View rootView) {
        rootView.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity) getActivity()).changeFragment(SignInFragment.newInstance());
            }
        });
    }

    private void initSignUpBtn(View rootView) {
        signUpBtn = (Button) rootView.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpRequested();
            }
        });
    }

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        stateChangeListener.addEditText(mailInput);
        mailInput.setHintMessage(R.string.email);
        mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

    }

    private void initPasswordInput(View rootView) {
        passwordInput = (ValidatableInput) rootView.findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        stateChangeListener.addEditText(passwordInput);
        passwordInput.setHintMessage(R.string.password);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initPasswordRepeat(View rootView) {
        passwordRepeatInput = (ValidatableInput) rootView.findViewById(R.id.passwordRepeat);
        passwordRepeatInput.setValidator(new RepeatPasswordValidator(passwordInput));
        stateChangeListener.addEditText(passwordRepeatInput);
        passwordRepeatInput.setHintMessage(R.string.passwordRepeat);
        passwordRepeatInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    private void onSignUpRequested() {
        if (stateChangeListener.getState() != ValidatableInput.STATE_VALID){
            showErrors();
        }else{
            messageView.setText("");
            signUp();
        }
    }

    private void showErrors() {
        int message = -1;
        if(passwordInput.getState() == ValidatableInput.STATE_EMPTY){
            passwordRepeatInput.showErrorStatusIcon();
        }else if(passwordRepeatDosNotMatch()){
            message = R.string.passwordRepeatDoesNotMatch;
            passwordRepeatInput.showErrorStatusIcon();
        }

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

    private boolean passwordRepeatDosNotMatch() {
        return !passwordInput.getText().equals(passwordRepeatInput.getText());
    }

    private void cancelRequesting(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }

        if(signUpCall != null){
            signUpCall.cancel();
        }
    }

    private void signUp() {
        cancelRequesting();
        progressDialog = ProgressDialog.show(getActivity(),
                getString(R.string.signUp),
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

        signUpCall = server.signUp(email, password);
        signUpCall.enqueue(new SignUpCallBack());
    }

    private void signUpFinished(String sessionKey) {
        progressDialog.dismiss();
        progressDialog = null;
        signUpCall = null;

        passwordInput.clearText();
        passwordRepeatInput.clearText();

        if(sessionKey == null){
            showRepeatedMailError();
        }else{
            ((AuthenticationActivity) getActivity()).enter(sessionKey);
        }
    }

    private void showRepeatedMailError() {
        mailInput.showErrorStatusIcon();
        messageView.setText(R.string.repeatedMail);
    }

    private void setSignUpBtnState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            signUpBtn.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            signUpBtn.setBackgroundResource(R.drawable.dark_button_background_invalid);
        }
    }

    @Override
    public void stateChanged(PsychoChangeable editText, int newState) {
        setSignUpBtnState(newState);
    }

    private class SignUpCallBack extends CallbackWithErrorDialog<String> {

        public SignUpCallBack() {
            super(getActivity());
        }

        @Override
        public void handleSuccessful(Response<String> response) {
            signUpFinished(response.body());
        }
    }
}
