package com.anashidgames.gerdoo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by vahid on 7/13/14.
 */
public class FitToWidthImageView extends ImageView {
    public FitToWidthImageView(Context context) {
        super(context);
    }

    public FitToWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitToWidthImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();

        if(drawable== null)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height;
            if(getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
                height = MeasureSpec.getSize(heightMeasureSpec);
                setScaleType(ScaleType.CENTER_CROP);
            }else
                height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
            setMeasuredDimension(width, height);
        }

    }

}
