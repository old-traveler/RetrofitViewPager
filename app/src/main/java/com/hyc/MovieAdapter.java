package com.hyc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hyc on 2017/4/15 21:34
 */

public class MovieAdapter extends BaseQuickAdapter<HotMovieBean.SubjectsBean> {

    public interface OnItemClickedListener{
        void onClick(ImageView imageView, HotMovieBean.SubjectsBean subjectsBean);
    }

    private OnItemClickedListener onItemClickedListener;

    public MovieAdapter(List data) {
        super(R.layout.item_movie,data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final HotMovieBean.SubjectsBean o) {
        Glide.with(mContext)
                .load(o.getImages().getLarge())
                .into((ImageView) baseViewHolder.getView(R.id.iv_one_photo));
        baseViewHolder.setText(R.id.tv_one_title,o.getTitle());
        baseViewHolder.setText(R.id.tv_one_directors,"导演："+o.getDirectors().get(0).getName());
        for (HotMovieBean.SubjectsBean.CastsBean bean:o.getCasts()){
            TextView v=baseViewHolder.getView(R.id.tv_one_casts);
            if (o.getCasts().indexOf(bean)==0){
                v.setText("主演："+bean.getName());
            }else {
                v.append("/"+bean.getName());
            }
        }
        for (String type:o.getGenres()){
            TextView t=baseViewHolder.getView(R.id.tv_one_genres);
            if (o.getGenres().indexOf(type)==0){
                t.setText("类型："+type);
            }else {
                t.append("/"+type);
            }
        }
        baseViewHolder.setText(R.id.tv_one_rating_rate,"评分："+o.getRating().getAverage());
        baseViewHolder.setOnClickListener(R.id.ll_one_item,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","点击了itemView");
                if (onItemClickedListener!=null){
                    Log.d("TAG","执行了onClick事件");
                    onItemClickedListener.onClick((ImageView)baseViewHolder.getView(R.id.iv_one_photo),o);
                }
            }
        });
    }

    public void setItemClickListener(OnItemClickedListener onItemClickedListener){
        this.onItemClickedListener=onItemClickedListener;
    }
}
