package com.anashidgames.gerdoo.pages.game;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by psycho on 5/20/16.
 */
public class FlashingAnimation extends Animation{
    private final ImageView imageView;
    private boolean finished;
    private int color;

    public FlashingAnimation(ImageView imageView, int color) {
        this.imageView = imageView;
        this.color = color;
        setDuration(350);
        setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public void cancel() {
        super.cancel();
        finished = true;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (finished)
            return;

        float alpha = (float) (interpolatedTime * 1.5);
        if (alpha > 1){
            alpha = 2f - alpha;
        }

        if (alpha == 0){
            alpha = 0.05f;
        }

        int color = (((int)(0xff * alpha)) * 0x1000000 + this.color % 0x1000000);

        imageView.setColorFilter(color);

        if (interpolatedTime == 1){
            finished = true;
        }

    }
}
