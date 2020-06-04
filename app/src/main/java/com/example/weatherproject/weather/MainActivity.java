package com.example.weatherproject.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherproject.map.MapsActivity;
import com.example.weatherproject.R;
import com.example.weatherproject.chatpackage.LoginActivity;
import com.example.weatherproject.covid19.MainCovidActivity;
import com.example.weatherproject.model.CheckConnectivityCallback;
import com.example.weatherproject.model.MyReceiver;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements CheckConnectivityCallback {
    View noConnectionView;
    AutoCompleteTextView edtSearch;
    ImageButton ibtnSearch;
    Button btnNextDay;
    TextView tvCity,tvCountry,tvTemp,tvStatus,tvTime,tvDoAm,tvApSuatKK,tvSpeedWind,tvCloud,tvTempMinMax,tvVisibility;
    ImageView imgWeather, img_navimenu;
    MyReceiver myReceiver;
    DrawerLayout drawerLayout;

    private String city;
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "SFWeather";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private Set<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        //Bắt sự kiện click vào img_menu , hiển thị lên Navigation
        img_navimenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);


        // Bắt sự kiện click vào item trong navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_gps:
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_add:
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.nav_about:
                        Intent intent2 = new Intent(MainActivity.this,MainCovidActivity.class);
                        startActivity(intent2);
                }
                return true;
            }
        });

        myReceiver = new MyReceiver(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, intentFilter);



        Log.d("Saveprefs","OnCreate");
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        //getStringset trả về một tham chiếu đối tượng HashSet dc lưu trong sf
        history = sharedPreferences.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>());

        //Sự kiện chạm vào edtSeach
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoCompleteSource();
            }
        });



        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                city = edtSearch.getText().toString();
                api_key(city);
                if (city.equals("")){
                    api_key("Hải Phòng");
                }
                else
                    api_key(city);

                addSearchInput(edtSearch.getText().toString().trim());

            }

        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("cityname",city);
                startActivity(intent);
            }
        });
    }

    private void api_key(final String City){
        OkHttpClient client = new OkHttpClient();
        Request request =new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&lang=vi&appid=c76f12f693f3f8719f79b67be13546b4&units=metric")
                .build();
        //Fix lỗi network on main thread exception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            client.newCall(request).execute();
            //Cuộc gọi không đồng bộ, yêu cầu sever trả về dữ liệu
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("Error","Network Error");
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData =response.body().string(); //hứng dữ liệu sever trả về
                    try {
                        JSONObject jsonObject=new JSONObject(responseData);

                        String name = jsonObject.getString("name");
                            setText(tvCity,name.toUpperCase());

                        JSONArray jsonArrayWeather=jsonObject.getJSONArray("weather");
                        JSONObject objectWeather=jsonArrayWeather.getJSONObject(0);
                        String status=objectWeather.getString("description");
                        setText(tvStatus,status.toUpperCase());
                        String icons = objectWeather.getString("icon");
                        setImage(imgWeather,icons);


                        JSONObject jsonObjectMain= jsonObject.getJSONObject("main");
                        int temp=jsonObjectMain.getInt("temp");
                        setText(tvTemp,temp+"");
                        int humidity = jsonObjectMain.getInt("humidity");
                        setText(tvDoAm,humidity+"%");
                        int pressure = jsonObjectMain.getInt("pressure");
                        setText(tvApSuatKK,pressure+"hPa");
                        int tempMin = jsonObjectMain.getInt("temp_min");
                        int tempMax = jsonObjectMain.getInt("temp_max");
                        setText(tvTempMinMax,tempMin+"°C/"+tempMax+"°C");


                        JSONObject jsonObjectCloulds = jsonObject.getJSONObject("clouds");
                        int clouds = jsonObjectCloulds.getInt("all");
                        setText(tvCloud,clouds+"%");


                        JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                        double speed = jsonObjectWind.getDouble("speed");
                        setText(tvSpeedWind,speed+"m/s");


                        JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                        String country = jsonObjectSys.getString("country");
                        setText(tvCountry,country);


                        int visibility = jsonObject.getInt("visibility");
                        setText(tvVisibility,visibility+"m");


                        String time = jsonObject.getString("dt");
                        long epkieu = Long.valueOf(time);
                        Date date = new Date(epkieu*1000);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-M-yyyy");
                        String timeDate = simpleDateFormat.format(date);
                        setText(tvTime,timeDate);

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
    private void setImage(final ImageView imageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //paste switch
                switch (value){
                    case "01d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "01n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d01n));
                        break;
                    case "02d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d02n));
                        break;
                    case "03d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d03n));
                        break;
                    case "04d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d04n));
                        break;
                    case "09d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "09n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d09n));
                        break;
                    case "10d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "10n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d10n));
                        break;
                    case "11d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "11n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "13d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "13n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "50d": imgWeather.setImageResource(R.drawable.d50d);
                        break;
                    default:
                        imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.fail));
                }
            }
        });
    }

    // Hiển thị list history khi người dùng ấn vào sự kiện edtSearch
    private void setAutoCompleteSource()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        edtSearch.setAdapter(adapter);
        edtSearch.setThreshold(1);
    }

    //Thêm dữ liệu khi người dùng nhấn vào sự kiện btnSearch
    private void addSearchInput(String input)
    {
        if (!history.contains(input))
        {
            history.add(input);
            setAutoCompleteSource();
        }
    }


    //Hàm này dùng dễ lưu history(value) vào sf,
    //thằng history ở trên add bao nhiều thằng thì hàm này sẽ thêm vào sf bấy nhiêu giá trị
    private void savePrefs()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putStringSet(PREFS_SEARCH_HISTORY, history);
        editor.apply();
    }

    public void onStop() {
        super.onStop();
        savePrefs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void setOnConnectivityChangeListener(boolean isOnline) {
        if (isOnline){
            noConnectionView.setVisibility(View.GONE);
            btnNextDay.setVisibility(View.VISIBLE);
            edtSearch.setVisibility(View.VISIBLE);
            ibtnSearch.setVisibility(View.VISIBLE);
            img_navimenu.setVisibility(View.VISIBLE);
        }else {
            noConnectionView.setVisibility(View.VISIBLE);
            btnNextDay.setVisibility(View.GONE);
            edtSearch.setVisibility(View.GONE);
            ibtnSearch.setVisibility(View.GONE);
            img_navimenu.setVisibility(View.GONE);
        }
    }

    private void AnhXa() {
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edt_search);
        ibtnSearch = (ImageButton) findViewById(R.id.ibtn_seach);
        btnNextDay = (Button) findViewById(R.id.btn_next_day);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvCity.setText("");
        tvCountry = (TextView) findViewById(R.id.tv_country);
        tvCountry.setText("");
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvTemp.setText("");
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvStatus.setText("");
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvTime.setText("");
        tvDoAm = (TextView) findViewById(R.id.tv_do_am);
        tvDoAm.setText("");
        tvApSuatKK = (TextView) findViewById(R.id.tv_ap_suat_kk);
        tvApSuatKK.setText("");
        tvSpeedWind = (TextView) findViewById(R.id.tv_speedwind);
        tvSpeedWind.setText("");
        tvVisibility = (TextView) findViewById(R.id.tv_visiblity);
        tvVisibility.setText("");
        tvTempMinMax = (TextView) findViewById(R.id.tv_temp_min_max);
        tvTempMinMax.setText("");
        tvCloud = (TextView) findViewById(R.id.tv_clouds);
        tvCloud.setText("");
        imgWeather = (ImageView) findViewById(R.id.img_Weather);

        drawerLayout = findViewById(R.id.drawer_layout);
        img_navimenu = findViewById(R.id.img_menu);

        noConnectionView = findViewById(R.id.no_connection_view);

    }
}
