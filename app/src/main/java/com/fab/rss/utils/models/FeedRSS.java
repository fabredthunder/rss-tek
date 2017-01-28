package com.fab.rss.utils.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
public class FeedRSS implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("titleImage")
    @Expose
    private String titleImage;

    @SerializedName("urlImage")
    @Expose
    private String urlImage;

    @SerializedName("feedrss")
    @Expose
    private RSS[] feed;

    public FeedRSS(String title, String description, String author, String url, String titleImage, String urlImage, RSS[] feed) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.url = url;
        this.titleImage = titleImage;
        this.urlImage = urlImage;
        this.feed = feed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public RSS[] getFeed() {
        return feed;
    }

    public void setFeed(RSS[] feed) {
        this.feed = feed;
    }
}
