package com.example.countriesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
            textViewCountry1Details.setText("Details for " + country1);
            textViewCountry2Name.setText(country2);
            textViewCountry2Details.setText("Details for " + country2);
        }

        return view;
    }
}
