package com.example.shownews.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = "https://newsapi.org/v2/";

    private static Retrofit retrofit;

    public static Retrofit getApiClient(){

        if(retrofit==null){

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return  retrofit;
    }

    

}
