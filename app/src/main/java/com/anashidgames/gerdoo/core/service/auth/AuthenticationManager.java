package com.anashidgames.gerdoo.core.service.auth;



import android.content.Context;
import android.content.Intent;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.call.PsychoCall;
import com.anashidgames.gerdoo.core.service.call.PsychoCallBack;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
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

    public Call<SignUpInfo> signUp(String email, String password) {
        return service.signUp(new SignUpParameters(email, password));
    }

    public Call<AuthenticationInfo> signIn(String email, String password) {
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

    public void startAuthenticationActivity() {
        Intent intent = AuthenticationActivity.newIntent(context, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
}