package com.deliveryhero.deliveryherotest.beans.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MACB105 on 11/06/16.
 */
public class Album implements Serializable {

    // Properties


    @SerializedName("id")
    public Integer id;

    @SerializedName("title")
    public String title;


}
