package com.example.weatherproject.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.weatherproject.R;

public class ItemCountryActivity extends AppCompatActivity {

    private  int positionCountry;
    TextView tvCountry,tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_country);

        Intent intent = getIntent();
        positionCountry = intent.getIntExtra("position",0);


        tvCountry = findViewById(R.id.tvCountry);
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);

        tvCountry.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getCountry());
        tvCases.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getCases());
        tvRecovered.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getRecovered());
        tvCritical.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getCritical());
        tvActive.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getActive());
        tvTodayCases.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getTodayCases());
        tvTotalDeaths.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getDeaths());
        tvTodayDeaths.setText(CountryCovidActivity.countryModelsList.get(positionCountry).getTodayDeaths());


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

