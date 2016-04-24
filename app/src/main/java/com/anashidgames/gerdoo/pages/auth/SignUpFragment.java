package com.anashidgames.gerdoo.pages.auth;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.PsychoCallBack;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.pages.auth.view.validator.EmailValidator;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.auth.view.ValidatableInput;
import com.anashidgames.gerdoo.pages.auth.view.validator.PasswordValidator;
import com.anashidgames.gerdoo.pages.auth.view.validator.RepeatPasswordValidator;

import br.com.dina.oauth.instagram.InstagramApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends FormFragment {

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 2359;
    private static final String CLIENT_ID = "426177ad892f444bbec4c0e33f87056b";
    private static final String CLIENT_SECRET = "f7d46cfe53764f76936ed00b7a2d769a";
    private static final String CALLBACK_URL = "http://gerdoo.anashidgames.com/auth";

    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    //Views
    private ValidatableInput mailInput;
    private ValidatableInput passwordInput;
    private ValidatableInput passwordRepeatInput;

    private DataHelper dataHelper;
    private GerdooServer server;

    private GoogleApiClient googleApiClient;
    private InstagramApp instagramApp;

    public SignUpFragment() {
        super(R.id.message, R.id.signUpBtn);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        dataHelper = new DataHelper(getActivity());
        server = GerdooServer.INSTANCE;

        initViews(rootView);
        return rootView;
    }

    @Override
    public void onStop() {
        if(googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
            googleApiClient = null;
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    protected void callServer(Callback callback) {
        String email = mailInput.getText();
        String password = passwordInput.getText();
        Call<SignUpInfo> call = server.signUp(email, password);
        call.enqueue(new SignUpCallBack((PsychoCallBack) callback, email, password));
    }

    @Override
    protected void submitted(Object result) {
        passwordInput.clearText();
        passwordRepeatInput.clearText();


        AuthenticationInfo info = (AuthenticationInfo) result;
        if(info == null || !info.isValid()){
            showRepeatedMailError();
        }else{

            ((AuthenticationActivity) getActivity()).enter();
        }
    }

    private void initViews(View rootView) {
        initMailInput(rootView);
        initPasswordInput(rootView);
        initPasswordRepeat(rootView);

        initSignInBtn(rootView);

        initGoogleSignIn(rootView);
        initInstagramSignIn(rootView);

        rootView.findViewById(R.id.enterAnonymous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAnonymous();
            }
        });

    }

    private void initInstagramSignIn(View rootView) {
        instagramApp = new InstagramApp(getContext(), CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
        instagramApp.setListener(new InstagramCallBack());

        rootView.findViewById(R.id.signInWidthInstagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramApp.authorize();
            }
        });
    }

    private void initGoogleSignIn(View rootView) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() , new GoogleOnConnectionFailListener() /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        rootView.findViewById(R.id.signInWidthGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigInWithGoogle();
            }
        });
    }

    private void sigInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        getActivity().startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);

    }

    private void enterAnonymous() {
        dataHelper.setAnonymousAuthenticationInfo();
        ((AuthenticationActivity) getActivity()).enter();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            ((AuthenticationActivity) getActivity()).enter();
        }

    }

    private void handleInstagramSigInResult(boolean success) {
        if(success){
            ((AuthenticationActivity) getActivity()).enter();
        }
    }

    private class GoogleOnConnectionFailListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }

    private class InstagramCallBack implements InstagramApp.OAuthAuthenticationListener {
        @Override
        public void onSuccess() {
            handleInstagramSigInResult(true);
        }

        @Override
        public void onFail(String error) {
            handleInstagramSigInResult(false);
        }
    }

    private class SignUpCallBack extends PsychoCallBack<SignUpInfo> {

        private final PsychoCallBack callback;
        private String email;
        private String password;

        public SignUpCallBack(PsychoCallBack callback, String email, String password) {
            this.callback = callback;
            this.email = email;
            this.password = password;
        }

        @Override
        public void handleSuccessful(SignUpInfo data) {
            Call<AuthenticationInfo> call = GerdooServer.INSTANCE.signIn(email, password);
            call.enqueue(callback);
        }

        @Override
        public void handleFailure(Call<SignUpInfo> call, Throwable t) {
            callback.handleFailure(call, t);
        }

        @Override
        public void handleServerError(Response<SignUpInfo> response) {
            callback.handleServerError(response);
        }
    }
}
