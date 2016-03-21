package com.rahnema.gerdoo.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;

import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends Fragment implements PsychoChangeable.StateChangedListener {

    public static final int STATE_NOT_SPECIFIED = -1;

    public static SignInFragment newINstance(){
        return new SignInFragment();
    }

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private Button signInBtn;

    // This properties are for tracking inputs validity
    private AuthStateChangeListener stateChangeListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {

        messageView = (TextView) rootView.findViewById(R.id.message);

        stateChangeListener = new AuthStateChangeListener(this);

        initMailInput(rootView);
        initPasswordInput(rootView);

        initSignInBtn(rootView);
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
            doSignIn();
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

    private void doSignIn() {

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
        setSignInBtnState(newState);
    }
}
