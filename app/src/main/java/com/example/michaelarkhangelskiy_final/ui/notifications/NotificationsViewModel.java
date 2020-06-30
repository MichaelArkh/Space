package com.example.michaelarkhangelskiy_final.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificationsViewModel extends ViewModel {
    public static String peopleResult;
    public static String locationResult;
    public static ISS information;

    public NotificationsViewModel() { }

    public static void setInformation() throws Exception {
        JSONArray peoplea= new JSONObject(peopleResult).getJSONArray("people");
        String[] people = new String[peoplea.length()];
        for(int i = 0; i < peoplea.length(); i++){
            people[i] = peoplea.getJSONObject(i).getString("name");
        }
        JSONObject location = new JSONObject(locationResult).getJSONObject("iss_position");
        double lat = location.getDouble("latitude");
        double lng = location.getDouble("longitude");
        LatLng pos = new LatLng(lat, lng);
        information = new ISS(people, pos);
    }
}