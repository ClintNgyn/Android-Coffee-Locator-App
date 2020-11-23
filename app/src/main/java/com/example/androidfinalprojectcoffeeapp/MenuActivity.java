package com.example.androidfinalprojectcoffeeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {
  
  private RecyclerView rvMenuListId;
  
  private List<MenuJsonObj> menuJsonObjsList;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    
    //link rvMenuListId
    rvMenuListId = findViewById(R.id.rvMenuListId);
    
    //init menuItemClassList
    menuJsonObjsList = new ArrayList<>();
    
    //add menu items from json file to menuJsonObjsList
    addItemsToList();
    
  }
  
  /**
   * Fetch json file with retrofit and add items to menuJsonObjsList.
   */
  private void addItemsToList() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.npoint.io/")
                                              .addConverterFactory(GsonConverterFactory.create())
                                              .build();
    MenuJsonPlaceHolder menuJsonPlaceHolder = retrofit.create(MenuJsonPlaceHolder.class);
    Call<List<MenuJsonObj>> call = menuJsonPlaceHolder.getMenuJsonPlaceHolder();
    call.enqueue(new Callback<List<MenuJsonObj>>() {
      @Override
      public void onResponse(Call<List<MenuJsonObj>> call, Response<List<MenuJsonObj>> response) {
        if (!response.isSuccessful()) {
          return;
        }
        
        //storing menu items into menuItemClassList
        menuJsonObjsList = response.body();
        
        //display menu items in recycler view
        displayMenu();
        
      }
      
      @Override
      public void onFailure(Call<List<MenuJsonObj>> call, Throwable t) {
      
      }
    });
  }
  
  private void displayMenu() {
    MenuAdapter menuAdapter = new MenuAdapter(this, menuJsonObjsList);
    rvMenuListId.setLayoutManager(new GridLayoutManager(this, 3));
    rvMenuListId.setAdapter(menuAdapter);
  }
  
}