package com.example.searchimage;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://api.imgur.com";

    @GET("/3/gallery/search/{UNIQUE_ID}")
    Call<JsonElement> getData(@Path("UNIQUE_ID") String number, @Query("q") String search, @Header("Authorization") String auth);

}
