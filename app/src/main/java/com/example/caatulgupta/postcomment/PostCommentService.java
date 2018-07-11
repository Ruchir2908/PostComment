package com.example.caatulgupta.postcomment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PostCommentService {

    @GET("posts")
    Call<ArrayList<PostComment>> getPost();

}
