package com.example.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText edtSearch;
    ImageButton ibtnSearch;
    Button btnNextDay;
    TextView tvCity,tvCountry,tvTemp,tvStatus,tvTime,tvDoAm,tvApSuatKK,tvSpeedWind,tvCloud,tvTempMinMax,tvVisibility;
    ImageView imgWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ẩn bàn phím sau khi nhấn search
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
                api_key(String.valueOf(edtSearch.getText()));
            }
        });
    }

    private void api_key(final String City){
        OkHttpClient client = new OkHttpClient();
        //OkHttp là thư viện giúp chúng ta tương tác (gửi nhận dữ liệu từ app lên sever và ngược lại) tốt giữa app và sever thông qua giao thức HTTP(sử dụng internet)
        //Dùng để đọc nội dung trên đường dẫn , đọc hình ảnh từ đường dẫn
        //Request.Builder : hỗ trợ tạo request bao gồm : HTTP Method, header, cookie, media type, …
        //RestClient : chịu trách nhiệm giao tiếp với REST service bao gồm gửi Request và nhận Response.
        //Gửi yêu cầu lên webside(sever) bằng đường dẫn (url:...) sau đó ấn build để thực thi
        // Tài liệu :https://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
        Request request =new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=c76f12f693f3f8719f79b67be13546b4&units=metric")
//                .get()
                .build();
        //Giải thích strictMode: https://helpex.vn/question/lam-cach-nao-de-sua-loi-android-os-networkonmainthreadexception--5cb02216ae03f645f4200743?page=2
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // Tạo Cuộc gọi đồng bộ, sử dụng đối tượng client để tạo ra đối tượng Call và sử dụng phương thức execute để thực hiện
            Response response = client.newCall(request).execute();
            //Từ thằng đối tượng client trong OkHttpClient() gọi phương thưc newCall(tryền vào thằng request), lấy từ trong hàng đợi enqueue ra thằng CallBack
            //Nếu lấy từ sever ra fail nó sẽ nhảy vào hàm onFailure báo ra log.e
            //Nếu nó trả lời thì sẽ nhảy xuống hàm onResponse
            //Cuộc gọi không đồng bộ thông qua phương thức enqueue
            //Nếu bạn đang sử dụng Android và muốn cập nhật giao diện người dùng(UI), bạn cần sử dụng Content.runOnUiThread (new Runnable)
            //để đồng bộ hóa với UI Thread.
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("Error","Network Error");
                }
                @Override
                //Tạo ra thằng responseData để hứng dữ liệu từ thằng response trả về(@NotNull Response response) lấy ra cái thân của json (body()) kiểu
                //trả về là dạng chuỗi string()
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData =response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(responseData); //Giá trị của json object này đã được đổ trong biến responseData ,chỉ cần gán biến responseData thì nó sẽ đọc đc dữ liệu bên trong json object

                        setText(tvCity,City);
                        JSONArray jsonArrayWeather=jsonObject.getJSONArray("weather");
                        JSONObject objectWeather=jsonArrayWeather.getJSONObject(0);
                        String status=objectWeather.getString("description");
                        setStatus(tvStatus,status);
                        String icons = objectWeather.getString("icon");
                        setImage(imgWeather,icons);


                        JSONObject jsonObjectMain= jsonObject.getJSONObject("main");
                        int temp=jsonObjectMain.getInt("temp");
                        setText(tvTemp,temp+"");
                        int humidity = jsonObjectMain.getInt("humidity");
                        setText(tvDoAm,humidity+"%");
                        int pressure = jsonObjectMain.getInt("pressure");
                        setText(tvApSuatKK,pressure+"");
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
                        setText(tvVisibility,visibility+"");


                        String time = jsonObject.getString("dt");// trả về giá trị giây tính từ 1.1.1970 nên phải chuyển đổi sang dạng thứ ngày tháng
                        long epkieu = Long.valueOf(time);
                        Date date = new Date(epkieu*1000); //ép kiểu về mili giây
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-M-yyyy");
                        String timeDate = simpleDateFormat.format(date);
                        setText(tvTime,timeDate);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Ketqua",responseData);
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
                    case "01n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "02d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "03d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "04d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "09d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "09n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "10d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "10n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "11d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "11n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "13d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "13n": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "50d": imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.d50d));
                        break;
                    default:
                        imgWeather.setImageDrawable(getResources().getDrawable(R.drawable.fail));
                }
            }
        });
    }
    private void setStatus(final TextView text,final String value){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                switch (value) {
                    case "clear sky":
                        text.setText("Bầu trời quang đãng");
                        break;
                    case "few clouds":
                        text.setText("Hơi Có Mây");
                        break;
                    case "scattered clouds":
                        text.setText("Có mây rải rác");
                        break;
                    case "broken clouds":
                        text.setText("Nhiều mây");
                        break;
                    case "light rain":
                        text.setText("Mưa rơi nhẹ hạt");
                        break;
                    case "shower rain":
                        text.setText("Mưa rào");
                        break;
                    case "rain":
                        text.setText("Mưa");
                        break;
                    case "thunderstorm":
                        text.setText("Dông, có sấm chớp");
                        break;
                    case "snow":
                        text.setText("Tuyết rơi");
                        break;
                    case "mist":
                        text.setText("Sương mù");
                        break;
                    case "overcast clouds":
                        text.setText("Nhiều mây u ám");
                        break;
                    default:
                        text.setText(value);
                      break;
                }
            }
        });
    }
    private void AnhXa() {
        edtSearch = (EditText) findViewById(R.id.edt_search);
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
    }


}
