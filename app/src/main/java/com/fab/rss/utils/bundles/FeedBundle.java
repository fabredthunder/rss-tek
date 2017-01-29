package com.fab.rss.utils.bundles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/29/17
 */

@SuppressWarnings("UnusedDeclaration")
public class FeedBundle implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("comment")
    @Expose
    private String comment;

    public FeedBundle(String title, String url, String comment) {
        this.title = title;
        this.url = url;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "FeedBundle{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
