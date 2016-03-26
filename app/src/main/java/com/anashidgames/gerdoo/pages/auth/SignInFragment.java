package com.anashidgames.gerdoo.pages.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.view.validation.PsychoChangeable;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.view.validation.ValidatableInput;
import com.anashidgames.gerdoo.view.validation.validator.EmailValidator;
import com.anashidgames.gerdoo.view.validation.validator.PasswordValidator;

import retrofit2.Call;
import retrofit2.Response;

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

        server = new GerdooServer();

        initViews(rootView);

        return rootView;
    }

    @Override
    protected Call callServer() {
        String email = mailInput.getText();
        String password = passwordInput.getText();
        return server.signIn(email, password);
    }

    @Override
    protected void submitted(Object result) {
        String sessionKey = (String) result;
        if(sessionKey == null){
            showWrongInputError();
        }else{
            ((AuthenticationActivity) getActivity()).enter(sessionKey);
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
