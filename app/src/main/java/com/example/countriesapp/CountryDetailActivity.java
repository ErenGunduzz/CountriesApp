package com.example.countriesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CountryDetailActivity extends AppCompatActivity {

    TextView countryNameTextView, countryDetailsTextView;
    String countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryDetailsTextView = findViewById(R.id.countryDetailsTextView);

        // Intent ile gelen ülke adını al
        countryName = getIntent().getStringExtra("country_name");
        countryNameTextView.setText(countryName);

        // Wikipedia'dan veriyi çek
        new FetchCountryDetails().execute();
    }

    private class FetchCountryDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Wikipedia ülke sayfası URL'sini oluştur
                String url = "https://en.wikipedia.org/wiki/" + countryName.replace(" ", "_");
                Document doc = Jsoup.connect(url).get();

                // Sayfadaki infobox'tan veri çekme
                Element infoBox = doc.select("table.infobox").first();
                if (infoBox != null) {
                    StringBuilder details = new StringBuilder();
                    // Nüfus, Başkent, Dil gibi alanları çekelim
                    details.append("Capital: ").append(getInfoBoxValue(infoBox, "Capital")).append("\n");
                    details.append("Language: ").append(getInfoBoxValue(infoBox, "Official language")).append("\n");
                    details.append("Currency: ").append(getInfoBoxValue(infoBox, "Currency")).append("\n");

                    return details.toString();
                } else {
                    return "Details not available.";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "Error fetching details.";
            }
        }

        @Override
        protected void onPostExecute(String details) {
            super.onPostExecute(details);
            countryDetailsTextView.setText(details);
        }
    }

    // InfoBox'tan belirli bir değeri al
    private String getInfoBoxValue(Element infoBox, String key) {
        Element row = infoBox.select("tr:contains(" + key + ")").first();
        if (row != null) {
            String text = row.select("td").text();

            // [] içindeki metni ve sayıları temizle
            text = text.replaceAll("\\[.*?\\]", ""); // Köşeli parantez ve içindekileri sil
            if (!key.equals("Population")) {
                text = text.replaceAll("\\d.*", ""); // Eğer 'Population' değilse, ilk sayıdan sonrasını sil
            }

            return text.trim(); // Son hali boşluklardan arındır
        }
        return "Not available";
    }




}

