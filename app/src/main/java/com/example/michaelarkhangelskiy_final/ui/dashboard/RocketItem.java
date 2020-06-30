package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RocketItem {
    private int itemId;
    private Date startTime;
    private String name;
    private String summary;
    private Bitmap image;
    private String location;
    private LatLng cords;
    private String rocket;
    private boolean saved;

    public RocketItem(){
        itemId = -1;
    }
    public RocketItem(Date startTime, String name, String summary, String image, String location, LatLng cords, String rocket) {
        this.startTime = startTime;
        this.name = name;
        this.summary = summary;
        try {
            this.image = new SetImageTask().execute(image).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.location = location;
        this.cords = cords;
        this.rocket = rocket;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LatLng getCords() {
        return cords;
    }

    public void setCords(LatLng cords) {
        this.cords = cords;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getRocket() {
        return rocket;
    }

    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    private class SetImageTask extends AsyncTask<String, Void, Bitmap> {

        public SetImageTask() {
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imgurl = strings[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(imgurl).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }
    }
    public String toString(){
        return this.name;
    }
}
