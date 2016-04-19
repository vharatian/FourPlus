package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by psycho on 3/30/16.
 */
public class ExpandableRelativeLayout extends RelativeLayout {

    private boolean expanded = false;
    private Animation animation;
    private OnExpansionListener expansionListener;
    private Integer expandHeight = null;

    public ExpandableRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public ExpandableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

    }

    private boolean isAnimating() {
        return animation != null;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        if(isAnimating())
            return;

        this.expanded = expanded;

        checkState();
    }

    public void checkState() {
        if(isExpanded()){
            getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }else{
            getLayoutParams().height = 0;
        }

        requestLayout();
    }

    public void expand() {
        if (isExpanded() || isAnimating())
            return;

        if (expansionListener != null)
            expansionListener.onExpansion(this);

        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int measuredHeight = getMeasuredHeight();
        if (expandHeight != null)
            measuredHeight = expandHeight;

        final int targetHeight = measuredHeight + getPaddingBottom() + getPaddingTop();

        getLayoutParams().height = 0;
        setVisibility(View.VISIBLE);
        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                getLayoutParams().height = (int)(targetHeight * interpolatedTime);
                requestLayout();

                if (interpolatedTime == 1){
                    animation = null;

                    if (expansionListener != null)
                        expansionListener.expanded(ExpandableRelativeLayout.this);
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        animation.setDuration(getDurationMillis(targetHeight));
        startAnimation(animation);
        expanded = true;
    }

    public void collapse() {
        if (!isExpanded() || isAnimating())
            return;

        final int initialHeight = getMeasuredHeight();

        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    setVisibility(View.GONE);
                    animation = null;
                }else{
                    getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        animation.setDuration(getDurationMillis(initialHeight));
        startAnimation(animation);
        expanded = false;
    }

    private int getDurationMillis(int height) {
       return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics())/5;
    }

    public void toggle() {
        if (isAnimating())
            return;

        if(expanded)
            collapse();
        else
            expand();
    }


    public void setExpansionListener(OnExpansionListener expansionListener) {
        this.expansionListener = expansionListener;
    }

    public interface OnExpansionListener{
        void expanded(View view);
        void onExpansion(View view);
    }
}
