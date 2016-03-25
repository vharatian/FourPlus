package com.rahnema.gerdoo.core.service;

import android.provider.ContactsContract;

import java.util.EnumMap;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by psycho on 3/22/16.
 */
class MockService implements GerdooService {


    private BehaviorDelegate<GerdooService> behaviorDelegate;
    private Random random;

    public MockService(BehaviorDelegate<GerdooService> behaviorDelegate) {
        this.behaviorDelegate = behaviorDelegate;
        random = new Random(System.currentTimeMillis());
    }


    @Override
    public Call<Boolean> checkSession(String sessionKey) {
        return behaviorDelegate.returningResponse(random.nextBoolean()).checkSession(sessionKey);
    }

    @Override
    public Call<String> signUp(String email, String password) {
        String sessionKey = null;
        if(email.equals("test@test.com") && password.equals("test")){
            sessionKey = UUID.randomUUID().toString();
        }

        return behaviorDelegate.returningResponse(sessionKey).signUp(email, password);
    }

    @Override
    public Call<Boolean> sendForgetPasswordMail(String email) {
        return behaviorDelegate.returningResponse(random.nextBoolean()).checkSession(email);
    }

    @Override
    public Call<String> signIn(String email, String password) {
        String sessionKey = null;
        if(email.equals("test@test.com") && password.equals("test")){
            sessionKey = UUID.randomUUID().toString();
        }

        return behaviorDelegate.returningResponse(sessionKey).signIn(email, password);
    }
}
