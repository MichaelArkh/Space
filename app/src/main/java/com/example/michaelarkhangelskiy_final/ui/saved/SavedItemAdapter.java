package com.example.michaelarkhangelskiy_final.ui.saved;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The type Saved item adapter.
 */
public class SavedItemAdapter extends RecyclerView.Adapter  {
    /**
     * The Items.
     */
    public ArrayList<SavedItem> items;
    private Context parentContext;

    /**
     * Instantiates a new Saved item adapter.
     *
     * @param items         the items
     * @param parentContext the parent context
     */
    public SavedItemAdapter(List<SavedItem> items, Context parentContext) {
        this.items = new ArrayList<SavedItem>(items);
        this.parentContext = parentContext;
    }

    /**
     * Adds a new list.
     *
     * @param list the list
     */
    public void newList(ArrayList<SavedItem> list){
        items = list;
        notifyDataSetChanged();
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
        return new SavedItemViewHolder(v);
    }

    /**
     * Sets up actions and images for the recyclerviewcell
     * @param holder the holder
     * @param position the position
     */
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
        try {
            DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = format.parse(items.get(position).getDate());
            if (date.before(new Date()) && items.get(position).getStatus() != -1) {
                if (items.get(position).getStatus() == 3) {
                    itemholder.getSucceded().setText("Succeded");
                    itemholder.getSucceded().setTextColor(Color.GREEN);
                } else {
                    itemholder.getSucceded().setText("Failed");
                    itemholder.getSucceded().setTextColor(Color.RED);
                }
            } else {
                itemholder.getSucceded().setText("");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * The type Saved item view holder.
     */
    public static class SavedItemViewHolder extends RecyclerView.ViewHolder {
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
         * Instantiates a new Saved item view holder.
         *
         * @param itemView the item view
         */
        public SavedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.article_author);
            date = itemView.findViewById(R.id.article_published);
            summary = itemView.findViewById(R.id.article_summary);
            saved = itemView.findViewById(R.id.news_save_button);
            saved.setText("Delete");
            background = itemView.findViewById(R.id.news_background);
            succeded = itemView.findViewById(R.id.succededText);
        }
    }
}
