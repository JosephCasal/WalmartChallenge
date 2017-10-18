package com.example.joseph.walmartchallenge.remote;

import com.example.joseph.walmartchallenge.model.SearchResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by joseph on 10/15/17.
 */

public interface WalmartService {

    @GET("/v1/search")
    Observable<SearchResult> search(@Query("query") String query, @Query("format") String format, @Query("apikey") String apikey);

    @GET("/v1/search")
    Observable<SearchResult> searchMore(@Query("query") String query, @Query("start") String start, @Query("format") String format, @Query("apikey") String apikey);
}
