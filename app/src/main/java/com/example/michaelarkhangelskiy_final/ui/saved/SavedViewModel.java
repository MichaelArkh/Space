package com.example.michaelarkhangelskiy_final.ui.saved;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.example.michaelarkhangelskiy_final.ui.dashboard.RocketItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;

import java.util.ArrayList;

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

}
