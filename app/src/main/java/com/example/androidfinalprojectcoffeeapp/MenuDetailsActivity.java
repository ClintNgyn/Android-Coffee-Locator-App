package com.example.androidfinalprojectcoffeeapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuDetailsActivity extends AppCompatActivity {
  
  private MenuJsonObj currMenu;
  private ExtendedFloatingActionButton smallBtn, mediumBtn, largeBtn;
  private CircleImageView ivFoodImageId;
  private TextView tvNameId, tvServingSizeId, tvCaloriesId, tvTotalFatsId, tvSaturatedFatsId, tvTransFatsId, tvCholesterolId,
      tvSodiumId,
      tvTotalCarbohydrateId, tvFiberId, tvSugarId, tvProteinId, tvCaffeineId;
  private ImageView ivNavToolbar;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_details);
    
    //connect views
    connectViews();
    
    //get intent data from menuAdapter
    currMenu = (MenuJsonObj) getIntent().getSerializableExtra("currMenu");
    
    //load image and name
    tvNameId.setText(currMenu.getName());
    Picasso.get().load(currMenu.getImageUrl()).into(ivFoodImageId);
    
    //update nutrition facts
    updateNutritionFacts(1);
    
    //set fab onclick handlers
    setOnClickHandlers();
  }
  
  private void setOnClickHandlers() {
    smallBtn.setOnClickListener(view -> {
      updateNutritionFacts(1);
      setFabColors(smallBtn, mediumBtn, largeBtn);
    });
  
    mediumBtn.setOnClickListener(view -> {
      updateNutritionFacts(1.5);
      setFabColors(mediumBtn, smallBtn, largeBtn);
    });
  
    largeBtn.setOnClickListener(view -> {
      updateNutritionFacts(2);
      setFabColors(largeBtn, smallBtn, mediumBtn);
    });
  }
  
  /**
   * Sets the color of the buttons
   *
   * @param clicked    the button that is clicked
   * @param notClicked buttons that are not clicked
   */
  private void setFabColors(ExtendedFloatingActionButton clicked, ExtendedFloatingActionButton... notClicked) {
    clicked.setBackgroundColor(getResources().getColor(R.color.btnOrange));
    clicked.setRippleColorResource(R.color.menuColorAccent);
    
    for (ExtendedFloatingActionButton currFab : notClicked) {
      currFab.setBackgroundColor(getResources().getColor(R.color.menuColorPrimaryDark));
    }
    
  }
  
  /**
   * Connect views of the xml file
   */
  private void connectViews() {
    //fabs
    smallBtn = findViewById(R.id.smallBtn);
    mediumBtn = findViewById(R.id.mediumBtn);
    largeBtn = findViewById(R.id.largeBtn);
  
    //ImageViews
    ivFoodImageId = findViewById(R.id.ivFoodImageId);
    ivNavToolbar = findViewById(R.id.ivNavToolbar);
    ivNavToolbar.setOnClickListener(view -> super.onBackPressed());
  
    //TextViews
    tvNameId = findViewById(R.id.tvNameId);
    tvServingSizeId = findViewById(R.id.tvServingSizeId);
    tvCaloriesId = findViewById(R.id.tvCaloriesId);
    tvTotalFatsId = findViewById(R.id.tvTotalFatsId);
    tvSaturatedFatsId = findViewById(R.id.tvSaturatedFatsId);
    tvTransFatsId = findViewById(R.id.tvTransFatsId);
    tvCholesterolId = findViewById(R.id.tvTransFatsId);
    tvSodiumId = findViewById(R.id.tvSodiumId);
    tvTotalCarbohydrateId = findViewById(R.id.tvTotalCarbohydrateId);
    tvFiberId = findViewById(R.id.tvFiberId);
    tvSugarId = findViewById(R.id.tvSugarId);
    tvProteinId = findViewById(R.id.tvProteinId);
    tvCaffeineId = findViewById(R.id.tvCaffeineId);
  }
  
  /**
   * Update the UI to match the item's nutrition facts with size (small, medium, large)
   */
  private void updateNutritionFacts(double size) {
    tvServingSizeId.setText(Double.parseDouble(currMenu.getServingSizes()) * size + " fl oz");
    tvCaloriesId.setText(currMenu.getCalories() * size + "");
    tvTotalFatsId.setText(Double.parseDouble(currMenu.getTotalFats()) * size + " g");
    tvSaturatedFatsId.setText(Double.parseDouble(currMenu.getSaturatedFats()) * size + " g");
    tvTransFatsId.setText(Double.parseDouble(currMenu.getTransFats()) * size + " g");
    tvCholesterolId.setText(Double.parseDouble(currMenu.getCholesterol()) * size + " mg");
    tvSodiumId.setText(Double.parseDouble(currMenu.getSodium()) * size + " mg");
    tvTotalCarbohydrateId.setText(Double.parseDouble(currMenu.getTotalCarbohydrates()) * size + " g");
    tvFiberId.setText(Double.parseDouble(currMenu.getDiataryFiber()) * size + " g");
    tvSugarId.setText(Double.parseDouble(currMenu.getSugar()) * size + " g");
    tvProteinId.setText(Double.parseDouble(currMenu.getProtein()) * size + " g");
    tvCaffeineId.setText(Double.parseDouble(currMenu.getCaffeine()) * size + " mg");
  }
}