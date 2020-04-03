package com.example.shownews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.shownews.Models.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private OnItemClickListener myListener;
    private List<Article> articles;
    private Context context;


    public Adapter(List<Article> art,Context ctx){
        this.articles = art;
        this.context = ctx;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);

        return new MyViewHolder(view,myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Article currentAtricle = articles.get(position);

        //Setting image properties with requestOptions of Glide Library

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_result);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context).load(currentAtricle.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;

                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);


        holder.title.setText(currentAtricle.getTitle());
        holder.desc.setText(currentAtricle.getDescription());
        holder.author.setText(currentAtricle.getAuthor());
        holder.source.setText(currentAtricle.getSource().getName());




    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setListener(OnItemClickListener onItemClickListener){


        this.myListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onItaClick(View view,int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView title, author, desc ,published_at, source;
        ImageView image;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener) {
            super(itemView);

            //dont't know the use of this line.
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.title);
            author =itemView.findViewById(R.id.author);
            desc = itemView.findViewById(R.id.desc);
            published_at = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);

            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);

            this.onItemClickListener = itemClickListener;



        }




        @Override
        public void onClick(View v) {

            onItemClickListener.onItaClick(v,getAdapterPosition());
        }
    }



}
