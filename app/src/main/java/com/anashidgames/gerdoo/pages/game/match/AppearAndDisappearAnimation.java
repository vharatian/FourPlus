package com.anashidgames.gerdoo.pages.game.match;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by psycho on 5/20/16.
 */
public class AppearAndDisappearAnimation extends Animation {
    public static final int DEFAULT_DURATION = 2000;
    private View view;
    private boolean finished = false;
    private OnFinishedListener onFinishedListener;

    public AppearAndDisappearAnimation(View view) {
        this.view = view;
        setDuration(DEFAULT_DURATION);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (finished)
            return;

        if (interpolatedTime == 0){
            view.setVisibility(View.VISIBLE);
        }

        float alpha;
        if (interpolatedTime < 0.5){
            alpha = interpolatedTime;
        }else{
            alpha = 1 - interpolatedTime;
        }
        alpha = alpha * 2;

        view.setAlpha(alpha);


        if (interpolatedTime == 1){
            finished = true;
            view.setVisibility(View.GONE);

            if (onFinishedListener != null){
                onFinishedListener.onFinished(view);
            }
        }

        view.requestLayout();
    }

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }

    public interface OnFinishedListener {
        void onFinished(View view);
    }
}
