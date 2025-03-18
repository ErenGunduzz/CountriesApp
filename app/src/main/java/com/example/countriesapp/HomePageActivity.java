package com.example.countriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    Button btnCountryList, btnFavorites, btnCompare;

    // SQLiteOpenHelper controller helper class for SQLite database operations
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnCountryList = findViewById(R.id.btn_country_list);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnCompare = findViewById(R.id.btn_compare);

        // Button click list listeners
        // Explicit intent to CountryListActivity class
        btnCountryList.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CountryListActivity.class);
            startActivity(intent);
        });

        // Explicit intent to FavoritesActivity class
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, FavoritesActivity.class);
            intent.putStringArrayListExtra("favorites", new ArrayList<>(databaseHelper.getAllFavorites()));
            startActivity(intent);
        });

        // Explicit intent to CompareCountriesActivity class
        btnCompare.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, CompareCountriesActivity.class);
            startActivity(intent);
        });
    }

}
