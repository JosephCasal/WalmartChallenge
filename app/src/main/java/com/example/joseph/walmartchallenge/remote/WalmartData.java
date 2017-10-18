package com.example.joseph.walmartchallenge.remote;

import com.example.joseph.walmartchallenge.model.SearchResult;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joseph on 10/15/17.
 */

public class WalmartData {

    public static final String BASE_URL = "http://api.walmartlabs.com";
    public static final String API_KEY = "c7tu24xvgd7kuecwpt2z3gak";

    public static Retrofit create(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;

    }

    public static Observable<SearchResult> search(String search){
        Retrofit retrofit = create();
        WalmartService walmartService = retrofit.create(WalmartService.class);
        return walmartService.search(search, "json", API_KEY);
    }

    public static Observable<SearchResult> searchMore(String search, int start) {
        Retrofit retrofit = create();
        WalmartService walmartService = retrofit.create(WalmartService.class);
        return walmartService.searchMore(search, Integer.toString(start), "json", API_KEY);
    }
}
