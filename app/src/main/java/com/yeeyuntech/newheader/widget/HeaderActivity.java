package com.yeeyuntech.newheader.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yeeyuntech.newheader.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: Jojo on 2018/4/2 ,Copyright YeeyunTech
 */
public class HeaderActivity extends AppCompatActivity implements HeaderView.onLoadMoreListener, HeaderView.onRefreshListener, HeaderView.OnToolsVisibilityChangeListener {
    private SimpleAdapter mAdapter;
    private HeaderView mHeader;
    private char A = 'a';
    private int count = 40;
    private int index = 1;
    private List<String> mData = new ArrayList<>();
    private Disposable disposable;
    private TextView mTop, mTool, mATool;
    private Button jump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_header);
        FlingRecyclerView recyclerView = findViewById(R.id.recycler);
        mTop = findViewById(R.id.tv_top);
        mTool = findViewById(R.id.tv_tool);
        jump = findViewById(R.id.jump);
        // mATool = findViewById(R.id.tv_an);
        mHeader = findViewById(R.id.header);
        mHeader.setOnLoadMoreListener(this);
        mHeader.setOnRefreshListener(this);
        mHeader.setOnToolsVisibleChangeListener(this);
        mAdapter = new SimpleAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        init();
    }

    private void init() {
        List<String> data = getData(false, false);
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(HeaderActivity.this, mTop, "share");
                ActivityCompat.startActivity(HeaderActivity.this, new Intent(HeaderActivity.this, TestActivity.class), compat.toBundle());
            }
        });
    }

    @Override
    public void loadMore() {
        List<String> data = getData(true, false);
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        disposable = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, List<String>>() {
                    @Override
                    public List<String> apply(Long aLong) throws Exception {
                        return getData(false, true);
                    }
                }).subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        mData.clear();
                        mData.addAll(strings);
                        mAdapter.notifyDataSetChanged();
                        mHeader.onRefreshFinish();
                    }
                });
    }

    @Override
    public void onCancelRefresh() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onToolsInvisible(boolean has) {
        mTop.setText("This is Top");
        mTool.setText("");
        // mATool.setText("");
    }

    @Override
    public void onToolsVisible(boolean has) {
        mTop.setText("");
        mTool.setText("This is Top");
        // mATool.setText("Annnnnnn");
    }

    private List<String> getData(boolean isLoadMore, boolean isRefresh) {
        List<String> data = new ArrayList<>();
        if (isLoadMore) {
            int size = mData.size();
            for (int i = 0; i < count; i++) {
                data.add("This is " + i + size + "  ");
            }
        } else if (isRefresh) {
            A++;
            for (int i = 0; i < count; i++) {
                data.add("This is " + i + A + "  ");
            }
        } else {
            for (int i = 0; i < count; i++) {
                data.add("This is " + i + "  ");
            }
        }
        return data;
    }
}
