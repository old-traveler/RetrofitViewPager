package com.hyc;

import android.app.ActivityOptions;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mingle.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MovieDetailsActivity extends AppCompatActivity {

    private CompositeSubscription mCompositeSubscription;

    private ApiStores mApiStores;

    private boolean isCollection=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initView(DataStores.getSubjectsBean());
        mApiStores=AppClient.moveDetailsRetrofit(ApiStores.booksBaseUrl).create(ApiStores.class);
        loadMoreDetailsData(DataStores.getSubjectsBean().getId());
    }


    private void loadMoreDetailsData(String id) {
        mLoadingView.setVisibility(View.VISIBLE);
        final TextView tv_movie_said=(TextView) findViewById(R.id.tv_movie_said);
        final TextView tv_movie_info= (TextView) findViewById(R.id.tv_movie_info);
        addSubscription(mApiStores.loadMovieDetails(id), new ApiCallback<MovieMoreData>() {
            @Override
            public void onSuccess(MovieMoreData model) {
                for (String s:model.getAka()){
                    tv_movie_said.append(model.getAka().indexOf(s)==0?s:"、"+s);
                }
                tv_movie_info.setText(model.getSummary());
                for (String m:model.getCountries()){
                    tv_movie_place.append(model.getCountries().indexOf(m)==0?m:"、"+m);
                }
                List<MovieDetailsPerson> data=new ArrayList<MovieDetailsPerson>();
                for (MovieMoreData.DirectorsBean d:model.getDirectors()){
                    MovieDetailsPerson person=new MovieDetailsPerson();
                    person.imageUrl=d.getAvatars().getSmall();
                    person.name=d.getName();
                    person.role="导演";
                    data.add(person);
                }
                for (MovieMoreData.CastsBean cast:model.getCasts()){
                    MovieDetailsPerson person=new MovieDetailsPerson();
                    person.imageUrl=cast.getAvatars().getSmall();
                    person.name=cast.getName();
                    person.role="演员";
                    data.add(person);
                }
                mMovieDetailsAdapter.addData(data);
            }

            @Override
            public void onFailure(String msg) {
                mLoadingView.setVisibility(View.GONE);
                Toast.makeText(MovieDetailsActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                mLoadingView.setVisibility(View.GONE);
                mCardView.setVisibility(View.VISIBLE);
            }
        });
    }

    LoadingView mLoadingView;
    TextView tv_movie_place;
    RecyclerView rv_movie_people;
    MovieDetailsAdapter mMovieDetailsAdapter;
    CardView mCardView;
    private void initView(HotMovieBean.SubjectsBean subjectsBean) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mLoadingView= (LoadingView) findViewById(R.id.loadView);
        mCardView= (CardView) findViewById(R.id.cv_more_details);
        ImageView iv_movie_details= (ImageView) findViewById(R.id.iv_movie_details);
        ImageView iv_movie_background= (ImageView) findViewById(R.id.iv_movie_background);
        TextView tv__movie_score= (TextView) findViewById(R.id.tv_movie_score);
        TextView tv_movie_director= (TextView) findViewById(R.id.tv_movie_director);
        TextView tv_movie_star= (TextView) findViewById(R.id.tv_movie_star);
        TextView tv_movie_type= (TextView) findViewById(R.id.tv_movie_type);
        TextView tv_movie_time= (TextView) findViewById(R.id.tv_movie_time);
        tv_movie_place= (TextView) findViewById(R.id.tv_movie_place);
        rv_movie_people= (RecyclerView) findViewById(R.id.rv_movie_people);
        rv_movie_people.setLayoutManager(new LinearLayoutManager(this));
        mMovieDetailsAdapter=new MovieDetailsAdapter(null);
        rv_movie_people.setAdapter(mMovieDetailsAdapter);
        final FloatingActionButton fab_movie= (FloatingActionButton) findViewById(R.id.fab_movie);
        Glide.with(this)
                .load(subjectsBean.getImages().getLarge())
                .into(iv_movie_details);
        iv_movie_details.setTransitionName("head");//第二个Activity里的头像控件
        Glide.with(this)
                .load(subjectsBean.getImages().getLarge())
                .override(1080,1980)
                .bitmapTransform(new BlurTransformation(this,23,4))
                .into(iv_movie_background);
        collapsingToolbar.setTitle(subjectsBean.getTitle());
        tv__movie_score.setText("评分："+subjectsBean.getRating().getAverage()+"  "
                +subjectsBean.getCollect_count()+"人评价");
        tv_movie_director.setText("导演："+subjectsBean.getDirectors().get(0).getName());
        for (HotMovieBean.SubjectsBean.CastsBean bean:subjectsBean.getCasts()){
            if (subjectsBean.getCasts().indexOf(bean)==0){
                tv_movie_star.setText("主演："+bean.getName());
            }else {
                tv_movie_star.append("/"+bean.getName());
            }
        }
        for (String type:subjectsBean.getGenres()){
            if (subjectsBean.getGenres().indexOf(type)==0){
                tv_movie_type.setText("类型："+type);
            }else {
                tv_movie_type.append("/"+type);
            }
        }
        tv_movie_time.setText("上映日期："+subjectsBean.getYear());
        tv_movie_place.setText("制片国家/地区：");
        fab_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCollection){
                    fab_movie.setImageResource(R.drawable.shoucansucessful);
                    isCollection=true;
                    fab_movie.setClickable(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab_movie.setClickable(true);
                        }
                    }, 2000);
                    Snackbar.make(v, "收藏成功", Snackbar.LENGTH_SHORT)
                            .setAction("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fab_movie.setImageResource(R.drawable.shoucan);
                                    isCollection=false;
                                }
                            })
                            .show();

                }else {
                    isCollection=false;
                    fab_movie.setImageResource(R.drawable.shoucan);
                }

            }
        });
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

    public void addSubscription(rx.Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
