package com.example.countriesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CountryList extends AppCompatActivity {

    RecyclerView recyclerView;
    CountryAdapter adapter;
    ArrayList<String> countryList = new ArrayList<>(); // Ülkeleri saklayacak liste

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the "Back" button on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CountryAdapter(this, countryList);
        recyclerView.setAdapter(adapter);

        // Veriyi çekme işlemi
        new FetchData().execute();
    }

    // Handle the back button press on the toolbar
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Go back to the previous screen (HomePageActivity)
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Wikipedia'dan veriyi çekme işlemi
    private class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://en.wikipedia.org/wiki/List_of_countries_by_population_(United_Nations)";
                Document document = Jsoup.connect(url).get();

                // Tabloyu bulma
                Element table = document.select("table.wikitable").first();
                Elements rows = table.select("tr");

                // İlk 20 ülkeyi alma (Tablodaki 1. satır başlık olduğu için 2'den başlıyoruz)
                for (int i = 2; i <= 21; i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    // Ülke ismini çekme
                    String country = cells.get(0).text().split("\\[", 2)[0].trim();
                    countryList.add(country);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (countryList.isEmpty()) {
                Toast.makeText(CountryList.this, "Veri çekilemedi", Toast.LENGTH_SHORT).show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
