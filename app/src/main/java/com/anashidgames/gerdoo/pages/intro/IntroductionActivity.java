package com.anashidgames.gerdoo.pages.intro;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.auth.AuthenticationActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Arrays;
import java.util.List;

public class IntroductionActivity extends GerdooActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, IntroductionActivity.class);
    }

    private InnerAdapter adapter;
    private ViewPager pager;
    private View doneButton;
    private View mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        initViewPager();
        initIndicator(pager);
        initDoneButton();

        mainLayout = findViewById(R.id.mainLayout);
    }

    private void initDoneButton() {
        doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(AuthenticationActivity.newIntent(IntroductionActivity.this));
            }
        });
    }

    private void initIndicator(ViewPager pager) {
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    @NonNull
    private ViewPager initViewPager() {
        List<Fragment> fragments = Arrays.asList(
                (Fragment) IntroductionFragment.newInstance(R.drawable.intro1, R.string.introTitle, R.string.introDescription1),
                IntroductionFragment.newInstance(R.drawable.intro2, R.string.introTitle, R.string.introDescription1),
                IntroductionFragment.newInstance(R.drawable.intro3, R.string.introTitle, R.string.introDescription1)
        );

        adapter = new InnerAdapter(getSupportFragmentManager(), fragments);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new InnerPageChangeListener());
        return pager;
    }

    @Override
    public void onBackPressed() {
        int currentItem = pager.getCurrentItem();
        if(currentItem != 0){
            pager.setCurrentItem(currentItem - 1);
        }else {
            super.onBackPressed();
        }
    }

    private class InnerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public InnerAdapter(FragmentManager fm, List<Fragment> fragments) {
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

    private class InnerPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(final int position) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((IntroductionFragment) adapter.getItem(position)).resetAnimation();

                    if (position == 0){
                        mainLayout.setBackgroundResource(R.color.colorPrimary);
                    }else{
                        mainLayout.setBackgroundResource(R.color.introPageBackgroundSecond);
                    }
                }
            });

            if(position == adapter.getCount() - 1){
                doneButton.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(IntroductionActivity.this, R.anim.right_to_left_slide);
                doneButton.startAnimation(animation);
            }else {
                doneButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}


    }
}
