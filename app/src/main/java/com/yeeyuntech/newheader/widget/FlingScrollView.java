package com.yeeyuntech.newheader.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Description: Jojo on 2018/4/9 ,Copyright YeeyunTech
 */
public class FlingScrollView extends ScrollView {
    private onScrollFlingListener mListener;

    public FlingScrollView(Context context) {
        this(context, null);
    }

    public FlingScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlingScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

   /* @Override
    protected void onScrollChanged(int l, int top, int oldl, int oldt) {
        super.onScrollChanged(l, top, oldl, oldt);
        //Log.e("Scrool:", "top:" + top + "  oldTop:" + oldt);
        if (top < 100 && oldt > top) {

        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        // Log.e("Scrool:", "scrollY:" + scrollY + "  clampedY:" + clampedY);
    }*/

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //Log.e("Scrool:", "deltaY:" + deltaY + "  scrollY:" + scrollY + "    scrollRangeY:" + scrollRangeY + "   maxOverScrollY:" + maxOverScrollY + "   isTouchEvent" + isTouchEvent);
        if (scrollY < 100 && deltaY < -50) {
            if (mListener != null) {
                mListener.onScrollFling();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

    }

    public interface onScrollFlingListener {
        void onScrollFling();
    }

    public void setScrollFlingListener(onScrollFlingListener listener) {
        this.mListener = listener;
    }
}
