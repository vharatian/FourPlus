package com.anashidgames.gerdoo.core.service.auth;

import android.util.Log;

import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by psycho on 4/23/16.
 */
public class AuthenticationInterceptor implements Interceptor {

    public static final String AUTHORIZATION = "Authorization";
    private AuthenticationManager authenticationManager;

    public AuthenticationInterceptor(AuthenticationManager authenticationServer) {
        this.authenticationManager = authenticationServer;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        AuthenticationInfo info = authenticationManager.checkInfo(true);

        if (info != null) {
            String accessToken = info.getAccessToken();
            if (info.getTokenType() != null && !info.getTokenType().isEmpty())
                accessToken = info.getTokenType() + " " + accessToken;

            request = request.newBuilder()
                    .header(AUTHORIZATION, accessToken)
                    .build();
        }

        return chain.proceed(request);
    }
}
