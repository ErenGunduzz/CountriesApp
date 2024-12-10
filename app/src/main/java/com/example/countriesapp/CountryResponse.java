package com.example.countriesapp;

import com.google.gson.annotations.SerializedName;

public class CountryResponse {
    @SerializedName("population")
    public long population;

    @SerializedName("flags")
    public Flags flags;

    public class Flags {
        @SerializedName("png")
        public String png;
    }

}

