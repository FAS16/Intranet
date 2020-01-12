package com.fahadali.intranet.model;


import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Token {
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String EXPIRES_IN = "EXPIRES_IN";
    public static final String USERNAME = "USERNAME";
    public static final String EXPIRES = "EXPIRES";

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("userName")
    private String username;

    @SerializedName(".expires")
    private String expires;

    public String getBearerToken() {
        return "Bearer " + accessToken;
    }


}
