package com.example.countriesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// define adapter class to display recyclerview
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    ArrayList<String> countryList;
    Context context;
    OnCountrySelectedListener listener; //Used when comparing countries

    //Constructor method
    public CountryListAdapter(Context context, ArrayList<String> countryList, OnCountrySelectedListener listener) {
        this.context = context;
        this.countryList = countryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String country = countryList.get(position);
        holder.textView.setText(country);

        // Clicking
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CountryDetailActivity.class);
            intent.putExtra("country_name", country);
            context.startActivity(intent);
        });
    /*
        holder.itemView.setOnClickListener(v -> {
            listener.onCountrySelected(country);
        });

     */
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnCountrySelectedListener {
        void onCountrySelected(String country);
    }

}
