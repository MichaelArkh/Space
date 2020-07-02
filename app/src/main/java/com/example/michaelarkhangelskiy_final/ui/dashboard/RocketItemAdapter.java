package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.ui.saved.SavedViewModel;

import java.util.ArrayList;
import java.util.List;

public class RocketItemAdapter extends RecyclerView.Adapter {
    private ArrayList<RocketItem> items;
    private Context parentContext;

    public RocketItemAdapter(List<RocketItem> items, Context parentContext) {
        this.items = new ArrayList<RocketItem>(items);
        this.parentContext = parentContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new RocketItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RocketItemViewHolder itemholder = (RocketItemViewHolder) holder;

        itemholder.getBackground().setImageBitmap(items.get(position).getImage());
        itemholder.getAuthor().setText(items.get(position).getLocation());
        itemholder.getTitle().setText(items.get(position).getName());
        itemholder.getDate().setText(items.get(position).getStartTime().toString());
        itemholder.getSummary().setText(items.get(position).getSummary());
        itemholder.getSaved().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedViewModel.addedItem(items.get(position), parentContext);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getWikiURL()));
                parentContext.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RocketItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView background;
        private TextView title, author, date, summary;
        private Button saved;

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

        public Button getSaved() {
            return saved;
        }

        public RocketItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.article_author);
            date = itemView.findViewById(R.id.article_published);
            summary = itemView.findViewById(R.id.article_summary);
            saved = itemView.findViewById(R.id.news_save_button);
            background = itemView.findViewById(R.id.news_background);
        }
    }
}
