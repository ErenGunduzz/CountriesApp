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
import java.util.Locale;

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
                    details.append("Population: ").append(getInfoBoxValue(infobox, "Population")).append("\n");

                    getActivity().runOnUiThread(() -> detailsView.setText(details.toString()));
                } else {
                    getActivity().runOnUiThread(() -> detailsView.setText("Details not available"));
                }

            } catch (IOException e) {
                getActivity().runOnUiThread(() -> detailsView.setText("Error fetching details"));
            }
        }).start();
    }

    private String getInfoBoxValue(Element infobox, String key) {
        Element row = infobox.select("tr:contains(" + key + ")").first();
        if (row != null) {
            return row.select("td").text().replaceAll("\\[.*?\\]", "");
        }
        return "Not available";
    }
}
