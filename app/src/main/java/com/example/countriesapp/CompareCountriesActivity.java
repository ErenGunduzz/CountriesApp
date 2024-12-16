package com.example.countriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CompareCountriesActivity extends AppCompatActivity implements CountryFetcher.CountryFetchListener {

    Spinner spinnerCountry1, spinnerCountry2;
    Button btnCmp;
    ArrayList<String> countryList = new ArrayList<>(); // Ülke listesi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_countries);

        spinnerCountry1 = findViewById(R.id.spinner_country1);
        spinnerCountry2 = findViewById(R.id.spinner_country2);
        btnCmp = findViewById(R.id.btn_cmp);

        CountryFetcher.fetchCountries(this);

        btnCmp.setOnClickListener(view -> {
            String country1 = (String) spinnerCountry1.getSelectedItem();
            String country2 = (String) spinnerCountry2.getSelectedItem();

            /*
            if (country1 != null && country2 != null) {
                Intent intent = new Intent(this, CompareResultActivity.class);
                intent.putExtra("country1", country1);
                intent.putExtra("country2", country2);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select two different countries", Toast.LENGTH_LONG).show();
            }

             */
        });
    }

    @Override
    public void onCountriesFetched(List<String> countries) {
        countryList.clear();
        countryList.addAll(countries);
        setupSpinners(); // Spinnerları ayarlayan metod
    }

    @Override
    public void onFetchFailed() {
        Toast.makeText(this, "Failed to fetch country data.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry1.setAdapter(adapter);
        spinnerCountry2.setAdapter(adapter);
    }
}
