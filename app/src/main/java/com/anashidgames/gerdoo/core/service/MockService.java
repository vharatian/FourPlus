package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryItem;
import com.anashidgames.gerdoo.core.service.model.HomeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Url;
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

    @Override
    public Call<List<HomeItem>> getHome() {
        List<HomeItem> response = new ArrayList<>();

        String categoryBannerImageUrl = "https://pixabay.com/static/uploads/photo/2015/11/10/08/31/banner-1036483_960_720.jpg";
        double categoryBannerImageAspectRatio = 3.189;
        String urlBannerImageUrl = "https://i.imgsafe.org/5b75feb.jpg";
        double urlBannerImageAspectRatio = 8.372;
        String title = "عنوان   ";

        int dataSize = 10 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            HomeItem item;
            int rand = random.nextInt(4);
            if (rand == 0){
                item = new HomeItem(HomeItem.TYPE_BANNER_CATEGORY, categoryBannerImageUrl, categoryBannerImageAspectRatio, categoryBannerImageUrl);
            }else if (rand == 1){
                item = new HomeItem(HomeItem.TYPE_BANNER_URL, urlBannerImageUrl, urlBannerImageAspectRatio, urlBannerImageUrl);
            }else {
                item = new HomeItem(HomeItem.TYPE_CATEGORY, "", title + i);
            }

            response.add(item);
        }

        return behaviorDelegate.returningResponse(response).getHome();
    }

    @Override
    public Call<List<CategoryItem>> getCategoryItems(@Url String url) {
        List<CategoryItem> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/1163740.png",
                "https://i.imgsafe.org/1b41d04.png",
                "https://i.imgsafe.org/1e5ef6d.png",
                "https://i.imgsafe.org/2269d44.png",
                "https://i.imgsafe.org/20698a8.png",
                "https://i.imgsafe.org/190c8ee.png"
        );
        String title = "عنوان  ";

        int dataSize = 5 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));

            String itemTitle = title;
            while(random.nextBoolean()) {
                itemTitle += title;
            }

            itemTitle += i;

            response.add(new CategoryItem(imageUrl, itemTitle, ""));
        }

        return behaviorDelegate.returningResponse(response).getCategoryItems(url);
    }

    @Override
    public Call<List<Category>> getCategories(@Url String url) {
        List<Category> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/1ad850b.png",
                "https://i.imgsafe.org/1babef5.png",
                "https://i.imgsafe.org/1c744e3.png",
                "https://i.imgsafe.org/1d47d98.png"
        );
        String title = "عنوان  ";

        int dataSize = 10 + random.nextInt(30);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));

            String categoryTitle = title;
            while(random.nextBoolean()) {
                categoryTitle += title;
            }

            categoryTitle += i;

            response.add(new Category(categoryTitle, imageUrl, "", random.nextInt()%4 == 0));
        }


        return behaviorDelegate.returningResponse(response).getCategories(url);
    }
}