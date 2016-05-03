package com.anashidgames.gerdoo.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.view.basic.TextView;

/**
 * Created by psycho on 4/30/16.
 */
public class FitToWidthTextView extends TextView {
    public FitToWidthTextView(Context context) {
        super(context);
    }

    public FitToWidthTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitToWidthTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FitToWidthTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT ||
                getText() == null || getText().toString().isEmpty()) {
            return;
        }

        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        correctTextSize(width);
    }

    private void correctTextSize(int desiredWidth)
    {
        Paint paint = new Paint();
        Rect bounds = new Rect();


        paint.setTypeface(getTypeface());
        float textSize = getTextSize();
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        setTextSize(textSize);
    }
}
