package com.rahnema.gerdoo.auth;

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
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;
import com.rahnema.gerdoo.view.validation.validator.RepeatPasswordValidator;


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

    // This properties are for tracking inputs validity
    private AuthStateChangeListener stateChangeListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

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

    }

    private void initSignInBtn(View rootView) {
        rootView.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity) getActivity()).changeFragment(SignInFragment.newINstance());
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
            doSignUp();
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

    private void doSignUp() {

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
}
