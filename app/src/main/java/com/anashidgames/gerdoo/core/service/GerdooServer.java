package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SignInParameters;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.utils.PsychoUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String HOST = "http://192.168.0.99:8043";
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
        try {
            return service.signIn(new SignInParameters(email, password));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
        return service.getRanking(url);
    }
}
