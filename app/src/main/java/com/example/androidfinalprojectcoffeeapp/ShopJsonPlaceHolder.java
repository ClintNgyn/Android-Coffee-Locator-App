package com.example.androidfinalprojectcoffeeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShopJsonPlaceHolder {
    @GET("d7fe1a8605c44549e63b")
    Call<List<ShopJsonObj>> getShopJsonPlaceHolder();
}
