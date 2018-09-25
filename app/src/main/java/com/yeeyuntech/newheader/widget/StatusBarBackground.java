/*
 * Created by adu on 2017/12/2.
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.yeeyuntech.newheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yeeyuntech.newheader.R;


public class StatusBarBackground extends View {

    private int mHeight;

    public StatusBarBackground(Context context) {
        super(context);
    }

    public StatusBarBackground(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StatusBarBackground, defStyleAttr, 0);
            int background = a.getColor(R.styleable.StatusBarBackground_android_background, 0);
            int foreground = a.getColor(R.styleable.StatusBarBackground_android_foreground, 0);
            a.recycle();
            if (foreground != 0 && background != 0) {
                int color = ColorUtils.compositeColors(foreground, background);
                setBackgroundColor(color);
            }
        }
        init();
    }

    private void init() {
        mHeight = getStatusBarHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), mHeight);
    }

    private int getStatusBarHeight() {
        int height = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
