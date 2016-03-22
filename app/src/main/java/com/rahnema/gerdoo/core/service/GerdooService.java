package com.rahnema.gerdoo.core.service;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by psycho on 3/22/16.
 */
interface GerdooService {

    @GET("/")
    Call<Boolean> checkSession(String sessionKey);

    @GET("/")
    Call<String> signUp(String email, String password);

    @GET("/")
    Call<Boolean> sendForgetPasswordMail(String email);

    @GET("/")
    Call<String> signIn(String email, String password);
}
