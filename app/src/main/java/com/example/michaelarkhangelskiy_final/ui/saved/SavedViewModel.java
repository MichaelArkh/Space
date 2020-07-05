package com.example.michaelarkhangelskiy_final.ui.saved;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModel;

import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.example.michaelarkhangelskiy_final.ui.dashboard.RocketItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class SavedViewModel extends ViewModel {
    public static ArrayList<SavedItem> itemList = new ArrayList<SavedItem>();

    public SavedViewModel() {

    }

    public static void loadFromDB(Context context){
        itemList.clear();
        try {
            SavedItemDataSource ds = new SavedItemDataSource(context);
            ds.open();
            itemList.addAll(ds.getItems());
            ds.close();
        } catch (Exception e) {}
    }

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

    public static void removeItem(SavedItem removed){
        itemList.remove(removed);
    }

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
