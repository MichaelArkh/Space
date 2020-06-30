package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardViewModel extends ViewModel {
    public static String items;
    public static List<RocketItem> itemList;

    public DashboardViewModel() { }

    public static void getNewsItems() throws Exception {
        ArrayList<RocketItem> ret = new ArrayList<RocketItem>();
        JSONObject values = new JSONObject(items);

        JSONArray main = values.getJSONArray("launches");
        for(int i = 0; i < 10; i++){
            JSONObject one = main.getJSONObject(i);
            //Location Data
            JSONObject locationdata = one.getJSONObject("location");
            String location = locationdata.getString("name");
            Double lat = locationdata.getJSONArray("pads").getJSONObject(0).getDouble("latitude");
            Double lng = locationdata.getJSONArray("pads").getJSONObject(0).getDouble("longitude");
            LatLng latLng = new LatLng(lat, lng);

            //Rocket Data
            JSONObject rocketdata = one.getJSONObject("rocket");
            String name = rocketdata.getString("name");
            String image = rocketdata.getString("imageURL");

            //Summary
            JSONObject summarydata = one.getJSONArray("missions").getJSONObject(0);
            String summary = summarydata.getString("description");


            //Date
            DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss z", Locale.ENGLISH);
            Date date = format.parse(one.getString("windowstart"));

            ret.add(new RocketItem(date, name, summary, image, location, latLng, null));
        }

        itemList = ret;

    }
}