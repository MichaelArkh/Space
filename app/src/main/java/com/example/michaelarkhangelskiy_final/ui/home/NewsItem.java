package com.example.michaelarkhangelskiy_final.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class NewsItem {
    private int itemId;
    private String title;
    private String author;
    private Date published;
    private String summary;
    private Bitmap imgLink;
    private String articleLink;
    private boolean saved;

    public NewsItem() {
        itemId = -1;
    }

    public NewsItem(String title, String author, Date published, String summary, String imgLink, String articleLink, boolean saved){

        try {
            this.imgLink = new SetImageTask().execute(imgLink).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.articleLink = articleLink;
        this.title = title;
        this.author = author;
        this.published = published;
        this.summary = summary;
        this.saved = saved;
        itemId = -1;
    }

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

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Bitmap getImgLink() {
        return imgLink;
    }

    public void setImgLink(Bitmap imgLink) {
        this.imgLink = imgLink;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    private class SetImageTask extends AsyncTask<String, Void, Bitmap> {

        public SetImageTask() { }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imgurl = strings[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(imgurl).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }
    }
}
