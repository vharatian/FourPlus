package com.anashidgames.gerdoo.pages.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import com.anashidgames.gerdoo.pages.auth.view.validator.EmailValidator;
import com.anashidgames.gerdoo.pages.auth.view.validator.PasswordValidator;

import retrofit2.Call;
import retrofit2.Callback;

public class SignInFragment extends FormFragment {


    public static SignInFragment newInstance(){
        return new SignInFragment();
    }

    //Views
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;

    private GerdooServer server;

    public SignInFragment() {
        super(R.id.message, R.id.signInBtn);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        server = GerdooServer.INSTANCE;

        initViews(rootView);

        return rootView;
    }

    @Override
    protected void callServer(Callback callback) {
        String email = mailInput.getText();
        String password = passwordInput.getText();
        server.signIn(email, password, callback);
    }

    @Override
    protected void submitted(Object result) {
        AuthenticationInfo info = (AuthenticationInfo) result;
        if(info == null || !info.isValid()){
            showWrongInputError();
        }else{
            ((AuthenticationActivity) getActivity()).enter();
        }

        passwordInput.clearText();
    }

    private void initViews(View rootView) {

        initMailInput(rootView);
        initPasswordInput(rootView);

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

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        mailInput.setHintMessage(R.string.email);
        mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mailInput.setErrorMessage(R.string.emailIsNeeded, R.string.wrongMail);

        addInput(mailInput);
    }

    private void initPasswordInput(View rootView) {
        passwordInput = (ValidatableInput) rootView.findViewById(R.id.password);
        passwordInput.setValidator(new PasswordValidator());
        passwordInput.setHintMessage(R.string.password);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setErrorMessage(R.string.passwordIsNeeded, R.string.passwordSouldBeLonger);

        addInput(passwordInput);
    }


    private void showWrongInputError() {
        showMessage(R.string.wrongMailOrPassword);
    }

}
