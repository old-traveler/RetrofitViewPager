package com.hyc;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hyc on 2017/4/13 13:06
 */

public class WelfareAdapter extends BaseQuickAdapter<WelfareResult.Result>{


    public WelfareAdapter(List data) {
        super(R.layout.item_welfare, data);
    }

    private WelfareItemClickedListener welfareItemClickedListener;

    public void setWelfareItemClickedListener(WelfareItemClickedListener welfareItemClickedListener) {
        this.welfareItemClickedListener = welfareItemClickedListener;
    }

    public interface WelfareItemClickedListener{
        void OnClick(ImageView imageView);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final WelfareResult.Result result) {
        Glide.with(baseViewHolder.getView(R.id.img).getContext())
                .load(result.getUrl())
                .override(540,700)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.drawable.img_default_meizi)
                .into((ImageView) baseViewHolder.getView(R.id.img));
        baseViewHolder.setOnClickListener(R.id.img,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelfareDetailsActivity.imageurl=result.getUrl();
                welfareItemClickedListener.OnClick((ImageView) baseViewHolder.getView(R.id.img));


            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (position % 2 == 0) {
            DensityUtil.setViewMargin(holder.itemView, false, 12, 6, 12, 0);
        } else {
            DensityUtil.setViewMargin(holder.itemView, false, 6, 12, 12, 0);
        }


    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
