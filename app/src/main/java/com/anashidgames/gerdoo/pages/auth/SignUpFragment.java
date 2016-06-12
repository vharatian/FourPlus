package com.anashidgames.gerdoo.pages.auth;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.pages.auth.view.validator.EmailValidator;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import com.anashidgames.gerdoo.pages.auth.view.validator.PasswordValidator;
import com.anashidgames.gerdoo.pages.auth.view.validator.PhoneNumberValidator;
import com.anashidgames.gerdoo.pages.auth.view.validator.RepeatPasswordValidator;

import retrofit2.Callback;


public class SignUpFragment extends FormFragment {

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 2359;
    public static final int ANONYMOUS_FORM_ID = 2;


    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    //Views
    private ValidatableInput mailInput;
    private ValidatableInput phoneNumberInput;
    private ValidatableInput passwordInput;
    private ValidatableInput passwordRepeatInput;

    private DataHelper dataHelper;
    private AuthenticationManager authenticationManager;

    public SignUpFragment() {
        super(R.id.message, R.id.signUpBtn);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        dataHelper = new DataHelper(getActivity());
        authenticationManager = GerdooServer.INSTANCE.getAuthenticationManager();

        initViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void callServer(int formId, Callback callback) {
        if(formId == ANONYMOUS_FORM_ID){
            authenticationManager.gustSignUp(callback);
        }else  {
            String email = mailInput.getText();
            String phoneNumber = phoneNumberInput.getText();
            String password = passwordInput.getText();
            authenticationManager.signUp(email, phoneNumber, password, callback);
        }
    }

    @Override
    protected void submitted(Object result) {
        passwordInput.clearText();
        passwordRepeatInput.clearText();


        AuthenticationInfo info = (AuthenticationInfo) result;
        if(info == null || !info.isValid()){
            showRepeatedMailError();
        }else{

            ((AuthenticationActivity) getActivity()).notifyAuthenticated();
        }
    }

    private void initViews(View rootView) {
        initMailInput(rootView);
        initPhoneNumber(rootView);
        initPasswordInput(rootView);
        initPasswordRepeat(rootView);

        initSignInBtn(rootView);

        rootView.findViewById(R.id.enterAnonymous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAnonymous();
            }
        });

    }

    private void enterAnonymous() {
        submitForm(ANONYMOUS_FORM_ID);
    }

    private void initSignInBtn(View rootView) {
        rootView.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity) getActivity()).changeFragment(SignInFragment.newInstance());
            }
        });
    }

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        mailInput.setHintMessage(R.string.email);
        mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mailInput.setErrorMessage(R.string.emailIsNeeded, R.string.wrongMail);

        addInput(mailInput);
    }

    private void initPhoneNumber(View rootView) {
        phoneNumberInput = (ValidatableInput) rootView.findViewById(R.id.phoneNumber);
        phoneNumberInput.setValidator(new PhoneNumberValidator());
        phoneNumberInput.setHintMessage(R.string.phoneNumber);
        phoneNumberInput.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneNumberInput.setErrorMessage(R.string.phoneNumberIsNeeded, R.string.wrongPhoneNumber);

        addInput(phoneNumberInput);
    }

    private void initPasswordInput(View rootView) {
        passwordInput = (ValidatableInput) rootView.findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        passwordInput.setHintMessage(R.string.password);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setErrorMessage(R.string.passwordIsNeeded, R.string.passwordSouldBeLonger);

        addInput(passwordInput);
    }

    private void initPasswordRepeat(View rootView) {
        passwordRepeatInput = (ValidatableInput) rootView.findViewById(R.id.passwordRepeat);
        passwordRepeatInput.setValidator(new RepeatPasswordValidator(passwordInput));
        passwordRepeatInput.setHintMessage(R.string.passwordRepeat);
        passwordRepeatInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordRepeatInput.setErrorMessage(R.string.passwordRepeatDoesNotMatch, R.string.passwordRepeatDoesNotMatch);

        addInput(passwordRepeatInput);
    }

    private void showRepeatedMailError() {
        mailInput.showErrorStatusIcon();
        showMessage(R.string.repeatedMail);
    }
}
