package com.anashidgames.gerdoo.pages.game;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.game.view.ScoreView;
import com.anashidgames.gerdoo.utils.PsychoUtils;
import com.anashidgames.gerdoo.view.FitToWidthWithAspectRatioImageView;
import com.anashidgames.gerdoo.view.chart.pie.PieChart;
import com.anashidgames.gerdoo.view.chart.pie.PieChartItem;
import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by psycho on 4/30/16.
 */
public class ScoresFragment extends Fragment {

    public static final String INFO = "info";

    public static Fragment newInstance(){
        ScorePageInfo.Scores scores = new ScorePageInfo.Scores(113, 1, 20, 133);
        ScorePageInfo.LevelChartInfo levelChartInfo = new ScorePageInfo.LevelChartInfo(23, 8, 69);
        ScorePageInfo.AdInfo adInfo = new ScorePageInfo.AdInfo("https://i.imgsafe.org/5b75feb.jpg", 8.372, "www.google.com");
        List<String> images = Arrays.asList("/sdcard/Pictures/1.jpeg", "/sdcard/Pictures/1.jpeg", "/sdcard/Pictures/1.jpeg", "/sdcard/Pictures/1.jpeg", "/sdcard/Pictures/1.jpeg", "/sdcard/Pictures/1.jpeg");
        ScorePageInfo info = new ScorePageInfo(scores, levelChartInfo, adInfo, images);

        Bundle bundle = new Bundle();
        bundle.putSerializable(INFO, info);
        Fragment fragment = new ScoresFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private ViewPager questionsImagePager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scores, container, false);

        Bundle bundle = getArguments();
        ScorePageInfo info = (ScorePageInfo) bundle.getSerializable(INFO);

        initScores(rootView, info.getScores());
        initLevelChart(rootView, info.getLevelChartInfo());
        initAdBanner(rootView, info.getAdInfo());
        initQuestionsImagePager(rootView, info.getQuestionsImage());

        rootView.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    private void initAdBanner(View rootView, ScorePageInfo.AdInfo adInfo) {
        if (adInfo == null)
            return;

        FitToWidthWithAspectRatioImageView adBanner = (FitToWidthWithAspectRatioImageView) rootView.findViewById(R.id.adBanner);
        adBanner.setAspectRatio(adInfo.getAspectRatio());
        Glide.with(getActivity()).load(adInfo.getBannerUrl()).placeholder(R.drawable.banner_place_holder).crossFade().into(adBanner);
        adBanner.setOnClickListener(new UrlOpener(adInfo.getClickUrl()));
    }

    @NonNull
    private void initQuestionsImagePager(View rootView, List<String> questionsImage) {
        if (questionsImage == null)
            return;

        List<Fragment> fragments = new ArrayList<>();
        for (String path: questionsImage)
            fragments.add(ImageFragment.newInstance(path));

        QuestionsImageAdapter adapter = new QuestionsImageAdapter(getFragmentManager(), fragments);
        questionsImagePager = (ViewPager) rootView.findViewById(R.id.viewPager);
        questionsImagePager.setAdapter(adapter);

        CirclePageIndicator indicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        indicator.setViewPager(questionsImagePager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = (Display) getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        questionsImagePager.getLayoutParams().height = (int) (size.y * 0.7);
    }

    private void initLevelChart(View rootView, ScorePageInfo.LevelChartInfo levelChartInfo) {
        if (levelChartInfo == null)
            return;

        PieChart levelChart = (PieChart) rootView.findViewById(R.id.levelChart);
        levelChart.setBoldCenterText(14 + "");
        levelChart.setSmallCenterText(getString(R.string.level));

        levelChart.addData(new PieChartItem("امتیاز موجود", levelChartInfo.getExistingScore(), getResources().getColor(R.color.red)));
        levelChart.addData(new PieChartItem("امتیاز این دست", levelChartInfo.getThisRoundScore(), getResources().getColor(R.color.colorAccent)));
        levelChart.addData(new PieChartItem("امتیاز مانده تا سطح بعد", levelChartInfo.getRemainingScoresToNextLevel(), getResources().getColor(R.color.white)));
    }

    private void initScores(View rootView, ScorePageInfo.Scores scores) {
        if (scores == null)
            return;

        ScoreView thisRoundScore = (ScoreView) rootView.findViewById(R.id.thisRoundScore);
        ScoreView boostFactor = (ScoreView) rootView.findViewById(R.id.boostFactor);
        ScoreView finishing = (ScoreView) rootView.findViewById(R.id.finishing);
        ScoreView totalScore = (ScoreView) rootView.findViewById(R.id.totalScore);

        thisRoundScore.init(R.string.thisRound, R.color.colorAccent);
        boostFactor.init(R.string.boostFactor, R.color.green);
        finishing.init(R.string.finishing, R.color.yellow);
        totalScore.init(R.string.total, R.color.red);

        thisRoundScore.setScore("" + scores.getThisRoundScore());
        boostFactor.setScore("x" + scores.getBoostFactor());
        finishing.setScore("" + scores.getFinishing());
        totalScore.setScore("" + scores.getTotalScore());
    }

    private class QuestionsImageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public QuestionsImageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private class UrlOpener implements View.OnClickListener {
        private String clickUrl;

        public UrlOpener(String clickUrl) {
            this.clickUrl = clickUrl;
        }

        @Override
        public void onClick(View v) {
            clickUrl = PsychoUtils.fixUrl(this.clickUrl);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
            startActivity(browserIntent);
        }
    }
}
