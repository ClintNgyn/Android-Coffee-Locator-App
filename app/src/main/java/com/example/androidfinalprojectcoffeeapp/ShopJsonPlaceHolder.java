package com.example.androidfinalprojectcoffeeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShopJsonPlaceHolder {
  //starbucks
  //@GET("d7fe1a8605c44549e63b")
  @GET("fc1a61ff3a7c7fb45993")
  Call<List<ShopJsonObj>> getShopJsonPlaceHolder();
}
