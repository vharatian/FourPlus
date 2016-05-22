package com.anashidgames.gerdoo.pages.game;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.MatchHistoryItem;
import com.anashidgames.gerdoo.core.service.model.Question;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.core.service.realTime.MatchMakingManager;
import com.anashidgames.gerdoo.pages.game.view.ScoreView;
import com.anashidgames.gerdoo.utils.PsychoUtils;
import com.anashidgames.gerdoo.view.FitToWidthWithAspectRatioImageView;
import com.anashidgames.gerdoo.view.chart.pie.PieChart;
import com.anashidgames.gerdoo.view.chart.pie.PieChartItem;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 4/30/16.
 */
public class ScoresFragment extends Fragment {

    public static final int FINISHING_SCORE = 20;

    public static Fragment newInstance(){
        return new ScoresFragment();
    }

    private ViewPager gameHistoryPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scores, container, false);

        GameActivity activity = (GameActivity) getActivity();
        GameManager gameManager = activity.getGameManager();
        MatchMakingManager matchMakingManager = activity.getMatchMakingManager();

        ScorePageInfo.Scores scores = new ScorePageInfo.Scores(
                gameManager.getMyScore() - FINISHING_SCORE,
                1,
                FINISHING_SCORE,
                gameManager.getMyScore());
        initScores(rootView, scores);

        int score = matchMakingManager.getScore() + gameManager.getMyScore();
        initLevelChart(rootView, new ScorePageInfo.LevelChartInfo(score));

//        initAdBanner(rootView, info.getAdInfo());

        initMatchHistory(rootView, gameManager.getQuestions(), gameManager.getCorrectAnswers(), gameManager.getMyAnswers());

        rootView.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        rootView.findViewById(R.id.reportQuestionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReportMail();
            }
        });

        return rootView;
    }

    private void sendReportMail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"feedback@fourapp.ir"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Report");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Reporting question");

        getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void initMatchHistory(View rootView, List<Question> questions, List<String> correctAnswers, List<String> myAnswers) {
        if (questions == null)
            return;

        List<MatchHistoryItem> history = new ArrayList<>();
        int size = Math.min(questions.size(), Math.min(correctAnswers.size(), myAnswers.size()));
        for (int i=0; i<size; i++){
            history.add(new MatchHistoryItem(questions.get(i), correctAnswers.get(i), myAnswers.get(i)));
        }

        HistoryAdapter adapter = new HistoryAdapter(getFragmentManager(), history);
        gameHistoryPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        gameHistoryPager.setAdapter(adapter);

        CirclePageIndicator indicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        indicator.setViewPager(gameHistoryPager);
    }

    private void initAdBanner(View rootView, ScorePageInfo.AdInfo adInfo) {
        if (adInfo == null)
            return;

        FitToWidthWithAspectRatioImageView adBanner = (FitToWidthWithAspectRatioImageView) rootView.findViewById(R.id.adBanner);
        adBanner.setAspectRatio(adInfo.getAspectRatio());
        PsychoImageLoader.loadImage(getActivity(), adInfo.getBannerUrl(), R.drawable.banner_place_holder, adBanner);
        adBanner.setOnClickListener(new UrlOpener(adInfo.getClickUrl()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = (Display) getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gameHistoryPager.getLayoutParams().height = (int) (size.y * 0.7);
    }

    private void initLevelChart(View rootView, ScorePageInfo.LevelChartInfo levelChartInfo) {
        if (levelChartInfo == null)
            return;

        PieChart levelChart = (PieChart) rootView.findViewById(R.id.levelChart);
        levelChart.setBoldCenterText(levelChartInfo.getLevel() + "");
        levelChart.setSmallCenterText(getString(R.string.level));

        levelChart.addData(new PieChartItem("امتیاز موجود", levelChartInfo.getCurrentScore(), getResources().getColor(R.color.red)));
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

    private class HistoryAdapter extends FragmentPagerAdapter {

        private List<MatchHistoryItem> history;

        public HistoryAdapter(FragmentManager fm, List<MatchHistoryItem> history) {
            super(fm);
            this.history = history;
        }

        @Override
        public Fragment getItem(int position) {
            MatchHistoryItem history = this.history.get(position);
            return GameHistoryFragment.getInstance(history);
        }

        @Override
        public int getCount() {
            return history.size();
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
