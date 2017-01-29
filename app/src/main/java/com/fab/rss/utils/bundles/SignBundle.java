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
public class SignBundle implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("password")
    @Expose
    private String pass;

    public SignBundle(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "SignBundle{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
