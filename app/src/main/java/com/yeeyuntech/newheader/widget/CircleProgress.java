package com.yeeyuntech.newheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.yeeyuntech.newheader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Jojo on 2018/4/2 ,Copyright YeeyunTech
 */
public class CircleProgress extends View {
    private int mInnerRadius = 5;//内圈的半径
    private int mOutterRadius = 20;//外圈的半径
    private int mCount = 10;//线条数
    private int mColor;//画笔的颜色
    private int mBackgroundColor;//背景色
    private Paint mPaint;
    private Context mContext;
    private List<PointF> mInnerPoints = new ArrayList<>();
    private List<PointF> mOutterPoints = new ArrayList<>();
    Animation mRotate;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
        mInnerRadius = typedArray.getInteger(R.styleable.CircleProgress_inner_radius, 5);
        mInnerRadius = dp2px(mInnerRadius);
        mOutterRadius = typedArray.getInteger(R.styleable.CircleProgress_outter_radius, 20);
        mOutterRadius = dp2px(mOutterRadius);
        mCount = typedArray.getInteger(R.styleable.CircleProgress_count, 10);
        if (mCount > 15) {
            mCount = 15;
        }
        mColor = typedArray.getResourceId(R.styleable.CircleProgress_line_color, R.color.white);
        mBackgroundColor = typedArray.getResourceId(R.styleable.CircleProgress_bg_color, R.color.bg_color);
        setBackgroundColor(mContext.getResources().getColor(mBackgroundColor));
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setStyle(Paint.Style.FILL);
        mRotate = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        mRotate.setInterpolator(lin);

    }

    private void calculatePoints(int current) {
        // 圆心的坐标
        float xRadious = getWidth() / 2;
        float yRadious = getHeight() / 2;
        mInnerPoints.clear();
        mOutterPoints.clear();
        for (int i = 0; i < current; i++) {
            // 计算相应的 sin cos 值
            float cos = (float) Math.cos(2 * Math.PI / 360 * (360 / mCount) * i);
            float sin = (float) Math.sin(2 * Math.PI / 360 * (360 / mCount) * i);
            PointF innerPoint = new PointF(xRadious + sin * mInnerRadius, yRadious - cos * mInnerRadius);
            PointF outterPoint = new PointF(xRadious + sin * mOutterRadius, yRadious - cos * mOutterRadius);
            mInnerPoints.add(innerPoint);
            mOutterPoints.add(outterPoint);
        }
        postInvalidate();
    }

    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setCurrentProgress(float progress) {
        int currentIndex = (int) (progress * mCount);
        calculatePoints(currentIndex);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mColor != -1) {
            mPaint.setColor(mContext.getResources().getColor(mColor));
        }
        for (int i = 0; i < mInnerPoints.size(); i++) {
            PointF startEntity = mInnerPoints.get(i);
            PointF endEntity = mOutterPoints.get(i);
            canvas.drawLine(startEntity.x, startEntity.y, endEntity.x, endEntity.y, mPaint);
        }
    }

    public void rotate() {
        if (this.getAnimation() == null) {
            this.setAnimation(mRotate);
            this.startAnimation(mRotate);
        }
    }

    public void stopRotate() {
        if (this.getAnimation() != null) {
            this.clearAnimation();
        }
    }
}
