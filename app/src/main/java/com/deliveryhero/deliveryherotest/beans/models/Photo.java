package com.deliveryhero.deliveryherotest.beans.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MACB105 on 11/06/16.
 */
public class Photo implements Serializable {

    // Properties

    @SerializedName("albumId")
    public Integer albumId;


    @SerializedName("id")
    public Integer id;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    @SerializedName("thumbnailUrl")
    public String thumbnailUrl;


}
