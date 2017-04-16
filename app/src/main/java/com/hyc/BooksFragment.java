package com.hyc;

import android.app.ActivityOptions;
import android.content.Intent;
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
 * Created by hyc on 2017/4/12 16:24
 */

public class BooksFragment extends Fragment{

    private CompositeSubscription mCompositeSubscription;

    private ApiStores mApiStores;

    private PullLoadMoreRecyclerView prv_books;

    private BooksAdapter booksAdapter;

    private int pager=0;

    private boolean isLoaded=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_books, container,false);
        initView(chatView);
        mApiStores=AppClient.booksRetrofit(ApiStores.booksBaseUrl).create(ApiStores.class);
        return chatView;

    }

    private void initView(View chatView) {
        prv_books= (PullLoadMoreRecyclerView) chatView.findViewById(R.id.prv_books);
        prv_books.setGridLayout(3);
        prv_books.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                prv_books.setPullLoadMoreCompleted();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        booksAdapter = new BooksAdapter(null);
        booksAdapter.setBooksItemClickedListener(new BooksAdapter.BooksItemClickedListener() {
            @Override
            public void onClicked(Intent intent, ImageView imageView) {
                Pair<View, String> p = new Pair<View, String>(imageView, "head");//haderIv是头像控件
                getActivity().startActivity(intent, ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), p).toBundle());
            }
        });
        prv_books.setAdapter(booksAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&prv_books!=null&&!isLoaded){
            prv_books.setRefreshing(true);
            loadData();
            isLoaded=true;
        }
    }

    private void loadData(){
        addSubscription(mApiStores.loadBooksData("","小说",12*(pager++), 12), new ApiCallback<BookApi>() {
            @Override
            public void onSuccess(BookApi model) {
                if (null!=model&&model.getBooks().size()>0){
                    booksAdapter.addData(model.getBooks());
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                prv_books.setPullLoadMoreCompleted();
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
