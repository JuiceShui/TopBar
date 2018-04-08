package com.yeeyuntech.newheader.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Description: Jojo on 2018/4/4 ,Copyright YeeyunTech
 */
public class FlingRecyclerView extends RecyclerView {
    private int mVelocityY;
    private int lastPosition = Integer.MAX_VALUE, currentPosition = Integer.MAX_VALUE;
    private long lastTime = 0, currentTime = 0;
    private boolean isShouldFling = false;
    private onFlingListener mListener;

    public FlingRecyclerView(Context context) {
        this(context, null);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = FlingRecyclerView.this.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    currentPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (currentPosition == 0 && dy < -50) {
                        isShouldFling = true;
                        if (mListener != null) {
                            mListener.onFling();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        this.mVelocityY = velocityY;
        return super.fling(velocityX, velocityY);
    }

    public int getVelocityY() {
        return mVelocityY;
    }

    public boolean isShouldFling() {
        return isShouldFling;
    }

    public interface onFlingListener {
        void onFling();
    }

    public void setOnFlingListener(onFlingListener listener) {
        mListener = listener;
    }
}
