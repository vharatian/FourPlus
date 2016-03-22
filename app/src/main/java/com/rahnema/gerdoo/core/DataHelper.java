package com.rahnema.gerdoo.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by psycho on 3/22/16.
 */
public class DataHelper {

    public static final String FIRST_TIME = "firstTime";
    public static final String SESSION_KEY = "sessionKey";

    private Context context;
    private SharedPreferences preferences;

    public DataHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
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

    public String getSessionKey() {
        return preferences.getString(SESSION_KEY, null);
    }

    public String createAnonymousSessionKey() {
        String sessionKey = UUID.randomUUID().toString();
        return sessionKey;
    }

    public void setSessionKey(String sessionKey){
        writeString(SESSION_KEY, sessionKey);
    }
}
