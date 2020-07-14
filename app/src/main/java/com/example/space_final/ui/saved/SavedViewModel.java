package com.example.space_final.ui.saved;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModel;

import com.example.space_final.R;
import com.example.space_final.database.SavedItem;
import com.example.space_final.database.SavedItemDataSource;
import com.example.space_final.ui.dashboard.RocketItem;
import com.example.space_final.ui.home.NewsItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * The view model for the saved objects
 */
public class SavedViewModel extends ViewModel {
    public static ArrayList<SavedItem> itemList = new ArrayList<SavedItem>();

    public SavedViewModel() {

    }

    /**
     * Loads items from the database
     * @param context the required context
     */
    public static void loadFromDB(Context context){
        itemList.clear();
        try {
            SavedItemDataSource ds = new SavedItemDataSource(context);
            ds.open();
            itemList.addAll(ds.getItems());
            ds.close();
        } catch (Exception e) {}
    }

    /**
     * Adds an newsitem to the database
     * @param item the item to add
     * @param context required context
     */
    public static void addedItem(NewsItem item, Context context){
        try {
            SavedItemDataSource ds = new SavedItemDataSource(context);
            ds.open();
            SavedItem converted = SavedItem.convert(item);
            converted.setItemId(ds.getLastID());
            ds.insertItem(converted);
            itemList.add(converted);
            ds.close();
        } catch (Exception e){}
        if(SavedFragment.ia != null){
            SavedFragment.ia.newList(itemList);
        }
    }

    /**
     * Replaces the current list with the new list
     * @param newItems the new list
     * @param context the required context
     */
    public static void newList(ArrayList<SavedItem> newItems, Context context){
        try {
            SavedItemDataSource ds = new SavedItemDataSource(context);
            ds.open();
            itemList.forEach(ds::removeItem);
            itemList = newItems;
            newItems.forEach(ds::insertItem);
            ds.close();
        } catch (Exception e){}
        if(SavedFragment.ia != null){
            SavedFragment.ia.newList(itemList);
        }
    }

    /**
     * Adds an rocketitem to the database
     * @param item the item to add
     * @param context required context
     */
    public static void addedItem(RocketItem item, Context context){
        try {
            SavedItemDataSource ds = new SavedItemDataSource(context);
            ds.open();
            SavedItem converted = SavedItem.convert(item);
            converted.setItemId(ds.getLastID());
            ds.insertItem(converted);
            itemList.add(converted);
            ds.close();
        } catch (Exception e){}
        if(SavedFragment.ia != null){
            SavedFragment.ia.newList(itemList);
        }
    }

    /**
     * removes an item from the list
     * @param removed the item to remove
     */
    public static void removeItem(SavedItem removed){
        itemList.remove(removed);
    }

    /**
     * Push notification generator
     * @param c the required context
     */
    public static void notifier(Context c){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "saved")
                .setSmallIcon(R.drawable.sputnik)
                .setContentTitle("Current status: ")
                .setContentText(itemList.size()+ " saves, next rocket launch at " + earlyDate().getDate())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
        if(c.getSharedPreferences("searchPref", Context.MODE_PRIVATE).getBoolean("notificationsEnabled", true)) {
            notificationManager.notify(0, builder.build());
        }
    }

    /**
     * Retreives the saveditem with the earliestdate
     * @return the earliestdate
     */
    private static SavedItem earlyDate(){
        Optional<SavedItem> i = itemList.stream().filter(e -> e.getStatus() > -1).filter(e -> new Date().before(e.getDateObject())).min((e, b) -> {
            if(e.getDateObject().before(b.getDateObject())){
                return -1;
            }
            return 1;
        });
        if(i.isPresent()){
            return i.get();
        }
        SavedItem a = new SavedItem();
        a.setDate("none scheduled.");
        return a;
    }

}
