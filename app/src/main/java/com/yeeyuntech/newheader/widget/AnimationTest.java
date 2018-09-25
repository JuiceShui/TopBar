package com.yeeyuntech.newheader.widget;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yeeyuntech.newheader.R;

/**
 * Description: Jojo on 2018/4/10 ,Copyright YeeyunTech
 */
public class AnimationTest extends AppCompatActivity {
    Button btnTarget, btnTarget1;
    TextView tvFade, tvSlide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_test);
        final LinearLayout transitionsContainerTarget = (LinearLayout) findViewById(R.id.ll_container_target);
        btnTarget = (Button) findViewById(R.id.btn_target);
        btnTarget1 = (Button) findViewById(R.id.btn_target_1);
        tvFade = (TextView) findViewById(R.id.tv_target_fade);
        tvSlide = (TextView) findViewById(R.id.tv_target_slide);
        btnTarget.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AnimationUtils.showAnim(tvFade, tvSlide);

            }
        });
        btnTarget1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.hideAnim(tvFade, tvSlide);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimationUtils.initAnim(tvFade, tvSlide);
    }
}
