package com.example.michaelarkhangelskiy_final.database;

import android.graphics.Bitmap;

import com.example.michaelarkhangelskiy_final.ui.dashboard.RocketItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;

import java.util.Date;

public class SavedItem {
    /**
     * (_id integer primary key autoincrement, "
     *                     + "title text, author text, "
     *                     + "date text, summary text, "
     *                     + "image blob, click_link text);";
     */
    private int itemId;
    private String title;
    private String author;
    private String date;
    private String summary;
    private Bitmap image;
    private String clicked;

    public SavedItem() {itemId = -1;}

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

    public static SavedItem convert(NewsItem i){
        SavedItem a = new SavedItem();
        a.setDate(i.getPublished().toString());
        a.setClicked(i.getArticleLink());
        a.setImage(i.getImgLink());
        a.setSummary(i.getSummary());
        a.setAuthor(i.getAuthor().replaceAll("'", ""));
        a.setTitle(i.getTitle());
        return a;
    }
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
        return a;
    }
}
