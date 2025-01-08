package com.example.countriesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryDetailActivity extends AppCompatActivity {

    TextView countryNameTextView, countryDetailsTextView;
    ImageView flagImageView; // To display flag
    ImageView countryPhotoImageView; // For country image
    Button btnOpenInMaps; // Button to open country in the map
    String countryName;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        // Define toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Back button to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryDetailsTextView = findViewById(R.id.countryDetailsTextView);
        flagImageView = findViewById(R.id.flagImageView);
        countryPhotoImageView = findViewById(R.id.countryPhotoImageView);
        btnOpenInMaps = findViewById(R.id.btn_open_in_maps);

        // Take country name from intent
        countryName = getIntent().getStringExtra("country_name");
        countryNameTextView.setText(countryName);

        // Fetch details from wikipedia
        new FetchCountryDetails().execute();

        fetchPopulationData(countryName);

        fetchCountryPhotos(countryName);

        // Haritada açma butonu işlevi
        btnOpenInMaps.setOnClickListener(v -> openCountryInMaps(countryName));
    }

    private void openCountryInMaps(String countryName) {
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(countryName));
        Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (Exception e){
            // Redirecting to Play store
            Toast.makeText(this, "Google Maps not installed. Redirecting to Play Store.", Toast.LENGTH_SHORT).show();
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
            startActivity(playStoreIntent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            // Favorilere ekleme işlemi
            addToFavorites();
            return true;
        } else if (id == android.R.id.home) {
            // Geri butonuna basıldığında aktiviteyi kapat
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites() {
        if (databaseHelper.isFavorite(countryName)) {
            databaseHelper.removeFavorite(countryName);
            Toast.makeText(this, countryName + " removed from favorites!", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.addFavorite(countryName);
            Toast.makeText(this, countryName + " added to favorites!", Toast.LENGTH_SHORT).show();
        }
        invalidateOptionsMenu(); // Menü simgesini güncelle
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        if (databaseHelper.isFavorite(countryName)) {
            favoriteItem.setIcon(R.drawable.baseline_star_24); // Dolu yıldız
        } else {
            favoriteItem.setIcon(R.drawable.empty_star); // Boş yıldız
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_country_detail, menu);
        return true;
    }

    // Wikipedia'dan başkent, dil ve para birimi çekme
    private class FetchCountryDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = "https://en.wikipedia.org/wiki/" + countryName.replace(" ", "_");
                Document doc = Jsoup.connect(url).get();

                Element infoBox = doc.select("table.infobox").first();
                if (infoBox != null) {
                    StringBuilder details = new StringBuilder();
                    details.append("Capital: ").append(getInfoBoxValue(infoBox, "Capital")).append("\n");
                    details.append("Language: ").append(getInfoBoxValue(infoBox, "Official language")).append("\n");
                    details.append("Currency: ").append(getInfoBoxValue(infoBox, "Currency")).append("\n");

                    return details.toString();
                } else {
                    return "Wikipedia details not available.";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "Error fetching Wikipedia details.";
            }
        }

        @Override
        protected void onPostExecute(String details) {
            super.onPostExecute(details);
            countryDetailsTextView.setText(details);
        }
    }

    // Wikipedia Infobox'tan veri al
    private String getInfoBoxValue(Element infoBox, String key) {
        Element row = infoBox.select("tr:contains(" + key + ")").first();
        if (row != null) {
            String text = row.select("td").text();

            // Köşeli parantezleri ve içindeki referansları temizle
            text = text.replaceAll("\\[.*?\\]", "");

            if (!key.equals("Population")) {
                // Sayılardan ve koordinatlardan önceki kısmı al
                text = text.split("\\d", 2)[0].trim();
            }

            return text.trim();
        }
        return "Not available";
    }

    // REST API ile nüfus bilgisini çek
    private void fetchPopulationData(String country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CountryDetailAPI service = retrofit.create(CountryDetailAPI.class);
        Call<List<CountryResponse>> call = service.getCountryData(country);

        call.enqueue(new Callback<List<CountryResponse>>() {
            @Override
            public void onResponse(Call<List<CountryResponse>> call, Response<List<CountryResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    long population = response.body().get(0).population;

                    // Nüfusu 3 basamaklı gruplara ayır
                    String formattedPopulation = formatPopulation(population);
                    String flagUrl = response.body().get(0).flags.png;

                    // Mevcut detayların sonuna nüfus bilgisini ekle
                    String currentDetails = countryDetailsTextView.getText().toString();
                    String updatedDetails = currentDetails + "Population: " + formattedPopulation + "\n";

                    countryDetailsTextView.setText(updatedDetails);

                    // Bayrağı ImageView'e yükle
                    Glide.with(CountryDetailActivity.this)
                            .load(flagUrl)
                            //.placeholder(R.drawable.placeholder) // Yüklenirken gösterilecek resim
                            //.error(R.drawable.error_image) // Hata olursa gösterilecek resim
                            .into(flagImageView);
                }
            }

            @Override
            public void onFailure(Call<List<CountryResponse>> call, Throwable t) {
                String currentDetails = countryDetailsTextView.getText().toString();
                countryDetailsTextView.setText(currentDetails + "Population: Not available (API error)\n");
            }
        });
    }

    // Nüfusu 3 basamaklı gruplara ayır
    private String formatPopulation(long population) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        return numberFormat.format(population);
    }

    private void fetchCountryPhotos(String country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pixabay.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PixabayApiService service = retrofit.create(PixabayApiService.class);
        String apiKey = "47563032-a261308df24fc9b96a78ff301"; // Pixabay API key

        Call<PixabayResponse> call = service.searchPhotos(apiKey, country, "photo");

        call.enqueue(new Callback<PixabayResponse>() {
            @Override
            public void onResponse(Call<PixabayResponse> call, Response<PixabayResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().hits.isEmpty()) {
                    String photoUrl = response.body().hits.get(0).imageUrl;

                    // Görseli ImageView'e yükle
                    Glide.with(CountryDetailActivity.this)
                            .load(photoUrl)
                            //.placeholder(R.drawable.placeholder) // Yüklenirken gösterilecek görsel
                            //.error(R.drawable.error_image) // Hata durumunda gösterilecek görsel
                            .into(countryPhotoImageView);
                }
            }

            @Override
            public void onFailure(Call<PixabayResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
