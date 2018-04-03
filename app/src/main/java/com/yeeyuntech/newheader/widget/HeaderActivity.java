package com.yeeyuntech.newheader.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.yeeyuntech.newheader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Jojo on 2018/4/2 ,Copyright YeeyunTech
 */
public class HeaderActivity extends Activity {
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_header);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.normalbar);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            data.add("This is " + i + "  ");
        }
        mAdapter = new SimpleAdapter(data);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }
}
