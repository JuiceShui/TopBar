package com.yeeyuntech.newheader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.yeeyuntech.newheader.R;

import io.reactivex.disposables.Disposable;

/**
 * Description: Jojo on 2018/4/2 ,Copyright YeeyunTech
 */
public class HeaderView extends ViewGroup implements FlingRecyclerView.onFlingListener, FlingScrollView.onScrollFlingListener, FlingRecyclerView.onLoadMoreListener {
    private View mTop, mTool, mAnotherTool, mNormal, mRefresh;
    private Context mContext;
    private int mTopId, mToolId, mAnotherToolId, mNormalId;
    private CircleProgress mProgress;
    private boolean mFirstLayout = true;
    private ViewDragHelper mHelper;
    private int mScrollRange = 600;
    private int mLoadRange = 150;
    private boolean mIsLoading = false;
    private int mLoadingTop;
    private onRefreshListener mRefreshListener;
    private OnToolsVisibilityChangeListener mToolVisibleChangeListener;
    private FlingRecyclerView mFlingRecycler;
    private FlingScrollView mFlingScroll;
    private boolean mIsRecyclerFling = false, mIsScrollFling = false;
    private boolean mHasSetFlingRecyclerListener = false, mHasSetFlingScrollListener = false;
    private Disposable disposable;
    private onLoadMoreListener mLoadMoreListener;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        mTopId = typedArray.getResourceId(R.styleable.HeaderView_topbar, 0);
        mToolId = typedArray.getResourceId(R.styleable.HeaderView_toolbar, 0);
        mAnotherToolId = typedArray.getResourceId(R.styleable.HeaderView_another_toolbar, 0);
        mNormalId = typedArray.getResourceId(R.styleable.HeaderView_normalbar, 0);
        mScrollRange = typedArray.getInteger(R.styleable.HeaderView_scroll_range, mScrollRange);
        mLoadRange = typedArray.getInteger(R.styleable.HeaderView_load_range, mLoadRange);
        mRefresh = LayoutInflater.from(context).inflate(R.layout.view_header_refresh, this, false);
        mProgress = mRefresh.findViewById(R.id.progress);
        addView(mRefresh, 0);
        typedArray.recycle();
        init();
    }

    private void init() {
        mHelper = ViewDragHelper.create(this, 1, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child == mNormal;
            }

            //当view释放时
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild == mNormal) {
                    if (mTool.getTop() < mTop.getMeasuredHeight() / 2) {//隐藏toolbar
                        int top = mTop.getMeasuredHeight();
                        mHelper.settleCapturedViewAt(0, top);
                        if (mAnotherTool != null && mHelper.getCapturedView() != null) {
                            mHelper.settleCapturedViewAt(0, top + mAnotherTool.getMeasuredHeight());
                        }
                        if (mAnotherTool != null) {
                            if (mAnotherTool.getTop() < mTop.getMeasuredHeight() / 2 && mHelper.getCapturedView() != null) {//隐藏topBar和toolbar
                                mHelper.settleCapturedViewAt(0, top);
                            }
                        }
                    } else {
                        int top;
                        if (!mIsLoading) {
                            if (mAnotherTool == null) {
                                top = mTop.getMeasuredHeight() + mTool.getMeasuredHeight();
                            } else {
                                top = mTop.getMeasuredHeight() + mTool.getMeasuredHeight() + mAnotherTool.getMeasuredHeight();
                            }
                        } else {
                            top = mLoadingTop;//执行loading
                            /**
                             * 模拟刷新完成
                             */
                            /*disposable = Observable.timer(3000, TimeUnit.MILLISECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long aLong) throws Exception {
                                            onRefreshFinish();
                                        }
                                    });*/
                            /*disposable = Observable
                                    .create(new ObservableOnSubscribe<String>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                            emitter.onNext("");
                                            emitter.onComplete();
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String s) throws Exception {
                                            if (mRefreshListener != null) {
                                                mRefreshListener.onRefresh();
                                            }
                                        }
                                    });*/
                            if (mRefreshListener != null) {
                                mRefreshListener.onRefresh();
                            }
                        }
                        mHelper.settleCapturedViewAt(0, top);
                    }
                    invalidate();
                }
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                int topHeight = 0;//normal上面除开顶部的总高
                if (changedView == mNormal) {
                    if (top < mScrollRange) {
                        if (mAnotherTool == null) {//位置改变时，设置toolbar，anothertoolbar的位置
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight());
                            topHeight = mTool.getMeasuredHeight() + mTop.getMeasuredHeight();
                        } else {
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight() - mAnotherTool.getMeasuredHeight());
                            mAnotherTool.setTop(mNormal.getTop() - mAnotherTool.getMeasuredHeight());
                            topHeight = mTool.getMeasuredHeight() + mAnotherTool.getMeasuredHeight() + mTop.getMeasuredHeight();
                        }
                        //位置改变时设置progress的样式 ，调用tools的样式
                        if (top <= topHeight + dp2px(20)) {
                            mProgress.stopRotate();////防止拉过快导致的缺帧数
                            mProgress.setCurrentProgress(0f);
                            //取消事件
                           /* if (disposable != null) {
                                disposable.dispose();
                                mRefreshListener.onCancelRefresh();
                            }*/
                            if (mRefreshListener != null) {
                                mRefreshListener.onCancelRefresh();
                            }
                            if (top < mTop.getMeasuredHeight() + mTool.getMeasuredHeight() / 2) {//当隐藏tool时
                                if (mToolVisibleChangeListener != null) {
                                    mToolVisibleChangeListener.onToolsInvisible();
                                }
                            } else {
                                if (mToolVisibleChangeListener != null) {
                                    mToolVisibleChangeListener.onToolsVisible();//显示Tools
                                }
                            }
                        } else {
                            if (top > topHeight + dp2px(20) && top < mLoadRange + topHeight + dp2px(20)) {//设置进度条的显示
                                mProgress.stopRotate();
                                mProgress.setCurrentProgress((top - topHeight - dp2px(20) + 0f) / (mLoadRange + 0f));
                                mIsLoading = false;
                                //取消事件
                                /*if (disposable != null) {
                                    disposable.dispose();
                                    mRefreshListener.onCancelRefresh();
                                }*/
                                if (mRefreshListener != null) {
                                    mRefreshListener.onCancelRefresh();
                                }
                            } else if (top >= mLoadRange + topHeight + dp2px(20)) {
                                mProgress.setCurrentProgress(1f);//防止拉过快导致的缺帧数
                                mProgress.rotate();
                                mIsLoading = true;//切换状态
                                mLoadingTop = mLoadRange + topHeight + dp2px(20);
                            }
                            if (mToolVisibleChangeListener != null) {//显示tools时
                                mToolVisibleChangeListener.onToolsVisible();
                            }
                        }
                    }
                    requestLayout();
                }
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (child == mNormal) {
                    if (0 < top && top < mScrollRange) {
                        return Math.max(top, mTop.getMeasuredHeight());
                    } else if (top == 0) {
                        //防止拖拽过快导致的  上下脱节
                        if (mAnotherTool == null) {
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight());
                        } else {
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight() - mAnotherTool.getMeasuredHeight());
                            mAnotherTool.setTop(mNormal.getTop() - mAnotherTool.getMeasuredHeight());
                        }
                        requestLayout();
                    } else if (top < 0) {//负值限制进入

                    } else {
                        //防止拖拽过快导致的  上下脱节
                        if (mAnotherTool == null) {
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight());
                        } else {
                            mTool.setTop(mNormal.getTop() - mTool.getMeasuredHeight() - mAnotherTool.getMeasuredHeight());
                            mAnotherTool.setTop(mNormal.getTop() - mAnotherTool.getMeasuredHeight());
                        }
                        requestLayout();
                        return mScrollRange;
                    }
                }
                return 0;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return child == mNormal ? mScrollRange : 0;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //判断是否有需要进行抛出的View事件传递
        if (mFlingRecycler != null && !mHasSetFlingRecyclerListener) {//not null&& has not set
            mFlingRecycler.setOnFlingListener(this);//设置fling监听
            mFlingRecycler.setOnLoadMoreListener(this);
            mHasSetFlingRecyclerListener = true;
        }
        if (mFlingScroll != null && !mHasSetFlingScrollListener) {//not null&& has not set
            mFlingScroll.setScrollFlingListener(this);
            mHasSetFlingScrollListener = true;
        }
        if (hasViewCanScrollUp(mNormal, event.getRawX(), event.getRawY())) {
            return false;
        } else {
            return mHelper.shouldInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mAnotherToolId != 0) {
            mAnotherTool = findViewById(mAnotherToolId);
        }
        if (mTopId != 0) {
            mTop = findViewById(mTopId);
        }
        if (mToolId != 0) {
            mTool = findViewById(mToolId);
        }
        if (mNormalId != 0) {
            mNormal = findViewById(mNormalId);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //TOP
        //MarginLayoutParams lp = (MarginLayoutParams) mTop.getLayoutParams();
        int topTop = mTop.getTop();
        mTop.layout(l, topTop,
                mTop.getMeasuredWidth(),
                topTop + mTop.getMeasuredHeight());
        //TOOL


        //REFRESH
        //lp = (MarginLayoutParams) mRefresh.getLayoutParams();
        int refreshTop = mTop.getMeasuredHeight();
        mRefresh.layout(l, refreshTop,
                mRefresh.getMeasuredWidth(),
                refreshTop + mRefresh.getMeasuredHeight());
        //TOOL
        int toolTop;
        // lp = (MarginLayoutParams) mTool.getLayoutParams();
        if (mFirstLayout) {
            toolTop = mTop.getMeasuredHeight();
        } else {
            toolTop = mTool.getTop();
        }
        mTool.layout(l, toolTop,
                mTool.getMeasuredWidth(),
                toolTop + mTool.getMeasuredHeight());

        //ANOTHER
        if (mAnotherTool != null) {
            int anotherToolTop;
            if (mFirstLayout) {
                anotherToolTop = mTop.getMeasuredHeight() + mTool.getMeasuredHeight();
            } else {
                anotherToolTop = mAnotherTool.getTop();
            }
            mAnotherTool.layout(l, anotherToolTop,
                    mAnotherTool.getMeasuredWidth(),
                    anotherToolTop + mAnotherTool.getMeasuredHeight());
        }

        //NORMAL
        // lp = (MarginLayoutParams) mNormal.getLayoutParams();
        int normalTop;
        if (mFirstLayout) {
            if (mAnotherTool != null) {
                normalTop = mTop.getMeasuredHeight() + mTool.getMeasuredHeight() + mAnotherTool.getMeasuredHeight();
            } else {
                normalTop = mTop.getMeasuredHeight() + mTool.getMeasuredHeight();
            }
        } else {
            normalTop = mNormal.getTop();
        }
        mNormal.layout(l, normalTop,
                mNormal.getMeasuredWidth(),
                normalTop + mNormal.getMeasuredHeight());

        mFirstLayout = false;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void smoothScrollTo() {
        if (mAnotherTool == null) {
            mHelper.smoothSlideViewTo(mTool, 0, mTop.getMeasuredHeight());
            mHelper.smoothSlideViewTo(mNormal, 0, mTop.getMeasuredHeight() + mTool.getMeasuredHeight());
        } else {
            mHelper.smoothSlideViewTo(mTool, 0, mTop.getMeasuredHeight());
            mHelper.smoothSlideViewTo(mAnotherTool, 0, mTop.getMeasuredHeight() + mTool.getMeasuredHeight());
            mHelper.smoothSlideViewTo(mNormal, 0, mTop.getMeasuredHeight() + mTool.getMeasuredHeight() + mAnotherTool.getMeasuredHeight());
        }
        ViewCompat.postInvalidateOnAnimation(this);
        if (mIsRecyclerFling) {
            mIsRecyclerFling = false;
        }
        if (mIsScrollFling) {
            mIsScrollFling = false;
        }
    }

    /**
     * 判断指定点所在的view是否未到顶可以继续上划，如果view为ViewGroup的话还得递归往子view判断进去
     */
    private boolean hasViewCanScrollUp(View view, float x, float y) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (hasViewCanScrollUp(child, x, y)) {
                    return true;
                }
            }
            return isInViewArea(view, x, y) && view.canScrollVertically(-1);
        } else {
            return isInViewArea(view, x, y) && view.canScrollVertically(-1);
        }
    }

    //FlingRecycler
    @Override
    public void onFling() {
        if (!mIsRecyclerFling) {
            mIsRecyclerFling = true;
            smoothScrollTo();
        }
    }

    //FlingScroll
    @Override
    public void onScrollFling() {
        if (!mIsScrollFling) {
            mIsScrollFling = true;
            smoothScrollTo();
        }
    }

    //load more
    @Override
    public void loadMore() {
        if (mLoadMoreListener != null) {
            mLoadMoreListener.loadMore();
        }
    }

    /**
     * 判断坐标是否在view区域内
     */
    private boolean isInViewArea(View view, float x, float y) {
        //判断是否有可滑动的view的类型
        if (view instanceof FlingRecyclerView && mFlingRecycler == null) {
            mFlingRecycler = (FlingRecyclerView) view;
        } else if (view instanceof FlingScrollView && mFlingScroll == null) {
            mFlingScroll = (FlingScrollView) view;
        }
        int[] local = new int[2];
        view.getLocationOnScreen(local);
        return x > local[0] && x < local[0] + view.getMeasuredWidth() && y > local[1] && y < local[1] + view.getMeasuredHeight();
    }

    //-----------------提供给外部调用 Start--------------------//
    public void setScrollRange(int scrollRange) {
        this.mScrollRange = mScrollRange;
    }

    public interface onRefreshListener {
        void onRefresh();

        void onCancelRefresh();
    }

    public void setOnRefreshListener(onRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    public void onRefreshFinish() {
        mIsLoading = false;
        smoothScrollTo();
    }

    public interface OnToolsVisibilityChangeListener {
        void onToolsInvisible();

        void onToolsVisible();

    }

    public void setOnToolsVisibleChangeListener(OnToolsVisibilityChangeListener listener) {
        this.mToolVisibleChangeListener = listener;
    }

    public interface onLoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(onLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    //------------------提供给外部调用  End--------------------//
}
