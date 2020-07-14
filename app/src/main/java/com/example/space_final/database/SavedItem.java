package com.example.space_final.database;

import android.util.Log;

import com.example.space_final.ui.dashboard.RocketItem;
import com.example.space_final.ui.home.NewsItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The class for the savedItems.
 */
public class SavedItem {
    private int itemId;
    private String title;
    private String author;
    private String date;
    private String summary;
    private String image;
    private String clicked;
    private int status;

    /**
     * Instantiates a new Saved item.
     */
    public SavedItem() {itemId = -1;}

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
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets date object.
     *
     * @return the date object
     */
    public Date getDateObject() {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            date = format.parse(getDate());
            Log.e("test", date.toString());
        } catch (Exception e) {}
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
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
     * Gets clicked.
     *
     * @return the clicked
     */
    public String getClicked() {
        return clicked;
    }

    /**
     * Sets clicked.
     *
     * @param clicked the clicked
     */
    public void setClicked(String clicked) {
        this.clicked = clicked;
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
     * Converts a newsitem to savedItem
     *
     * @param i the newsitem to convert
     * @return the savedItem
     */
    public static SavedItem convert(NewsItem i){
        SavedItem a = new SavedItem();
        a.setDate(i.getPublished().toString());
        a.setClicked(i.getArticleLink());
        a.setImage(i.getImgLink());
        a.setSummary(i.getSummary());
        a.setAuthor(i.getAuthor().replaceAll("'", ""));
        a.setTitle(i.getTitle());
        a.setStatus(-1);
        return a;
    }

    /**
     * Convert a rocketItem to savedItem
     *
     * @param i the rocket item
     * @return the saved item
     */
    public static SavedItem convert(RocketItem i){
        SavedItem a = new SavedItem();
        a.setDate(i.getStartTime().toString());
        if(i.getWikiURL().equals("")){
            a.setClicked(null);
        } else {
            a.setClicked(i.getWikiURL());
        }
        a.setImage(i.getImage());
        a.setSummary(i.getSummary());
        a.setAuthor(i.getLocation().replaceAll("'", ""));
        a.setTitle(i.getName());
        a.setStatus(i.getStatus());
        return a;
    }
}
