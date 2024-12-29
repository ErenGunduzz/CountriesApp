package com.example.countriesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

public class CompareResultFragment extends Fragment {

    private TextView textViewCountry1Name, textViewCountry1Details;
    private TextView textViewCountry2Name, textViewCountry2Details;

    public CompareResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compare_result, container, false);
        textViewCountry1Name = view.findViewById(R.id.textViewCountry1Name);
        textViewCountry1Details = view.findViewById(R.id.textViewCountry1Details);
        textViewCountry2Name = view.findViewById(R.id.textViewCountry2Name);
        textViewCountry2Details = view.findViewById(R.id.textViewCountry2Details);

        Bundle args = getArguments();
        if (args != null) {
            String country1 = args.getString("country1");
            String country2 = args.getString("country2");

            textViewCountry1Name.setText(country1);
            textViewCountry2Name.setText(country2);

            fetchCountryDetailsFromWikipedia(country1, textViewCountry1Details);
            fetchCountryDetailsFromWikipedia(country2, textViewCountry2Details);
        }

        return view;
    }

    private void fetchCountryDetailsFromWikipedia(String countryName, TextView detailsView) {
        new Thread(() -> {
            try {
                String url = "https://en.wikipedia.org/wiki/" + countryName.replace(" ", "_");
                Document document = Jsoup.connect(url).get();
                Element infobox = document.select("table.infobox").first();

                if (infobox != null) {
                    StringBuilder details = new StringBuilder();
                    details.append("Language: ").append(getInfoBoxValue(infobox, "Official language")).append("\n");
                    details.append("Currency: ").append(getInfoBoxValue(infobox, "Currency")).append("\n");
                    details.append("Capital: ").append(getInfoBoxValue(infobox, "Capital")).append("\n");
                    fetchCountryDetailsFromAPI(countryName, detailsView, details);

                    getActivity().runOnUiThread(() -> detailsView.setText(details.toString()));
                } else {
                    getActivity().runOnUiThread(() -> detailsView.setText("Details not available"));
                }

            } catch (IOException e) {
                getActivity().runOnUiThread(() -> detailsView.setText("Error fetching details"));
            }
        }).start();
    }

    private String getInfoBoxValue(Element infoBox, String key) {
        Element row = infoBox.select("tr:contains(" + key + ")").first();
        if (row != null) {
            String text = row.select("td").text();

            text = text.replaceAll("\\[.*?\\]", "");

            if (!key.equals("Population")) {
                text = text.split("\\d", 2)[0].trim();
            }

            return text.trim();
        }
        return "Not available";
    }


    // REST API ile nüfus bilgisini çek
    private void fetchCountryDetailsFromAPI(String countryName, TextView detailsView, StringBuilder details) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CountryDetailAPI service = retrofit.create(CountryDetailAPI.class);
        Call<List<CountryResponse>> call = service.getCountryData(countryName);

        call.enqueue(new Callback<List<CountryResponse>>() {
            @Override
            public void onResponse(Call<List<CountryResponse>> call, Response<List<CountryResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    CountryResponse countryData = response.body().get(0);
                    long population = countryData.population;
                    String formattedPopulation = NumberFormat.getInstance(Locale.US).format(population);

                    details.append("Population: ").append(formattedPopulation).append("\n");

                    detailsView.setText(details.toString());
                } else {
                    detailsView.setText("Population not available");
                }
            }

            @Override
            public void onFailure(Call<List<CountryResponse>> call, Throwable t) {
                detailsView.setText("Error fetching details");
            }
        });
    }
}
