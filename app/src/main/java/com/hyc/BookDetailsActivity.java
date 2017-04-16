package com.hyc;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class BookDetailsActivity extends AppCompatActivity {

    private boolean isCollection=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Intent intent = getIntent();
        initView(intent);
        ImageView imageView=(ImageView) findViewById(R.id.img_book_details);
        imageView.setTransitionName("head");

    }

    private void initView(Intent intent) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(intent.getStringExtra("title"));
        collapsingToolbar.setAlpha(0.9f);
        Glide.with(this)
                .load(intent.getStringExtra("img"))
                .into((ImageView) findViewById(R.id.img_book_details));
        Glide.with(this)
                .load(intent.getStringExtra("img"))
                .override(1080,1980)
                .bitmapTransform(new BlurTransformation(this,23,4))
                .into((ImageView) findViewById(R.id.img_details_background));
        setTextData(R.id.tv_author,"作者："+intent.getStringExtra("author"));
        setTextData(R.id.tv_details_score,"评分："+intent.getStringExtra("rat")+" "+intent.getIntExtra("count_rat",0)+"人评价");
        setTextData(R.id.tv_publish_time,"出版时间："+intent.getStringExtra("publish_time"));
        setTextData(R.id.tv_publish_place,"出版社："+intent.getStringExtra("publish_place"));
        setTextData(R.id.tv_book_abstract,intent.getStringExtra("book_abstract"));
        setTextData(R.id.tv_author_info,intent.getStringExtra("author_info"));
        setTextData(R.id.tv_book_tab,intent.getStringExtra("tab"));
        final FloatingActionButton fb= (FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCollection){
                    fb.setImageResource(R.drawable.shoucansucessful);
                    isCollection=true;
                    fb.setClickable(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fb.setClickable(true);
                        }
                    }, 2000);
                    Snackbar.make(v, "收藏成功", Snackbar.LENGTH_SHORT)
                            .setAction("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fb.setImageResource(R.drawable.shoucan);
                                    isCollection=false;
                                }
                            })
                            .show();

                }else {
                    isCollection=false;
                    fb.setImageResource(R.drawable.shoucan);
                }

            }
        });
    }

    private void setTextData(int id,String text){
        TextView t=(TextView) findViewById(id);
        t.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
