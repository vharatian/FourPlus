package com.anashidgames.gerdoo.pages.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import com.anashidgames.gerdoo.pages.auth.view.validator.EmailValidator;
import com.anashidgames.gerdoo.R;

import retrofit2.Call;

public class ForgetPasswordFragment extends FormFragment {

    public static ForgetPasswordFragment newInstance(){
        return new ForgetPasswordFragment();
    }

    //Views
    private ValidatableInput mailInput;
    private AlertDialog okDialog;

    private GerdooServer server;


    public ForgetPasswordFragment() {
        super(R.id.message, R.id.sendMailBtn);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);

        server = new GerdooServer();

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        initMailInput(rootView);
    }

    private void initMailInput(View rootView) {
        mailInput = (ValidatableInput) rootView.findViewById(R.id.mail);
        mailInput.setValidator(new EmailValidator());
        mailInput.setHintMessage(R.string.email);
        mailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mailInput.setErrorMessage(R.string.emailIsNeeded, R.string.mailIsInvalid);

        addInput(mailInput);

    }


    @Override
    protected Call callServer() {
        String email = mailInput.getText();
        return server.sendForgetPasswordMail(email);
    }

    @Override
    protected void submitted(Object result) {
        if(result == null || !((Boolean) result)){
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
        showMessage(R.string.emailNotFound);
        mailInput.showErrorStatusIcon();
    }

}