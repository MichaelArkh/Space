package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * The type Rocket item.
 */
public class RocketItem {
    private int itemId;
    private Date startTime;
    private String name;
    private String summary;
    private String image;
    private String location;
    private LatLng cords;
    private String rocket;
    private String wikiURL;
    private int status;
    private boolean saved;

    /**
     * Instantiates a new Rocket item.
     */
    public RocketItem(){
        itemId = -1;
    }

    /**
     * Instantiates a new Rocket item.
     *
     * @param startTime the start time
     * @param name      the name
     * @param summary   the summary
     * @param image     the image
     * @param location  the location
     * @param cords     the cords
     * @param rocket    the rocket
     * @param wikiURL   the wiki url
     * @param status    the status
     */
    public RocketItem(Date startTime, String name, String summary, String image, String location, LatLng cords, String rocket, String wikiURL, int status) {
        this.startTime = startTime;
        this.name = name;
        this.summary = summary;
        this.wikiURL = wikiURL;

        this.image = image;
        this.location = location;
        this.cords = cords;
        this.rocket = rocket;
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets wiki url.
     *
     * @return the wiki url
     */
    public String getWikiURL() {
        return wikiURL;
    }

    /**
     * Sets wiki url.
     *
     * @param wikiURL the wiki url
     */
    public void setWikiURL(String wikiURL) {
        this.wikiURL = wikiURL;
    }

    /**
     * Gets item id.
     *
     * @return the item id
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets item id.
     *
     * @param itemId the item id
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets summary.
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets summary.
     *
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets cords.
     *
     * @return the cords
     */
    public LatLng getCords() {
        return cords;
    }

    /**
     * Sets cords.
     *
     * @param cords the cords
     */
    public void setCords(LatLng cords) {
        this.cords = cords;
    }

    /**
     * Is saved boolean.
     *
     * @return the boolean
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Sets saved.
     *
     * @param saved the saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Gets rocket.
     *
     * @return the rocket
     */
    public String getRocket() {
        return rocket;
    }

    /**
     * Sets rocket.
     *
     * @param rocket the rocket
     */
    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    /**
     * Used for converting the image link to a bitmap.
     * @deprecated
     */
    private class SetImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * Instantiates a new Set image task.
         */
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

    /**
     * Converts this object to a string
     * @return the string
     */
    public String toString(){
        return this.name;
    }
}
