package com.example.countriesapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PixabayResponse {
    @SerializedName("hits")
    public List<Photo> hits;

    public class Photo {
        @SerializedName("webformatURL")
        public String imageUrl;
    }
}

