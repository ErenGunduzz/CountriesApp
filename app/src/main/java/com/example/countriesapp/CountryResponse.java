package com.example.countriesapp;

import com.google.gson.annotations.SerializedName;

public class CountryResponse {
    @SerializedName("population")
    public long population;

    @SerializedName("flags")
    public Flags flags;

    public static class Flags {
        @SerializedName("png")
        public String png;
    }

    /*
    example json response.
    {
      "population": 85000000,
      "flags": {
        "png": "https://example.com/flag.png"
        }
       }
     */
}

