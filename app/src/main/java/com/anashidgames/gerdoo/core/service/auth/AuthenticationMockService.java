package com.anashidgames.gerdoo.core.service.auth;

import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.CompleteRegistrationParameters;
import com.anashidgames.gerdoo.core.service.model.GustSignUpInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.parameters.SignUpParameters;

import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by psycho on 4/23/16.
 */
public class AuthenticationMockService implements AuthenticationService {
    private BehaviorDelegate<AuthenticationService> behaviorDelegate;
    private Random random;

    public AuthenticationMockService(BehaviorDelegate<AuthenticationService> behaviorDelegate) {
        this.behaviorDelegate = behaviorDelegate;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public Call<SignUpInfo> signUp(SignUpParameters parameters) {
        SignUpInfo info;
        if(parameters.getEmail().equals("test@test.com") && parameters.getPassword().equals("test")){
            info = new SignUpInfo(parameters.getEmail());
        }else{
            info = new SignUpInfo(null);
        }

        return behaviorDelegate.returningResponse(info).signUp(parameters);
    }

    @Override
    public Call<Boolean> sendForgetPasswordMail(String email) {
        return behaviorDelegate.returningResponse(random.nextBoolean()).sendForgetPasswordMail(email);
    }

    @Override
    public Call<AuthenticationInfo> refreshToken(String refreshToken) {
        AuthenticationInfo info;
        if(random.nextBoolean()){
            info = new AuthenticationInfo(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 4000);
        }else{
            info = new AuthenticationInfo(null, null, 0);
        }

        return behaviorDelegate.returningResponse(info).refreshToken(refreshToken);
    }

    @Override
    public Call<GustSignUpInfo> gustSignUp() {
        return null;
    }

    @Override
    public Call<SignUpInfo> completeRegistration(CompleteRegistrationParameters signUpParameters) {
        return null;
    }

    @Override
    public Call<AuthenticationInfo> signIn(String email, String password) {
        AuthenticationInfo info;
        if(email.equals("test@test.com") && password.equals("test")){
            info = new AuthenticationInfo(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 1000);
        }else{
            info = new AuthenticationInfo(null, null, 0);
        }

        return behaviorDelegate.returningResponse(info).signIn(email, password);
    }
}
