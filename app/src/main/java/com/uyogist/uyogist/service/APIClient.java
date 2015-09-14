package com.uyogist.uyogist.service;

import android.content.Context;

import com.uyogist.uyogist.R;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uyoGistService = retrofit.create(UyoGistService.class);
        return uyoGistService;
    }
}
