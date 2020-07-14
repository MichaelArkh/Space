package com.example.michaelarkhangelskiy_final.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * The type News item.
 */
public class NewsItem {
    private int itemId;
    private String title;
    private String author;
    private Date published;
    private String summary;
    private String imgLink;
    private String articleLink;
    private boolean saved;

    /**
     * Instantiates a new News item.
     */
    public NewsItem() {
        itemId = -1;
    }

    /**
     * Instantiates a new News item.
     *
     * @param title       the title
     * @param author      the author
     * @param published   the published
     * @param summary     the summary
     * @param imgLink     the img link
     * @param articleLink the article link
     * @param saved       the saved
     */
    public NewsItem(String title, String author, Date published, String summary, String imgLink, String articleLink, boolean saved){

        this.imgLink =  imgLink;
        this.articleLink = articleLink;
        this.title = title;
        this.author = author;
        this.published = published;
        this.summary = summary;
        this.saved = saved;
        itemId = -1;
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
     * Gets published.
     *
     * @return the published
     */
    public Date getPublished() {
        return published;
    }

    /**
     * Sets published.
     *
     * @param published the published
     */
    public void setPublished(Date published) {
        this.published = published;
    }

    /**
     * Gets img link.
     *
     * @return the img link
     */
    public String getImgLink() {
        return imgLink;
    }

    /**
     * Sets img link.
     *
     * @param imgLink the img link
     */
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    /**
     * Gets article link.
     *
     * @return the article link
     */
    public String getArticleLink() {
        return articleLink;
    }

    /**
     * Sets article link.
     *
     * @param articleLink the article link
     */
    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
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
     * Coverts an image url to a bitmap
     * @deprecated
     */
    private class SetImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * Instantiates a new Set image task.
         */
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
