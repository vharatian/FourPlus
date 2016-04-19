package com.anashidgames.gerdoo.core.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpParameters;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.pages.auth.AuthenticationActivity;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.utils.PsychoUtils;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String AUTHENTICATION_ID = "570523cde4b036bd289cc8ae";
    public static final String AUTHENTICATION_KEY = "570523cde4b08821522baab3";

    public static final GerdooServer INSTANCE = new GerdooServer(AUTHENTICATION_ID, AUTHENTICATION_KEY);

    public static final String HOST = "http://192.168.0.99:8585";
    private GerdooService mockService;
    private GerdooService realService;
    private GerdooService service;

    private Context context;
    private String authenticationKey;
    private String authenticationId;
    private DataHelper dataHelper;


    private GerdooServer(String authenticationId, String authenticationKey) {

        this.authenticationId = authenticationId;
        this.authenticationKey = authenticationKey;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).build();

        realService = retrofit.create(GerdooService.class);
        BehaviorDelegate<GerdooService> behaviorDelegate = mockRetrofit.create(GerdooService.class);
        mockService = new MockService(behaviorDelegate);

        service = mockService;
    }

    public void setContext(Context context) {
        this.context = context;
        dataHelper = new DataHelper(context);
    }

    public boolean checkSession() {
        AuthenticationInfo info = dataHelper.getAuthenticationInfo();
        if (info == null || !info.isValid()) {
            Intent intent = AuthenticationActivity.newIntent(context);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }else if (info.expired()){
            return false;
        }

        return true;
    }

    public Call<SignUpInfo> signUp(String email, String password) {
        return realService.signUp(authenticationId, authenticationKey, new SignUpParameters(email, password));
    }

    public Call<Boolean> sendForgetPasswordMail(String email) {
        return service.sendForgetPasswordMail(email);
    }

    public void signIn(String email, String password, Callback<AuthenticationInfo> callback) {
        Call<AuthenticationInfo> call = realService.signIn(authenticationId, authenticationKey, email, password);
        call.enqueue(new AuthenticationCallback(callback));
    }

    public Call<List<HomeItem>> getHome() {
//        if (checkSession())
            return service.getHome();

//        return null;
    }

    public Call<List<CategoryTopic>> getCategoryTopics(String url) {
        return service.getCategoryTopics(PsychoUtils.fixUrl(url));
    }

    public Call<List<Category>> getCategories(String url) {
        return service.getCategories(PsychoUtils.fixUrl(url));
    }

    public Call<PsychoListResponse<Rank>> getRanking(String url) {
        return service.getRanking(url);
    }

    public Call<UserInfo> getUserInfo() {
        return service.getUserInfo();
    }

    public Call<ProfileInfo> getProfile(Long userId) {
        return service.getProfile(userId);
    }

    public Call<List<Friend>> getFriends(Long userId) {
        return service.getFriends(userId);
    }

    public Call<List<Gift>> getGifts(Long userId) {
        return service.getGifts(userId);
    }

    public Call<FollowToggleResponse> toggleFollow(String followToggleUrl) {
        return service.toggleFollow(followToggleUrl);
    }

    public Call<ChangeImageResponse> changeImage(String url, Uri selectedImage) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), selectedImage.getPath());
        return service.changeImage(url, body);
    }

    private class AuthenticationCallback implements Callback<AuthenticationInfo> {

        private final Callback<AuthenticationInfo> callBack;

        public AuthenticationCallback(Callback<AuthenticationInfo> callback) {
            this.callBack = callback;
        }

        @Override
        public void onResponse(Call<AuthenticationInfo> call, Response<AuthenticationInfo> response) {
            callBack.onResponse(call, response);

            if (response.isSuccessful() && response.body().isValid())
                dataHelper.setAuthenticationInfo(response.body());
        }

        @Override
        public void onFailure(Call<AuthenticationInfo> call, Throwable t) {
            callBack.onFailure(call, t);
        }
    }
}
