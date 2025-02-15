package com.example.countriesapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    RecyclerView favoritesRecyclerView;
    FavoritesAdapter adapter;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        databaseHelper = new DatabaseHelper(this);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = findViewById(R.id.favoritesToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Favori ülkeleri alıp RecyclerView'e yükleme
        List<String> favoriteCountries = databaseHelper.getAllFavorites();
        if (favoriteCountries != null && !favoriteCountries.isEmpty()) {
            adapter = new FavoritesAdapter(favoriteCountries);
            if (adapter != null) {
                favoritesRecyclerView.setAdapter(adapter);
            } else {
                // Adapter oluşturulamadıysa hata mesajı
                Toast.makeText(this, "Adapter could not created!",Toast.LENGTH_SHORT).show();
            }
        } else {
            //Henüz bir favori yoksa ekranda mesajla gösterilir
            Toast.makeText(this, "There is no favorite yet!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Önceki ekrana yani HomePageActivity'a döner
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Favori ülkeler değiştiğinde, adapter'ı güncelle
    public void updateFavoritesList() {
        super.onResume();
        List<String> favoriteCountries = databaseHelper.getAllFavorites();
        if (adapter != null) {
            adapter.updateFavorites(favoriteCountries);
        }
    }


}
