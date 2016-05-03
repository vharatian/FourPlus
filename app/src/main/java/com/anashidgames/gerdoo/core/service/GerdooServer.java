package com.anashidgames.gerdoo.core.service;

import android.content.Context;
import android.net.Uri;

import com.anashidgames.gerdoo.core.service.auth.AuthenticationInterceptor;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.parameters.LeaderBoardParams;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardItem;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.utils.PsychoUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String AUTHENTICATION_ID = "57163ee5e4b0cad8c4dd1844";
    public static final String AUTHENTICATION_KEY = "57163ee5e4b093ed2821a011";

    public static final GerdooServer INSTANCE = new GerdooServer(AUTHENTICATION_ID, AUTHENTICATION_KEY);

    public static final String HOST = "http://api1.backtory.com/";
    public static final String GAME_ID = "57163f07e4b0cad8c4dd184c";
    private GerdooService mockService;
    private GerdooService realService;
    private GerdooService service;

    private AuthenticationManager authenticationManager;


    private GerdooServer(String authenticationId, String authenticationKey) {

        authenticationManager = new AuthenticationManager(HOST, authenticationId, authenticationKey);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationManager))
                .addInterceptor(logging);

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
        authenticationManager.setContext(context);
    }

    public Call<SignUpInfo> signUp(String email, String password) {
        return authenticationManager.signUp(email, password);
    }

    public Call<Boolean> sendForgetPasswordMail(String email) {
        return authenticationManager.sendForgetPasswordMail(email);
    }

    public Call<AuthenticationInfo>  signIn(String email, String password) {
        return authenticationManager.signIn(email, password);
    }

    public Call<List<HomeItem>> getHome() {
        return service.getHome();
    }

    public Call<List<CategoryTopic>> getCategoryTopics(String url) {
        return service.getCategoryTopics(PsychoUtils.fixUrl(url));
    }

    public Call<List<Category>> getCategories(String url) {
        return service.getCategories(PsychoUtils.fixUrl(url));
    }

    public Call<PsychoListResponse<Rank>> getRanking(String url) {
        return new ConverterCall<PsychoListResponse<Rank>, LeaderBoardResponse>(mockService.getRanking(GAME_ID, new LeaderBoardParams())) {
            @Override
            public PsychoListResponse<Rank> convert(LeaderBoardResponse data) {
                List<Rank> ranks = new ArrayList<>();
                List<LeaderBoardItem> items = data.getUsersProfile();

                if (items != null)
                    for (int i=0; i<items.size(); i++){
                        ranks.add(new Rank(items.get(i), i+1));
                    }

                return new PsychoListResponse<>(ranks, null);
            }
        };
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
}
