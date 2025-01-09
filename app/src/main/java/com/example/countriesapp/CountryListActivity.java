package com.example.countriesapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CountryListActivity extends AppCompatActivity implements CountryFetcher.CountryFetchListener {

    RecyclerView recyclerView;
    CountryListAdapter adapter;
    ArrayList<String> countryList = new ArrayList<>(); // Ülkeleri saklayacak liste

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toolbar'daki back buttonunu aktif etme
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Connecting adapter with recyclerView
        adapter = new CountryListAdapter(this, countryList, null);
        recyclerView.setAdapter(adapter);

        // Veriyi çekme işlemi
        CountryFetcher.fetchCountries(this);
    }

    @Override
    public void onCountriesFetched(List<String> countries) {
        countryList.clear();
        countryList.addAll(countries);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFetchFailed() {
        Toast.makeText(this, "Failed to fetch country data.", Toast.LENGTH_SHORT).show();
    }

    //Back buttonuna basılma işlevini halleder
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Bir önceki ekrana yani HomePageActivity'e geri döner
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
