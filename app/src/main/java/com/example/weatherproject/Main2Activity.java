package com.example.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherproject.adapter.CustomWeatherAdapter;
import com.example.weatherproject.model.Weather;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kotlin.collections.DoubleIterator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    private TextView tvThanhPho;
    private ListView lvWeather;
    CustomWeatherAdapter customWeatherAdapter;
    ArrayList<Weather> arrListWeather; //Mảng này chứa giá trị đọc đc từ adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AnhXa();
        lvWeather.setAdapter(customWeatherAdapter);
        Intent intent = getIntent();
        String city7day = intent.getStringExtra("cityname");
        if (city7day.equals(""))
        {
            api_key_7day("Hải Phòng");
        }
        else
            api_key_7day(city7day);

    }

    //lấy dữ liệu API thông qua OKHTTP
    private void api_key_7day (final String city_7day){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/forecast?q="+city_7day+"&units=metric&cnt=16&lang=vi&appid=91628733d74fa79baa10f0aa58daaa23")
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
                    String reponseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(reponseData);
                        JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                        String name = jsonObjectCity.getString("name");
                        tvThanhPho.setText(name);

                        JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                        for (int i =0;i<jsonArrayList.length();i++){
                            JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);

                            String day = jsonObjectList.getString("dt");
                            long dayL = Long.valueOf(day);
                            Date dateDay = new Date(dayL*1000); //ép kiểu về mili giây
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
                            String ngay = simpleDateFormat.format(dateDay);

                            Date dateThu = new Date(dayL*1000);
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE");
                            String thu = simpleDateFormat1.format(dateThu);

                            Date dateGio = new Date(dayL*1000);
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(" HH:mm");
                            String gio = simpleDateFormat2.format(dateGio);

                            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                            String max = jsonObjectMain.getString("temp_max");
                            String min = jsonObjectMain.getString("temp_min");
                            Double a = Double.valueOf(max);
                            Double b = Double.valueOf(min);
                            String tempMax = String.valueOf(a.intValue());
                            String tempMin = String.valueOf(b.intValue());
                            String doam = jsonObjectMain.getString("humidity");

                            JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                            JSONObject jsonObjectWeather =jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("description");
                            if (status.equals("bầu trời quang đãng"))
                            {
                                status = "trời quang đãng";

                            }

                            String icons = jsonObjectWeather.getString("icon");

                            arrListWeather.add(new Weather(ngay,thu,gio,icons,status,tempMax+"°",tempMin+"°",doam));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customWeatherAdapter.notifyDataSetChanged();
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Ketqua",reponseData);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void AnhXa() {
        tvThanhPho = (TextView) findViewById(R.id.tv_thanhpho);
        lvWeather = (ListView) findViewById(R.id.lv_weather);
        arrListWeather = new ArrayList<Weather>();
        customWeatherAdapter = new CustomWeatherAdapter(Main2Activity.this,R.layout.weather_item_listview,arrListWeather);
    }
}
