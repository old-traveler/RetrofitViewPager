package com.hyc;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class WelfareDetailsActivity extends AppCompatActivity  {

    static String imageurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welfare_details);
        initView();
        viewById.setTransitionName("head");
    }
    ImageView viewById;
    private void initView() {
        viewById = (ImageView) findViewById(R.id.iv_welfareDetailsActivity);
        Glide.with(this)
                .load(imageurl)
                .into(viewById);
        ImageView image=(ImageView) findViewById(R.id.iv_welfare_background);
        Glide.with(this)
                .load(imageurl)
                .override(1500,2100)
                .bitmapTransform(new BlurTransformation(this,23,4))
                .into(image);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run() {
                        try{
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        }
                        catch (Exception e) {
                            Log.e("Exception when onBack", e.toString());
                        }
                    }
                }.start();
            }
        });
    }



}
