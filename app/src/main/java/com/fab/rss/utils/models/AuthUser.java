package com.fab.rss.utils.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Author:       Fab
 * Email:        ffontaine@thingsaremoving.com
 * Created:      1/28/17
 */

@SuppressWarnings("UnusedDeclaration")
public class AuthUser implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("rss")
    @Expose
    private String[] rss;

    public AuthUser(String id, String name, String token, String[] rss) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.rss = rss;
    }

    public AuthUser(String id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getRss() {
        return rss;
    }

    public void setRss(String[] rss) {
        this.rss = rss;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", rss=" + Arrays.toString(rss) +
                '}';
    }
}
