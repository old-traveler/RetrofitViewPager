package com.hyc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hyc on 2017/4/15 11:01
 */

public class BooksAdapter extends BaseQuickAdapter<BookApi.BooksBean> {

    public BooksAdapter(List<BookApi.BooksBean> data) {
        super(R.layout.item_book,data);
    }

    private BooksItemClickedListener booksItemClickedListener;

    public BooksItemClickedListener getBooksItemClickedListener() {
        return booksItemClickedListener;
    }

    public void setBooksItemClickedListener(BooksItemClickedListener booksItemClickedListener) {
        this.booksItemClickedListener = booksItemClickedListener;
    }

    public interface BooksItemClickedListener{
        void onClicked(Intent intent,ImageView imageView);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final BookApi.BooksBean booksBean) {
        Glide.with(mContext)
                .load(booksBean.getImages().getLarge())
                .into((ImageView) baseViewHolder.getView(R.id.img_book));
        baseViewHolder.setText(R.id.tv_title,booksBean.getTitle());
        baseViewHolder.setText(R.id.tv_score,"评分："+booksBean.getRating().getAverage());
        baseViewHolder.setOnClickListener(R.id.img_book, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,BookDetailsActivity.class);
                intent.putExtra("img",booksBean.getImages().getLarge());
                intent.putExtra("title",booksBean.getTitle());
                intent.putExtra("author",booksBean.getAuthor().get(0));
                intent.putExtra("rat",booksBean.getRating().getAverage());
                intent.putExtra("count_rat",booksBean.getRating().getNumRaters());
                intent.putExtra("publish_time",booksBean.getPubdate());
                intent.putExtra("publish_place",booksBean.getPublisher());
                intent.putExtra("book_abstract",booksBean.getSummary());
                intent.putExtra("author_info",booksBean.getAuthor_intro());
                intent.putExtra("tab",booksBean.getCatalog());
                booksItemClickedListener.onClicked(intent, (ImageView) baseViewHolder.getView(R.id.img_book));
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (position % 2 == 0) {
            DensityUtil.setViewMargin(holder.itemView, false, 10, 10, 10, 10);
        } else {
            DensityUtil.setViewMargin(holder.itemView, false, 10, 10, 10, 10);
        }
    }



}
