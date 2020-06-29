package com.example.michaelarkhangelskiy_final.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter {
    private ArrayList<NewsItem> items;
    private Context parentContext;

    public NewsItemAdapter(List<NewsItem> items, Context parentContext) {
        this.items = new ArrayList<NewsItem>(items);
        this.parentContext = parentContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final NewsItemViewHolder itemholder = (NewsItemViewHolder) holder;
        itemholder.getBackground().setImageBitmap(items.get(position).getImgLink());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getArticleLink()));
                parentContext.startActivity(newIntent);
            }
        });
        itemholder.getAuthor().setText(items.get(position).getAuthor());
        itemholder.getTitle().setText(items.get(position).getTitle());
        itemholder.getDate().setText(items.get(position).getPublished().toString());
        itemholder.getSummary().setText(items.get(position).getSummary());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class NewsItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView background;
        private TextView title, author, date, summary;
        private Switch saved;

        public ImageView getBackground() {
            return background;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getSummary() {
            return summary;
        }

        public Switch getSaved() {
            return saved;
        }

        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.article_author);
            date = itemView.findViewById(R.id.article_published);
            summary = itemView.findViewById(R.id.article_summary);
            saved = itemView.findViewById(R.id.news_saved_switch);
            background = itemView.findViewById(R.id.news_background);
        }
    }
}
