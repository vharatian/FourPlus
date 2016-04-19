package com.anashidgames.gerdoo.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.google.gson.Gson;

import java.util.UUID;

/**
 * Created by psycho on 3/22/16.
 */
public class DataHelper {

    public static final String FIRST_TIME = "firstTime";
    public static final String SESSION_KEY = "sessionKey";
    public static final String AUTHENTICATION_INFO = "authenticationInfo";

    private Context context;
    private SharedPreferences preferences;
    private Gson gson;

    public DataHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        gson = new Gson();
    }

    private void writeBoolean(String key, boolean b){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    private void writeString(String key, String s){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public boolean isFirstTime(){
        return preferences.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(){
        writeBoolean(FIRST_TIME, false);
    }

    public AuthenticationInfo getAuthenticationInfo(){
        String json = preferences.getString(AUTHENTICATION_INFO, "{}");
        return gson.fromJson(json, AuthenticationInfo.class);
    }

    public void setAuthenticationInfo(AuthenticationInfo info){
        String json = gson.toJson(info);
        writeString(AUTHENTICATION_INFO, json);
    }

    public void setAnonymousAuthenticationInfo(){
        AuthenticationInfo info = new AuthenticationInfo(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 10000000);
        setAuthenticationInfo(info);
    }
}
