package com.example.countriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    ArrayList<String> countryList = new ArrayList<>();
    Button btnCountryList, btnFavorites, btnCompare;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnCountryList = findViewById(R.id.btn_country_list);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnCompare = findViewById(R.id.btn_compare);

        // Buton tıklama olayları
        btnCountryList.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CountryListActivity.class);
            startActivity(intent);
        });

        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, FavoritesActivity.class);
            intent.putStringArrayListExtra("favorites", new ArrayList<>(databaseHelper.getAllFavorites()));
            startActivity(intent);
        });


        btnCompare.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CompareCountriesActivity.class);
            startActivity(intent);
        });
    }

}
