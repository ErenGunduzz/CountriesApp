package com.example.countriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class CompareCountriesActivity extends AppCompatActivity implements CountryFetcher.CountryFetchListener {

    Spinner spinnerCountry1, spinnerCountry2;
    Button btnCmp;
    ArrayList<String> countryList = new ArrayList<>(); // countries added to this.
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_countries);

        toolbar = findViewById(R.id.toolbarCompare);
        setSupportActionBar(toolbar);

        //"Back" buttonunu aktif etme
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinnerCountry1 = findViewById(R.id.spinner_country1);
        spinnerCountry2 = findViewById(R.id.spinner_country2);
        btnCmp = findViewById(R.id.btn_cmp);

        //Ülkeleri çekme
        CountryFetcher.fetchCountries(this);

        //Karşılaştırılacak ülkelerin sırayla spinnerlar ile seçilmesi
        btnCmp.setOnClickListener(view -> {
            String country1 = (String) spinnerCountry1.getSelectedItem();
            String country2 = (String) spinnerCountry2.getSelectedItem();
            if (country1 != null && country2 != null && !country1.equals(country2)) {
                CompareResultFragment fragment = new CompareResultFragment();
                Bundle args = new Bundle();
                args.putString("country1", country1);
                args.putString("country2", country2);
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(this, "Please select two different countries", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCountriesFetched(List<String> countries) {
        countryList.clear();
        countryList.addAll(countries);
        setupSpinners(); // Spinnerları ayarlayan method
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

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
