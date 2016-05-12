package com.anashidgames.gerdoo.core.service.auth;



import android.content.Context;
import android.content.Intent;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.call.PsychoCall;
import com.anashidgames.gerdoo.core.service.call.PsychoCallBack;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.GustSignUpInfo;
import com.anashidgames.gerdoo.core.service.model.SignInInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.parameters.SignUpParameters;
import com.anashidgames.gerdoo.pages.auth.AuthenticationActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 4/23/16.
 */
public class AuthenticationManager {

    private AuthenticationService mockService;
    private AuthenticationService realService;
    private AuthenticationService service;
    private DataHelper dataHelper;

    private Gson gson;
    private Context context;
    private boolean isAuthenticationPageOnScreen = false;

    public AuthenticationManager(String host, String authenticationId, String authenticationKey) {
        gson = new Gson();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInfoInterceptor(authenticationId, authenticationKey))
                .addInterceptor(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).build();

        realService = retrofit.create(AuthenticationService.class);
        BehaviorDelegate<AuthenticationService> behaviorDelegate = mockRetrofit.create(AuthenticationService.class);
        mockService = new AuthenticationMockService(behaviorDelegate);

        service = realService;
    }

    public void signUp(String email, String phoneNumber, String password, Callback<AuthenticationInfo> callback) {
        Call<SignUpInfo> call = service.signUp(new SignUpParameters(email, phoneNumber, password));
        call.enqueue(new SignUpCallback(context, email, password, callback));
    }

    public void gustSignUp(Callback<AuthenticationInfo> callBack) {
        Call<GustSignUpInfo> call = service.gustSignUp();
        call.enqueue(new GustSignUpCallBack(context, callBack));
    }

    public Call<AuthenticationInfo> signIn(String email, String password) {
        SignInInfo info = new SignInInfo(email, password);
        dataHelper.setSignInInfo(info);

        Call<AuthenticationInfo> call = service.signIn(email, password);
        PsychoCall<AuthenticationInfo> psychoCall = new PsychoCall<>(call);
        psychoCall.addCallback(new AuthenticationCallback());

        return psychoCall;
    }

    public Call<Boolean> sendForgetPasswordMail(String email) {
        return mockService.sendForgetPasswordMail(email);
    }

    public synchronized AuthenticationInfo refreshToken() {
        try {
            AuthenticationInfo info = getAuthenticationInfo();
            Call<AuthenticationInfo> call = service.refreshToken(info.getRefreshToken());
            retrofit2.Response<AuthenticationInfo> response = call.execute();
            if (response.isSuccessful()) {
                checkAuthenticationInfo(response.body());
            }else{
                setAuthenticationInfo(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAuthenticationInfo(null);
        }

        return getAuthenticationInfo();
    }

    public void setContext(Context context) {
        this.context = context;
        dataHelper = new DataHelper(context);
    }

    private void checkAuthenticationInfo(AuthenticationInfo info) {
        AuthenticationInfo originalInfo = getAuthenticationInfo();
        if (info.getAccessToken() != null) {
            if(!info.hasRefreshToken() && originalInfo != null){
                info.setRefreshToken(originalInfo.getRefreshToken());
            }

            info.setCreationTime(System.currentTimeMillis());
            setAuthenticationInfo(info);
        }
    }

    private void setAuthenticationInfo(AuthenticationInfo info) {
        dataHelper.setAuthenticationInfo(info);
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return dataHelper.getAuthenticationInfo();
    }

    public synchronized void startAuthenticationActivity() {
        if (isAuthenticationPageOnScreen){
            return;
        }
        isAuthenticationPageOnScreen = true;
        Intent intent = AuthenticationActivity.newIntent(context, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public AuthenticationInfo checkInfo(boolean refresh){
        AuthenticationInfo info = getAuthenticationInfo();
        if (info == null || !info.isValid()) {
            startAuthenticationActivity();
            info = null;
        }else if (info.expired()){
            if (refresh){
                refreshToken();
                checkInfo(false);
            }else{
                info = null;
            }
        }

        return info;
    }

    public void signOut() {
        dataHelper.removeAuthenticationInfo();
        startAuthenticationActivity();
    }

    public void authenticationPageIsOnScreen(AuthenticationActivity activity){
        if (activity != null){
            isAuthenticationPageOnScreen = true;
        }
    }

    public void authenticationPageIsNotOnScreen(AuthenticationActivity activity){
        if (activity != null){
            isAuthenticationPageOnScreen = false;
        }
    }



    private class AuthenticationInfoInterceptor implements Interceptor {
        private static final String AUTHENTICATION_ID = "X-Backtory-Authentication-Id";
        private static final String AUTHENTICATION_KEY = "X-Backtory-Authentication-Key";

        private String authenticationId;
        private String authenticationKey;

        public AuthenticationInfoInterceptor(String authenticationId, String authenticationKey) {
            this.authenticationId = authenticationId;
            this.authenticationKey = authenticationKey;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .header(AUTHENTICATION_ID, authenticationId)
                    .header(AUTHENTICATION_KEY, authenticationKey)
                    .build();

            return chain.proceed(request);
        }
    }

    private class AuthenticationCallback extends PsychoCallBack<AuthenticationInfo> {

        @Override
        public void handleSuccessful(AuthenticationInfo data) {
            checkAuthenticationInfo(data);
        }
    }

    private class GustSignUpCallBack extends CallbackWithErrorDialog<GustSignUpInfo> {
        private final Callback<AuthenticationInfo> signInCallBack;

        public GustSignUpCallBack(Context context, Callback<AuthenticationInfo> signInCallBack) {
            super(context);
            this.signInCallBack = signInCallBack;
        }

        @Override
        public void handleSuccessful(GustSignUpInfo data) {
            dataHelper.setGustSignUpInfo(data);
            Call<AuthenticationInfo> call = signIn(data.getUserName(), data.getPassword());
            call.enqueue(signInCallBack);
        }
    }

    private class SignUpCallback extends CallbackWithErrorDialog<SignUpInfo> {
        private final Callback<AuthenticationInfo> signInCallBack;
        private final String username;
        private final String password;

        public SignUpCallback(Context context, String username, String password, Callback<AuthenticationInfo> callback) {
            super(context);
            this.username = username;
            this.password = password;
            this.signInCallBack = callback;
        }

        @Override
        public void handleSuccessful(SignUpInfo data) {
            dataHelper.setSignUpInfo(data);
            Call<AuthenticationInfo> call = signIn(username, password);
            call.enqueue(signInCallBack);
        }
    }
}