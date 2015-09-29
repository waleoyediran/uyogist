package com.uyogist.uyogist.service;

import com.uyogist.uyogist.model.Gist;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * API Service
 * Created by oyewale on 9/13/15.
 */
public interface UyoGistService {

    @GET("/api/gist")
    void getGists(Callback<List<Gist>> callback);

    @Multipart
    @POST("/api/gist")
    void postGist(@Part("img")TypedFile photo, @Part("nick")String author, @Part("gist")String gist, Callback<Gist> callback);
}
