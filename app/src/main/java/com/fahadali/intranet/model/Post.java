package com.fahadali.intranet.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Post {

    private int userId;
    private int id;
    private String title;
    @SerializedName("body")
    private String text;

}
