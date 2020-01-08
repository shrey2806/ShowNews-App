package com.example.shownews.Api;

import com.example.shownews.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top-headlines")
    Call<News>  fetchNews(
        @Query("country")String country, @Query("apiKey")String key
    );

    @GET("everything")
    Call<News> fetchSearchedNews(
        @Query("q")String keyword,
        @Query("language")String language,
        @Query("sortBy")String sortBy,
        @Query("apiKey")String apiKey
    );



}
