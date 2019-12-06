package com.fahadali.intranet.other;

import com.fahadali.intranet.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlacerHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();
}
