package com.example.countriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<String> favoriteCountries;//Favori ülkeler listesi

    public FavoritesAdapter(List<String> favoriteCountries) {
        this.favoriteCountries = favoriteCountries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String countryName = favoriteCountries.get(position);
        holder.textView.setText(countryName);
    }

    @Override
    public int getItemCount() {
        return favoriteCountries.size();
    }

    // Favori ülkeler listesi değiştiğinde adapter'ı güncelle
    public void updateFavorites(List<String> updatedFavorites) {
        this.favoriteCountries = updatedFavorites;
        notifyDataSetChanged();  // RecyclerView'i güncelle
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
