package com.rahnema.gerdoo.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rahnema.gerdoo.R;
import com.rahnema.gerdoo.view.validation.PsychoChangeable;
import com.rahnema.gerdoo.view.validation.ValidatableInput;
import com.rahnema.gerdoo.view.validation.validator.EmailValidator;
import com.rahnema.gerdoo.view.validation.validator.PasswordValidator;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    public static final int STATE_NOT_SPECIFIED = -1;

    //Views
    private TextView messageView;
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private Button signInBtn;

    // These properties are for tracking inputs validity
    private int inputsState;
    private InnerStateChangeListener stateChangeListener;
    private Map<ValidatableInput, Integer> states = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
    }

    private void initViews() {

        messageView = (TextView) findViewById(R.id.message);

        stateChangeListener = new InnerStateChangeListener();

        initMailInput();
        initPasswordInput();

        initSignInBtn();

        checkInputsValidity();
    }

    private void initSignInBtn() {
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInRequested();
            }
        });
    }

    private void initMailInput() {
        mailInput = (ValidatableInput) findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        stateChangeListener.addEditText(mailInput);
        mailInput.setHintMessage(R.string.email);
    }

    private void initPasswordInput() {
        passwordInput = (ValidatableInput) findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        stateChangeListener.addEditText(passwordInput);
        passwordInput.setHintMessage(R.string.password);
    }


    private void onSignInRequested() {
        if (inputsState != ValidatableInput.STATE_VALID){
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

    private int checkInputsValidity() {
        int newState = ValidatableInput.STATE_VALID;
        for(ValidatableInput key: states.keySet()){
            if(states.get(key) != ValidatableInput.STATE_VALID){
                newState = ValidatableInput.STATE_INVALID;
            }
        }

        if(inputsState != newState) {
            inputsState = newState;
            setSignInBtnState(newState);
        }
        return newState;
    }

    private void setSignInBtnState(int state) {
        if(state == ValidatableInput.STATE_VALID){
            signInBtn.setBackgroundResource(R.drawable.dark_button_background_valid);
        }else{
            signInBtn.setBackgroundResource(R.drawable.dark_button_background_invalid);
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
