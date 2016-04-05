package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.view.CategoryTopicsRow;
import com.anashidgames.gerdoo.pages.home.view.CategoryView;
import com.anashidgames.gerdoo.utils.PsychoUtils;
import com.anashidgames.gerdoo.view.FitToWidthWithAspectRatioImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class HomeFragment extends Fragment {


    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private GerdooServer server;

    private LinearLayout mainLayout;

    private View progressView;
    private List<Call> calls = new ArrayList<>();


    private ViewGroup.LayoutParams layoutParams;
    private List<Integer> rowColors;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        server = new GerdooServer();

        initViews(rootView);
        rowColors = Arrays.asList(R.color.categoryItemsRowColor1, R.color.categoryItemsRowColor2);

        return rootView;
    }

    private void initViews(View rootView) {
        mainLayout = (LinearLayout) rootView.findViewById(R.id.mainLayout);
        progressView = rootView.findViewById(R.id.progress);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        rootView.findViewById(R.id.allCategories).setOnClickListener(new BannerOnCLickListener(true, null));
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        Call<List<HomeItem>> call = server.getHome();
        calls.add(call);
        call.enqueue(new HomeInformationCallBack(getContext()));
    }

    private void addItems(List<HomeItem> items) {
        for(int i=0; i<items.size(); i++){
            HomeItem item = items.get(i);
            if(item.getType() == HomeItem.TYPE_BANNER_CATEGORY){
                addBanner(item.getAspectRatio(), item.getDataUrl(), true, item.getClickUrl());
            }else if (item.getType() == HomeItem.TYPE_BANNER_URL){
                addBanner(item.getAspectRatio(), item.getDataUrl(), false, item.getClickUrl());
            }else if (item.getType() == HomeItem.TYPE_CATEGORY){
                addCategory(item.getTitle(), item.getDataUrl(), rowColors.get(i%rowColors.size()));
            }
        }
    }

    private void addCategory(String title, String dataUrl, int colorResource) {
        CategoryView row = new CategoryView((FragmentContainerActivity) getActivity());
        row.setCategory(title, dataUrl);
        row.setBackgroundResource(colorResource);
        addRow(row);
    }

    private void addBanner(double aspectRatio, String dataUrl, boolean category, String clickUrl) {
        FitToWidthWithAspectRatioImageView banner = new FitToWidthWithAspectRatioImageView(getContext());
        banner.setAspectRatio(aspectRatio);
        addRow(banner);
        Glide.with(this).load(dataUrl).placeholder(R.drawable.banner_place_holder).crossFade().into(banner);

        banner.setOnClickListener(new BannerOnCLickListener(category, clickUrl));
    }

    private void addRow(View view) {
        view.setLayoutParams(layoutParams);
        mainLayout.addView(view);
    }

    private void openCategoryPage(@Nullable String categoryUrl) {
        ((FragmentContainerActivity) getActivity()).changeFragment(CategoryFragment.newInstance(categoryUrl));
    }

    private void hideLoading() {
        progressView.setVisibility(View.GONE);
    }

    private class HomeInformationCallBack extends CallbackWithErrorDialog<List<HomeItem>> {
        public HomeInformationCallBack(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(List<HomeItem> data) {
            addItems(data);
        }

        @Override
        protected void postExecution() {
            super.postExecution();

            hideLoading();
        }
    }

    private class BannerOnCLickListener implements View.OnClickListener {
        private boolean category;
        private String clickUrl;

        public BannerOnCLickListener(boolean category, @Nullable String clickUrl) {
            this.category = category;
            this.clickUrl = clickUrl;

        }

        @Override
        public void onClick(View v) {
            if (category){
                openCategoryPage(clickUrl);
            }else{
                openUrl();
            }
        }

        private void openUrl() {
            clickUrl = PsychoUtils.fixUrl(this.clickUrl);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
            startActivity(browserIntent);
        }
    }
}
