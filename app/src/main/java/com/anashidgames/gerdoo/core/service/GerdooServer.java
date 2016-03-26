package com.anashidgames.gerdoo.core.service;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String HOST = "https://api.github.com";
    private GerdooService mockService;
    private GerdooService realService;
    private GerdooService service;

    public GerdooServer() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).build();

        realService = retrofit.create(GerdooService.class);
        BehaviorDelegate<GerdooService> behaviorDelegate = mockRetrofit.create(GerdooService.class);
        mockService = new MockService(behaviorDelegate);

        service = mockService;
    }


    public Call<Boolean> checkSession(String sessionKey) {
        return service.checkSession(sessionKey);
    }

    public Call<String> signUp(String email, String password) {
        return service.signUp(email, password);
    }

    public Call<Boolean> sendForgetPasswordMail(String email) {
        return service.sendForgetPasswordMail(email);
    }

    public Call<String> signIn(String email, String password) {
        return service.signIn(email, password);
    }
}
