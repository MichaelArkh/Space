package com.example.michaelarkhangelskiy_final.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends ViewModel {
    public static String items;
    public static List<NewsItem> itemList;

    public HomeViewModel() { }

    public static void getNewsItems() throws Exception {
        ArrayList<NewsItem> ret = new ArrayList<NewsItem>();
            JSONObject values = new JSONObject(items);
            JSONArray main = values.getJSONArray("articles");
            for(int i = 0; i < 10; i++){
                JSONObject one = main.getJSONObject(i);
                String author = one.getString("author");
                String title = one.getString("title");
                String description = one.getString("description");
                String url = one.getString("url");
                String imgURL = one.getString("urlToImage");
                Date date = new DateTime(one.getString("publishedAt")).toDate();
                ret.add(new NewsItem(title, author, date, description, imgURL, url, false));
            }

        itemList = ret;
    }
}