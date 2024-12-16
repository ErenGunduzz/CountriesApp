package com.example.countriesapp;

import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CountryFetcher extends AppCompatActivity {

    public interface CountryFetchListener {
        void onCountriesFetched(List<String> countries);
        void onFetchFailed();
    }

    public static void fetchCountries(CountryFetchListener listener) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                try {
                    String url = "https://en.wikipedia.org/wiki/List_of_countries_by_population_(United_Nations)";
                    Document document = Jsoup.connect(url).get();
                    List<String> countries = new ArrayList<>();
                    Element table = document.select("table.wikitable").first();
                    Elements rows = table.select("tr");

                    for (int i = 2; i <= 21; i++) {
                        Element row = rows.get(i);
                        Elements cells = row.select("td");
                        String country = cells.get(0).text().split("\\[", 2)[0].trim();
                        countries.add(country);
                    }
                    return countries;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<String> countries) {
                if (countries != null && !countries.isEmpty()) {
                    listener.onCountriesFetched(countries);
                } else {
                    listener.onFetchFailed();
                }
            }
        }.execute();
    }
}
