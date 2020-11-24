package com.example.androidfinalprojectcoffeeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShopDetailsActivity extends AppCompatActivity {

    private ShopJsonObj mShop;

    private ImageView ivShopImageId;
    private TextView tvTypeId, tvAddressId, tvPhoneId, tvIsHeartCheckedId;
    Button btnSeeMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        //get intent data from ShopAdapter Activity
        mShop = (ShopJsonObj) getIntent().getSerializableExtra("currShop");

        //link views
        ivShopImageId = findViewById(R.id.ivShopImageId);
        tvTypeId = findViewById(R.id.tvTypeId);
        tvAddressId = findViewById(R.id.tvAddressId);
        tvPhoneId = findViewById(R.id.tvPhoneId);
        tvPhoneId = findViewById(R.id.tvPhoneId);
        tvIsHeartCheckedId = findViewById(R.id.tvIsHeartCheckedId);
        btnSeeMenuId = findViewById(R.id.btnSeeMenuId);

        //display shop's detail
        displayShopsData();
    }

    /**
     * Display shop's details such as address, phone number etc.
     */
    private void displayShopsData() {
        //set image
        Picasso.get().load(mShop.getImg()).into(ivShopImageId);

        //set type
        tvTypeId.setText(mShop.getType());

        //set address
        tvAddressId.setText(mShop.getAddress());

        //set phone number
        tvPhoneId.setText(mShop.getPhoneNumber());

        //set tvIsHeartCheckedId
        tvIsHeartCheckedId.setText(mShop.isHeartChecked() + "");

        //set see menu onclick handler
        btnSeeMenuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: change menu items base on shop's type i.e. Starbucks, Second Cup etc.

                // open menu activity
                Intent i = new Intent(ShopDetailsActivity.this, MenuActivity.class);
                i.putExtra("mShopType", mShop.getType());
                ShopDetailsActivity.this.startActivity(i);
            }
        });


    }
}