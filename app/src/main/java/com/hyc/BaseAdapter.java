package com.hyc;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/4/13 22:46
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.MyAdapterView> {
    List<WelfareResult.Result> data;

    @Override
    public MyAdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAdapterView(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_welfare, parent,
                false));
    }

    @Override
    public void onBindViewHolder(MyAdapterView holder, int position) {
        if (position % 2 == 0) {
            DensityUtil.setViewMargin(holder.itemView, false, 12, 6, 12, 0);
        } else {
            DensityUtil.setViewMargin(holder.itemView, false, 6, 12, 12, 0);
        }
        Glide.with(holder.imageView.getContext())
                .load(data.get(position).getUrl())
                .override(540,700)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public void addData(List<WelfareResult.Result> datas){
        if (data==null){
            data=new ArrayList<>();
        }
        for (WelfareResult.Result result:datas){
            data.add(getItemCount(),result);
        }
        notifyItemInserted(getItemCount());
    }



    public class MyAdapterView extends ViewHolder{
        ImageView imageView;

        public MyAdapterView(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
