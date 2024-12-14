package com.example.countriesapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class FavoriteCountries {
    private static Set<String> favoriteCountries = new HashSet<>();

    public static boolean isFavorite(String countryName) {
        return favoriteCountries.contains(countryName);
    }
    public static void addFavorite(String countryName) {
        if (!isFavorite(countryName)) {
            favoriteCountries.add(countryName);
        }
    }

    public static void removeFavorite(String countryName) {
        favoriteCountries.remove(countryName);
    }

    public static List<String> getFavorites() {
        return new ArrayList<>(favoriteCountries);
    }
}
