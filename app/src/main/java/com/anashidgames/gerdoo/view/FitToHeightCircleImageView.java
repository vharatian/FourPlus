package com.anashidgames.gerdoo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vahid on 7/13/14.
 */
public class FitToHeightCircleImageView extends CircleImageView {
    public FitToHeightCircleImageView(Context context) {
        super(context);
    }

    public FitToHeightCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitToHeightCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(size, size);
    }

}
