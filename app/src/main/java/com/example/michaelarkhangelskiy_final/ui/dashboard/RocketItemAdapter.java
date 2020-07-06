package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItemAdapter;
import com.example.michaelarkhangelskiy_final.ui.saved.SavedViewModel;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The adapter class for a rocket item recyclerView
 */
public class RocketItemAdapter extends RecyclerView.Adapter {
    private ArrayList<RocketItem> items;
    private Context parentContext;

    /**
     * Instantiates a new Rocket item adapter.
     *
     * @param items         the items
     * @param parentContext the parent context
     */
    public RocketItemAdapter(List<RocketItem> items, Context parentContext) {
        this.items = new ArrayList<RocketItem>(items);
        this.parentContext = parentContext;
    }

    /**
     * Sets new items.
     *
     * @param a the rocketItems to set
     */
    public void setNewItems(List<RocketItem> a) {
        items = new ArrayList<RocketItem>(a);
    }

    /**
     * Inflates the layout
     *
     * @param parent the parent
     * @param viewType the viewtype
     * @return the Viewholder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new RocketItemViewHolder(v);
    }

    /**
     * Sets up actions and images for the recyclerviewcell
     * @param holder the holder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RocketItemViewHolder itemholder = (RocketItemViewHolder) holder;

        //new SetImage(itemholder.getBackground()).execute(items.get(position).getImage());
        Picasso.get().load(items.get(position).getImage()).into(itemholder.getBackground());
        if(items.get(position).getImage().contains("https") || items.get(position).getImage().contains("http")) {
            Picasso.get().load(items.get(position).getImage()).into(itemholder.getBackground());
        } else {
            Picasso.get().load("https://launchlibrary1.nyc3.digitaloceanspaces.com/RocketImages/placeholder_1920.png").into(itemholder.getBackground());
        }
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

        if(items.get(position).getStartTime().before(new Date())){
            if(items.get(position).getStatus() == 3){
                itemholder.getSucceded().setText("Succeded");
                itemholder.getSucceded().setTextColor(Color.GREEN);
            } else {
                itemholder.getSucceded().setText("Failed");
                itemholder.getSucceded().setTextColor(Color.RED);
            }
        } else {
            itemholder.getSucceded().setText("");
        }
    }

    /**
     * Returns the size of the data
     * @return the size of the data
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * The type Rocket item view holder.
     */
    public static class RocketItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView background;
        private TextView title, author, date, summary, succeded;
        private Button saved;

        /**
         * Gets succeded.
         *
         * @return the succeded
         */
        public TextView getSucceded() {
            return succeded;
        }

        /**
         * Gets background.
         *
         * @return the background
         */
        public ImageView getBackground() {
            return background;
        }

        /**
         * Gets title.
         *
         * @return the title
         */
        public TextView getTitle() {
            return title;
        }

        /**
         * Gets author.
         *
         * @return the author
         */
        public TextView getAuthor() {
            return author;
        }

        /**
         * Gets date.
         *
         * @return the date
         */
        public TextView getDate() {
            return date;
        }

        /**
         * Gets summary.
         *
         * @return the summary
         */
        public TextView getSummary() {
            return summary;
        }

        /**
         * Gets saved.
         *
         * @return the saved
         */
        public Button getSaved() {
            return saved;
        }

        /**
         * Instantiates a new Rocket item view holder.
         *
         * @param itemView the item view
         */
        public RocketItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.article_author);
            date = itemView.findViewById(R.id.article_published);
            summary = itemView.findViewById(R.id.article_summary);
            saved = itemView.findViewById(R.id.news_save_button);
            background = itemView.findViewById(R.id.news_background);
            succeded = itemView.findViewById(R.id.succededText);
        }
    }
}
