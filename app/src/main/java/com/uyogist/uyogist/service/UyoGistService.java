package com.uyogist.uyogist.service;

import com.uyogist.uyogist.Gist;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * API Service
 * Created by oyewale on 9/13/15.
 */
public interface UyoGistService {

    @GET("/api/gist")
    Call<List<Gist>> getGists();
}
