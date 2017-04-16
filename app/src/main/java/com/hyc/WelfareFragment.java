package com.hyc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hyc on 2017/4/12 15:41
 */

public class WelfareFragment extends Fragment {

    private CompositeSubscription mCompositeSubscription;

    private ApiStores mApiStores;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_welfare, container,false);
        initView(chatView);
        mApiStores=AppClient.welfareRetrofit(ApiStores.welfareBaseUrl).create(ApiStores.class);
        rv_welfare.setRefreshing(true);
        addSubscription(mApiStores.loadWelfareData(10, 1), new ApiCallback<WelfareResult>() {
            @Override
            public void onSuccess(WelfareResult model) {
                if (!model.isError()&&model.getResults().size()==10){
                    mWelfareAdapter.addData(model.getResults());
                }
            }
            @Override
            public void onFailure(String msg) {
                rv_welfare.setRefreshing(false);
            }
            @Override
            public void onFinish() {
                rv_welfare.setPullLoadMoreCompleted();
            }
        });
        return chatView;
    }

    int page=1;
    PullLoadMoreRecyclerView rv_welfare;
    WelfareAdapter mWelfareAdapter;
    private void initView(View chatView) {
        rv_welfare= (PullLoadMoreRecyclerView) chatView.findViewById(R.id.rv_welfare);
        rv_welfare.setStaggeredGridLayout(2);
        rv_welfare.setItemAnimator(new DefaultItemAnimator());
        mWelfareAdapter=new WelfareAdapter(null);
        mWelfareAdapter.setWelfareItemClickedListener(new WelfareAdapter.WelfareItemClickedListener() {
            @Override
            public void OnClick(ImageView imageView) {
                Pair<View, String> p = new Pair<View, String>(imageView, "head");//haderIv是头像控件
                Intent intent = new Intent(getActivity(), WelfareDetailsActivity.class);
                getActivity().startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), p).toBundle());
            }
        });
        rv_welfare.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                rv_welfare.setPullLoadMoreCompleted();
            }

            @Override
            public void onLoadMore() {
                addSubscription(mApiStores.loadWelfareData(10, ++page), new ApiCallback<WelfareResult>() {
                    @Override
                    public void onSuccess(WelfareResult model) {
                        if (!model.isError()&&model.getResults().size()>0){
                            mWelfareAdapter.addData(model.getResults());
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.d("TAG","出错了"+msg);
                    }

                    @Override
                    public void onFinish() {
                        rv_welfare.setPullLoadMoreCompleted();
                    }
                });
            }
        });
        rv_welfare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        Glide.with(getContext())
                                .pauseRequests();
                        break;
                    case MotionEvent.ACTION_UP:
                        Glide.with(getContext())
                                .resumeRequests();
                        break;
                }
                return false;
            }
        });
        rv_welfare.setAdapter(mWelfareAdapter);
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
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
