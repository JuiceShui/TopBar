package com.yeeyuntech.newheader.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wkp.softlinearlayout.view.SoftLinearLayout;
import com.yeeyuntech.newheader.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Description: Jojo on 2018/4/10 ,Copyright YeeyunTech
 */
public class TestActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private SimpleAdapter mAdapter;
    private HeaderView mHeader;
    private char A = 'a';
    private int count = 40;
    private int index = 1;
    private List<String> mData = new ArrayList<>();
    private Disposable disposable;
    private TextView mTop, mTool, mATool;
    private FrameLayout normal;
    private FlingScrollView scrollView;
    private EditText editText1, editText2, editText3, editText4;
    private View decorView;
    private SoftLinearLayout mRoot;
    private FrameLayout mTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        FlingRecyclerView recyclerView = findViewById(R.id.recycler);
        mTop = findViewById(R.id.tv_top);
        mTool = findViewById(R.id.tv_tool);
        // mATool = findViewById(R.id.tv_an);
        mHeader = findViewById(R.id.header);
        scrollView = findViewById(R.id.scroll);
        normal = findViewById(R.id.normalbar);
        //decorView = getWindow().getDecorView();
        editText1 = findViewById(R.id.edit_1);
        editText2 = findViewById(R.id.edit_2);
        editText3 = findViewById(R.id.edit_3);
        editText4 = findViewById(R.id.edit_4);
      /*  editText1.setOnFocusChangeListener(this);
        editText2.setOnFocusChangeListener(this);
        editText3.setOnFocusChangeListener(this);
        editText4.setOnFocusChangeListener(this);*/
        mRoot = findViewById(R.id.root);
        mTopBar = findViewById(R.id.topbar);
        mRoot.hasStatusBar(false);
        mRoot.setOnToggleChangedListener(new SoftLinearLayout.OnToggleChangedListener() {
            @Override
            public void onToggleChanged(boolean isToggle) {
                Log.d("MainActivity", "isToggle:" + isToggle);
            }
        });
      /*  mRoot.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    *//**
         * the result is pixels
         *//*

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        mRoot.getWindowVisibleDisplayFrame(r);
                        decorView = getWindow().getDecorView();
                        int screenHeight = mRoot.getRootView().getHeight();
                        int heightDifference = screenHeight - (r.bottom - r.top);
                        Log.e("Keyboard Size", "Size: " + heightDifference + "decorTop:" + decorView.getTop());

                        //boolean visible = heightDiff > screenHeight / 3;
                    }
                });*/
        /*this.addOnSoftKeyBoardVisibleListener(this, new IKeyBoardVisibleListener() {
            @Override
            public void onSoftKeyBoardVisible(boolean visible, int windowBottom) {
                Log.e("TAG", "visible:" + visible + "    windowBottom:" + windowBottom);
            }
        });*/
    }

    /*private void controlKeyboardLayout(final View root, final View needToScrollView) {

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                //获取当前界面可视部分
                TestActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = TestActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                //拿到需要调整的高度,这里需要按照实际情况计算你们自己的高度
                //博主需要的高度是：屏幕宽度-状态栏高度-标题栏高度-键盘的高度
                int height = screenHeight - heightDifference;
                //如果计算出来的和原来的不一样,那么就调整一下
                if (height != layoutParams.height) {
                    //设置新的高度
                    layoutParams.height = height;
                    //重新布局
                    mHeader.requestLayout();
                }
            }
        });
    }*/
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    private Rect r = new Rect();

                    @Override
                    public void onGlobalLayout() {
                        TestActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                        //获取屏幕的高度
                        int screenHeight = TestActivity.this.getWindow().getDecorView().getRootView().getHeight();
                        //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                        int heightDifference = screenHeight - r.bottom;
                        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                        //拿到需要调整的高度,这里需要按照实际情况计算你们自己的高度
                        //博主需要的高度是：屏幕宽度-状态栏高度-标题栏高度-键盘的高度
                        int height = screenHeight - heightDifference;
                        //如果计算出来的和原来的不一样,那么就调整一下
                        if (height != layoutParams.height) {
                            //设置新的高度
                            layoutParams.height = height;
                            //重新布局
                            mRoot.requestLayout();
                        }
                    }
                });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edit_1:
                    controlKeyboardLayout(mRoot, editText1);
                    break;
                case R.id.edit_2:
                    controlKeyboardLayout(mRoot, editText2);
                    break;
                case R.id.edit_3:
                    controlKeyboardLayout(mRoot, editText3);
                    break;
                case R.id.edit_4:
                    controlKeyboardLayout(mRoot, editText4);
                    break;
            }
        }
    }

    public boolean isViewCovered(final View view) {
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        if (!totalViewVisible)//if any part of the view is clipped by any of its parents,return true
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            if (currentParent.getVisibility() != View.VISIBLE)//if the parent of view is not visible,return true
                return true;

            int start = indexOfViewInParent(currentView, currentParent);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                Rect viewRect = new Rect();
                view.getGlobalVisibleRect(viewRect);
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                if (Rect.intersects(viewRect, otherViewRect))//if view intersects its older brother(covered),return true
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }

    private int indexOfViewInParent(View view, ViewGroup parent) {
        int index;
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == view)
                break;
        }
        return index;
    }
}
