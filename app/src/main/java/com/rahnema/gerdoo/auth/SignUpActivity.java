package com.rahnema.gerdoo.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;
import com.rahnema.gerdoo.view.validation.validator.RepeatPasswordValidator;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final int STATE_NOT_SPECIFIED = -1;

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private ValidatableInput passwordRepeatInput;
    private Button signUpBtn;

    // These properties are for tracking inputs validity
    private int inputsState;
    private InnerStateChangeListener stateChangeListener;
    private Map<ValidatableInput, Integer> states = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    private void initViews() {

        messageView = (TextView) findViewById(R.id.message);

        stateChangeListener = new InnerStateChangeListener();

        initMailInput();
        initPasswordInput();
        initPasswordRepeat();

        initSignUpBtn();
        initSignInBtn();

        checkInputsValidity();
    }

    private void initSignInBtn() {
        findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
    }

    private void initSignUpBtn() {
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpRequested();
            }
        });
    }

    private void initMailInput() {
        mailInput = (ValidatableInput) findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        stateChangeListener.addEditText(mailInput);
        mailInput.setHintMessage(R.string.email);
//        mailInput.setRightIcon(R.drawable.google);
    }

    private void initPasswordInput() {
        passwordInput = (ValidatableInput) findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        stateChangeListener.addEditText(passwordInput);
        passwordInput.setHintMessage(R.string.password);
    }

    private void initPasswordRepeat() {
        passwordRepeatInput = (ValidatableInput) findViewById(R.id.passwordRepeat);
        passwordRepeatInput.setValidator(new RepeatPasswordValidator(passwordInput));
        stateChangeListener.addEditText(passwordRepeatInput);
        passwordRepeatInput.setHintMessage(R.string.passwordRepeat);
    }


    private void onSignUpRequested() {
        if (inputsState != ValidatableInput.STATE_VALID){
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
        return !passwordInput.getText().toString().equals(passwordRepeatInput.getText().toString());
    }

    private void doSignUp() {

    }

    private int checkInputsValidity() {
        int newState = ValidatableInput.STATE_VALID;
        for(ValidatableInput key: states.keySet()){
            if(states.get(key) != ValidatableInput.STATE_VALID){
                newState = ValidatableInput.STATE_INVALID;
            }
        }

        if(inputsState != newState) {
            inputsState = newState;
            setSignUpBtnState(newState);
        }
        return newState;
    }

    private void setSignUpBtnState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            signUpBtn.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            signUpBtn.setBackgroundResource(R.drawable.dark_button_background_invalid);
        }
    }


    private class InnerStateChangeListener implements PsychoChangeable.StateChangedListener {

        @Override
        public void stateChanged(PsychoChangeable changeable, int newState) {
            states.put((ValidatableInput) changeable, newState);

            checkInputsValidity();
        }

        protected void addEditText(PsychoChangeable editText){
            states.put((ValidatableInput) editText, STATE_NOT_SPECIFIED);
            editText.setStateChangedListener(this);
        }
    }
}
