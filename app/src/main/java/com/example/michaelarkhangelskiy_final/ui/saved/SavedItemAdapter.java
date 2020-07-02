package com.example.michaelarkhangelskiy_final.ui.saved;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SavedItemAdapter extends RecyclerView.Adapter  {
    public ArrayList<SavedItem> items;
    private Context parentContext;

    public SavedItemAdapter(List<SavedItem> items, Context parentContext) {
        this.items = new ArrayList<SavedItem>(items);
        this.parentContext = parentContext;
    }

    public void newList(ArrayList<SavedItem> list){
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new SavedItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final SavedItemViewHolder itemholder = (SavedItemViewHolder) holder;
        Picasso.get().load(items.get(position).getImage()).into(itemholder.getBackground());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.get(position).getClicked() != null) {
                    Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getClicked()));
                    parentContext.startActivity(newIntent);
                }
            }
        });
        itemholder.getAuthor().setText(items.get(position).getAuthor());
        itemholder.getTitle().setText(items.get(position).getTitle());
        itemholder.getDate().setText(items.get(position).getDate());
        itemholder.getSummary().setText(items.get(position).getSummary());
        itemholder.getSaved().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedItem removed = items.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                SavedItemDataSource ds = new SavedItemDataSource(parentContext);
                try {
                    ds.open();
                    SavedViewModel.removeItem(removed);
                    ds.removeItem(removed);
                    ds.close();
                }
                catch (Exception e) { }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SavedItemViewHolder extends RecyclerView.ViewHolder {
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

        public SavedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.article_author);
            date = itemView.findViewById(R.id.article_published);
            summary = itemView.findViewById(R.id.article_summary);
            saved = itemView.findViewById(R.id.news_save_button);
            saved.setText("Delete");
            background = itemView.findViewById(R.id.news_background);
        }
    }
}
