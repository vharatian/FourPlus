package com.anashidgames.gerdoo.view.chart.pie;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.utils.PsychoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by psycho on 4/30/16.
 */
public class PieChart extends View {

    public static final double INNER_RADIUS = 0.7;
    public static final double SHADOW_RADIUS = 0.8;
    private static final double TEXT_RADIUS = 1.1;
    public static final float SEGMENT_DISTANCE = 2.5f;
    public static final float BOLD_TEXT_MAX_WIDTH = 1.5f;
    public static final float SMALL_TEXT_MAX_WIDTH = 1f;
    public static final int MAX_SMALL_CENTER_TEXT = 12;
    private static final float MAX_BOLD_CENTER_TEXT = 30;
    public static final int START_ANGLE = -60;
    private List<PieChartItem> items;
    private int totalValues;

    private float outerRadius;
    private float innerRadius;
    private float textRadius;
    private float shadowRadius;
    private float centerX;
    private float centerY;
    private float width;

    private Paint paint;

    private float maxTextSize;
    private int centerColor;

    private String boldCenterText;
    private String smallCenterText;

    private float maxBoldCenterTextSize;
    private float maxSmallCenterText;



    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/IRANSansMobile_Light.ttf");
        paint.setTypeface(tf);

        maxTextSize = getResources().getDimension(R.dimen.pieChartDescriptionTextSize);
        maxBoldCenterTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_BOLD_CENTER_TEXT, getResources().getDisplayMetrics());;
        maxSmallCenterText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_SMALL_CENTER_TEXT, getResources().getDisplayMetrics());
        setCenterColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
    }

    public void setBoldCenterText(String boldCenterText) {
        this.boldCenterText = PsychoUtils.toPersianNumber(boldCenterText);
    }

    public void setSmallCenterText(String smallCenterText) {
        this.smallCenterText = PsychoUtils.toPersianNumber(smallCenterText);
    }

    public void addData(PieChartItem item){
        if (item == null)
            return;

        if (items == null) {
            items = new ArrayList<>();
            totalValues = 0;
        }

        items.add(item);
        totalValues += item.getValue();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        initCoordination();
    }

    private void initCoordination() {
        int openSpace = (int) getResources().getDimension(R.dimen.longBreak);
        int height = getMeasuredHeight();
        width = getMeasuredWidth();
        outerRadius = (height - openSpace) / 2;
        innerRadius = (float) (outerRadius * INNER_RADIUS);
        textRadius = (float) (outerRadius * TEXT_RADIUS);
        shadowRadius = (float) (outerRadius * SHADOW_RADIUS);
        centerX = width/2;
        centerY = height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startingAngle = START_ANGLE;
        for(PieChartItem item : items){
            startingAngle = drawItem(canvas, startingAngle, item);
        }

        drawShadow(canvas);
        drawCenterText(canvas);
    }

    private void drawCenterText(Canvas canvas) {
        boolean drawBoldText = boldCenterText != null && !boldCenterText.isEmpty();
        boolean drawSmallText = smallCenterText != null && !smallCenterText.isEmpty();

        if (drawBoldText){
            float desiredWidth = BOLD_TEXT_MAX_WIDTH * innerRadius;
            float textCenterY = centerY;
            if (drawSmallText)
                textCenterY *= 1.1;

            drawCenterText(canvas, boldCenterText, desiredWidth, maxBoldCenterTextSize, centerX,  textCenterY);
        }

        if (drawSmallText){
            float desiredWidth = SMALL_TEXT_MAX_WIDTH * innerRadius;
            float textCenterY = this.centerY - innerRadius *  0.45f;
            drawCenterText(canvas, smallCenterText, desiredWidth, maxSmallCenterText, centerX, textCenterY);
        }
    }

    private void drawCenterText(Canvas canvas, String text, float desiredWidth, float maxTextSize, float centerX, float centerY){
        float textSize = calculateTextSize(desiredWidth, maxTextSize, text);
        paint.setTextSize(textSize);
        paint.setColor(getResources().getColor(R.color.white));

        Rect textBounds = calculateTextBounds(text, textSize);
        float startX = centerX  - textBounds.width()/2;
        float startY = centerY  + textBounds.height()/2;

        canvas.drawText(text, startX, startY, paint);
    }

    private void drawShadow(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(0x70);
        canvas.drawCircle(centerX, centerY, shadowRadius, paint);

        paint.setAlpha(0xff);
        paint.setColor(centerColor);
        canvas.drawCircle(centerX, centerY, innerRadius, paint);
    }

    private float drawItem(Canvas canvas, float startAngle, PieChartItem item) {
        float angle = (float) ((item.getValue()/totalValues) * PsychoUtils.COMPLETE_CIRCLE);
        paint.setColor(item.getColor());
        drawSegment(canvas, startAngle + SEGMENT_DISTANCE,  angle - SEGMENT_DISTANCE, paint);

        float textAngle = startAngle + angle / 2;
        drawDescription(canvas, textAngle, getDescriptionText(item));

        return startAngle + angle;
    }

    private void drawDescription(Canvas canvas, float textAngle, String description) {
        Rect bounds = new Rect();
        float textSize = calculateDescriptionSize(textAngle, description);
        paint.setTextSize(textSize);
        paint.getTextBounds(description, 0, description.length(), bounds);

        float startX = getX(textRadius, textAngle);
        float startY = getY(textRadius, textAngle);

        if (PsychoUtils.isOnLeft(textAngle)) {
            startX -= bounds.width();
        }

        if (PsychoUtils.isOnBottom(textAngle)){
            startY += textSize;
        }


        canvas.drawText(description, startX, startY, paint);
    }

    private float calculateDescriptionSize(float angle, String descriptionText) {
        float desiredWidth;

        if (PsychoUtils.isOnLeft(angle)) {
            desiredWidth = getX(textRadius, angle);
        }else {
            desiredWidth = this.width - getX(textRadius, angle);
        }

        return calculateTextSize(desiredWidth, maxTextSize, descriptionText);
    }

    private float calculateTextSize(float desiredWidth, float maxTextSize, String text){
        if (maxTextSize == 0)
            maxTextSize = 10000000;

        float textSize;
        float distance = 1;

        for (textSize = 0; distance > 0 && textSize < maxTextSize; textSize += 2){
            float textWidth = calculateTextBounds(text, textSize).width();
            distance = desiredWidth - textWidth;
        }

        return textSize - 2;
    }

    private Rect calculateTextBounds(String text, float textSize) {
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        return bounds;
    }

    private void drawSegment(Canvas canvas, float startAngle, float angle, Paint paint) {
        if (angle < 0)
            return;

        Path path = new Path();

        path.arcTo(getOval(innerRadius), startAngle, angle, false);
        path.arcTo(getOval(outerRadius), startAngle + angle, -angle, false);
        path.close();

        canvas.drawPath(path, paint);
    }

    private String getDescriptionText(PieChartItem item) {
        return PsychoUtils.toPersianNumber(item.getTitle() + " %" + ((int) item.getValue()));
    }

    private RectF getOval(float radius) {
        return new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    private float getY(float radius, float angle) {
        return (float) (centerY + radius * Math.sin(Math.toRadians(angle)));
    }

    private float getX(float radius, float angle) {
        return (float) (centerX + radius * Math.cos(Math.toRadians(angle)));
    }
}
