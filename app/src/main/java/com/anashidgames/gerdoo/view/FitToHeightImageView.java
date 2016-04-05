package com.anashidgames.gerdoo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by vahid on 7/13/14.
 */
public class FitToHeightImageView extends ImageView {
    public FitToHeightImageView(Context context) {
        super(context);
    }

    public FitToHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitToHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();

        if(drawable== null)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else {
            int width;
            int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
            if(getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
                width = MeasureSpec.getSize(widthMeasureSpec);
                setScaleType(ScaleType.CENTER_CROP);
            }else
                width = (int) ( height * (1.0 *drawable.getIntrinsicWidth()) / drawable.getIntrinsicHeight());
            setMeasuredDimension(width, height);
        }
    }

}
