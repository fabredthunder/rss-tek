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
public class BaseResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public BaseResponse(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success='" + success + '\'' +
                '}';
    }
}
