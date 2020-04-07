package com.vishnu.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vishnu.newsapp.Model.Articles;
import com.vishnu.newsapp.NewsDetailsAcitivity;
import com.vishnu.newsapp.R;
import com.vishnu.newsapp.Utils.DBHelper;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Articles> articles;
    DBHelper wishlistDB;

    public HomeAdapter(Context context, ArrayList<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String imageUrl,webUrl;
        holder.title.setText(articles.get(position).getTitle());
        holder.publisher.setText(articles.get(position).getAuthor());

        Glide
                .with(context)
                .load(articles.get(position).getUrlToImage())
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView title;
        private TextView publisher;
        private Button saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            publisher = (TextView) itemView.findViewById(R.id.publisher_text);
            saveButton = (Button) itemView.findViewById(R.id.save_button);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Adapter:-","Inserting Values into Wishlist Database");
                    wishlistDB.insertWishlist(articles.get(getLayoutPosition()).getSource(),articles.get(getLayoutPosition()).getTitle(),articles.get(getLayoutPosition()).getDescription(),articles.get(getLayoutPosition()).getAuthor(),articles.get(getLayoutPosition()).getPublishedAt(),articles.get(getLayoutPosition()).getContent(),articles.get(getLayoutPosition()).getUrl(),articles.get(getLayoutPosition()).getUrlToImage());
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), NewsDetailsAcitivity.class);
            intent.putExtra("url",articles.get(getLayoutPosition()).getUrl());
            context.startActivity(intent);
        }
    }
}
