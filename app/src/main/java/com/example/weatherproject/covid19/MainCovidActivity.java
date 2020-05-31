package com.example.weatherproject.covid19;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.weatherproject.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainCovidActivity extends AppCompatActivity {

    TextView tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths,tvAffectedCountries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_covid);

        AnhXa();

        getDataCovid19();
    }


    private void getDataCovid19(){
        OkHttpClient client = new OkHttpClient();

        Request request =new okhttp3.Request.Builder().url("https://corona.lmao.ninja/v2/all/")
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            Response response = client.newCall(request).execute();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override

                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData =response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(responseData);

                            String cases = jsonObject.getString("cases");
                            setText(tvCases,cases);

                            String recovered = jsonObject.getString("recovered");
                            setText(tvRecovered,recovered);

                            String citical = jsonObject.getString("critical");
                            setText(tvCritical,citical);

                            String active = jsonObject.getString("active");
                            setText(tvActive,active);

                            String todayCases = jsonObject.getString("todayCases");
                            setText(tvTodayCases,todayCases);


                            String deaths = jsonObject.getString("deaths");
                            setText(tvTotalDeaths,deaths);

                            String todayDeaths = jsonObject.getString("todayDeaths");
                            setText(tvTodayDeaths,todayDeaths);

                            String affectedCountries = jsonObject.getString("affectedCountries");
                            setText(tvAffectedCountries,affectedCountries);


                    } catch (JSONException e) {
                        e.printStackTrace();


                    }
                    Log.d("KQ",responseData);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setText(final TextView text, final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    public void goTrackCountries(View view) {

        startActivity(new Intent(getApplicationContext(),CountryCovidActivity.class));

    }


    private void AnhXa() {
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);



    }
}
