package com.example.androidfinalprojectcoffeeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuJsonPlaceHolder {
  @GET("9d23d97dac5ee2c83203")
  Call<List<MenuJsonObj>> getMenuJsonPlaceHolder();
}
