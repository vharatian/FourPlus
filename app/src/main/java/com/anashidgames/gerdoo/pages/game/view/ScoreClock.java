package com.anashidgames.gerdoo.pages.game.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 5/23/16.
 */
public class ScoreClock extends ImageView {

    private Bitmap clockHand;
    private int handHeight = -1;
    private Paint paint;
    private int angle = 0;
    private AngleAnimation animation;

    public ScoreClock(Context context) {
        super(context);
        init();
    }

    public ScoreClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoreClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        clockHand = BitmapFactory.decodeResource(getResources(), R.drawable.clock_hand);
    }

    public void setAngle(int angle) {
        if (angle > 90 || angle < -90) {
            return;
        }

        if (angle > 60) {
            angle = 60;
        }

        if (angle < -60) {
            angle = -60;
        }


        if (animation != null && !animation.isFinished()) {
            animation.cancel();
        }
        animation = new AngleAnimation(this.angle, angle);
        startAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedHand = Bitmap.createBitmap(clockHand, 0, 0, clockHand.getWidth(), clockHand.getHeight(), matrix, true);

        float handTop = (float) (getMeasuredHeight() - handHeight * Math.cos(Math.toRadians(angle)));

        int handPinRadius = clockHand.getWidth() / 2;
        canvas.drawBitmap(rotatedHand, (getMeasuredWidth()/2) - handPinRadius, handTop + handPinRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (handHeight < 0) {
            handHeight = (int) (getMeasuredHeight() * 0.8);

            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, clockHand.getWidth(), clockHand.getHeight()),
                    new RectF(0, 0, clockHand.getWidth()*(handHeight * clockHand.getHeight()), handHeight), Matrix.ScaleToFit.CENTER);
            clockHand = clockHand.createBitmap(clockHand, 0, 0, clockHand.getWidth(), clockHand.getHeight(), m, true);
        }
    }

    private class AngleAnimation extends Animation{
        private final int startAngle;
        private final int endAngle;
        private boolean finished;

        public AngleAnimation(int startAngle, int endAngle) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
            int duration = Math.abs((endAngle - startAngle) * 10) ;
            setDuration(duration);
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

            if (finished){
                return;
            }

            int angle = (int) (startAngle + ((endAngle - startAngle) * interpolatedTime));
            ScoreClock.this.angle = angle;
            requestLayout();

            if (interpolatedTime == 1){
                finished = true;
            }
        }

        public boolean isFinished() {
            return finished;
        }
    }
}
