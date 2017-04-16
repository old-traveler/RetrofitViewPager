package com.hyc;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hyc on 2017/4/16 14:35
 */

public class MovieDetailsAdapter extends BaseQuickAdapter<MovieDetailsPerson> {

    public MovieDetailsAdapter(List<MovieDetailsPerson> data) {
        super(R.layout.item_movie_details,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MovieDetailsPerson movieDetailsPerson) {
        Glide.with(mContext)
                .load(movieDetailsPerson.imageUrl)
                .into((ImageView) baseViewHolder.getView(R.id.iv_avatar));
        baseViewHolder.setText(R.id.tv_person_name,movieDetailsPerson.name);
        baseViewHolder.setText(R.id.tv_person_role,movieDetailsPerson.role);
    }
}
