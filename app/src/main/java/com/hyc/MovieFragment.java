package com.hyc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hyc on 2017/4/12 16:25
 */

public class MovieFragment extends Fragment {

    private CompositeSubscription mCompositeSubscription;

    private ApiStores mApiStores;

    PullLoadMoreRecyclerView prv_movie;

    private MovieAdapter movieAdapter;

    public static int page=0;

    private  boolean isLoaded=false;

    public static boolean isNeed=false;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_movie, container,false);
        initView(chatView);
        mApiStores=AppClient.movieRetrofit(ApiStores.booksBaseUrl).create(ApiStores.class);
        return chatView;
    }

    private void initView(View chatView) {
        prv_movie= (PullLoadMoreRecyclerView) chatView.findViewById(R.id.prv_movie);
        prv_movie.setLinearLayout();
        prv_movie.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                if (movieAdapter.getItemCount()==0){
                    loadData();
                }else {
                    prv_movie.setPullLoadMoreCompleted();
                }
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        movieAdapter=new MovieAdapter(null);
        movieAdapter.setItemClickListener(new MovieAdapter.OnItemClickedListener() {
            @Override
            public void onClick(ImageView imageView, HotMovieBean.SubjectsBean subjectsBean) {
                DataStores.setSubjectsBean(subjectsBean);
                Pair<View, String> p = new Pair<View, String>(imageView, "head");//haderIv是头像控件
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                getActivity().startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), p).toBundle());
            }
        });
        prv_movie.setAdapter(movieAdapter);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&prv_movie!=null&&!isLoaded&&movieAdapter.getItemCount()==0){
            prv_movie.setRefreshing(true);
            loadData();
        }else if (!isVisibleToUser&&isNeed){
            isLoaded=false;
        }
    }

    private void loadData(){
        addSubscription(mApiStores.loadMovieTop250("", "top250", 10 * (page++), 10), new ApiCallback<HotMovieBean>() {
            @Override
            public void onSuccess(HotMovieBean model) {
                if (null!=model&&model.getSubjects().size()>0){
                    movieAdapter.addData(model.getSubjects());
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                prv_movie.setPullLoadMoreCompleted();
            }
        });
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
