package com.example.androidfinalprojectcoffeeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BackgroundImageJsonPlaceHolder {
  @GET("random.json")
  Call<BackgroundImageJsonObj> getBackgroundImageJsonPlaceHolder();
}
