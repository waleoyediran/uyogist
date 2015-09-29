package com.uyogist.uyogist.service;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.uyogist.uyogist.R;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * API Client Class
 * Created by oyewale on 9/13/15.
 */
public class APIClient {

    private static UyoGistService uyoGistService;

    /**
     * Get instance of UyoGist Service
     * @return service
     */
    public static UyoGistService getUyoGistAPIService(Context context){
        if (uyoGistService != null){
            return uyoGistService;
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.base_url))
                .setConverter(new GsonConverter(gson))
                .build();

        uyoGistService = restAdapter.create(UyoGistService.class);

        return uyoGistService;
    }
}
