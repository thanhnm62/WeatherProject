package com.example.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

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
                //2 dòng này dùng để ẩn bàn phím sau khi nhấn search, nếu không bàn phím nó sẽ che màn hình hiển thị ở dưới
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
                String city = edtSearch.getText().toString();
                api_key(city);
                if (city.equals("")){
                    api_key("Hải Phòng");
                }
                else
                    api_key(city);
            }
        });

        //Sau khi click vào button "Xem các ngày tiếp theo",
        //Khai báo biến city để lấy ra giá trị city lúc mình nhập vào từ ô seach ( ví dụ nhập Hải Phòng nó sẽ lấy ra giá trị Hải Phòng)
        //Sau đó khởi tạo thằng intent để chuyển từ màn hình 1 sang màn hình 2
        //put.Extra là hàm để gửi giá trị city ( đã nói ở trên) từ màn hình 1 sang màn hình thứ 2.
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
        //OkHttp là thư viện giúp tương tác (gửi nhận dữ liệu từ app lên sever và ngược lại) tốt giữa app và sever thông qua giao thức HTTP(cần cấp quyền internet trong manifests)
        //Dùng để đọc nội dung trên đường dẫn , đọc hình ảnh từ đường dẫn.....
        //Request.Builder : hỗ trợ tạo request bao gồm : HTTP Method, header, cookie, media type, …
        //RestClient : chịu trách nhiệm giao tiếp với REST service bao gồm gửi Request và nhận Response.
        //Gửi request lên webside(sever) bằng đường dẫn (url:...) sau đó ấn build để thực thi
        // Tài liệu :https://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
        Request request =new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&lang=vi&appid=c76f12f693f3f8719f79b67be13546b4&units=metric")
                .build();
        //Giải thích strictMode: https://helpex.vn/question/lam-cach-nao-de-sua-loi-android-os-networkonmainthreadexception--5cb02216ae03f645f4200743?page=2
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // Tạo Cuộc gọi đồng bộ, sử dụng đối tượng client để tạo ra đối tượng Call và sử dụng phương thức execute để thực hiện
            //Hàm này có cũng được không có cũng dc thêm vào cho đủ cấu trúc OKHTTP
            Response response = client.newCall(request).execute();

            //Tạo cuộc gọi không đồng bộ thông qua phương thức enqueue
            //Từ thằng đối tượng client trong OkHttpClient() gọi phương thưc newCall(tryền vào thằng request), lấy từ trong hàng đợi enqueue ra thằng CallBack
            //Nếu API từ sever trả về fail nó sẽ nhảy vào hàm onFailure báo ra log.e
            //Nếu sever trả về giá trị thì sẽ nhảy xuống hàm onResponse
            //Nếu đang sử dụng Android và muốn cập nhật giao diện người dùng(UI), cần sử dụng runOnUiThread (new Runnable) để đồng bộ hóa với UI Thread.
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("Error","Network Error");
                }
                @Override
                //Tạo ra thằng responseData để hứng dữ liệu từ sever trả về(@NotNull Response response) lấy ra cái thân của json (body()) kiểu
                //trả về là dạng chuỗi string()
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData =response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(responseData); //Giá trị của json object này đã được đổ trong biến responseData ,
                        // chỉ cần gán biến responseData thì nó sẽ đọc đc dữ liệu bên trong json object
                        //Muốn test thì comment từ đoạn try đến catch để hiển thị log . Lúc này dữ liệu nó đã trả về từ sever thông qua thằng response.body().string()


                        //Mấy cái key này là dạng dữ liệu Json , thầy hỏi chỉ cần bảo lấy dữ liệu trả ra từ sever theo dạng chuỗi Json
                        //sau đó gán giá trị cho các View
                        String name = jsonObject.getString("name");
                            setText(tvCity,name.toUpperCase());

                        JSONArray jsonArrayWeather=jsonObject.getJSONArray("weather");
                        JSONObject objectWeather=jsonArrayWeather.getJSONObject(0);
                        String status=objectWeather.getString("description");
//                        setStatus(tvStatus,status);
                        setText(tvStatus,status.toUpperCase());
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
                    Log.d("KQ",responseData);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //thằng runOnUiThread này dùng để thay đổi giao diện
    //thằng runOnUIThread này dành riêng cho việc xử lí thay đổi giao diện nặng (kiểu nhiều dữ liệu cần thay đổi)
    //,nếu chỉ chạy trong main thread thì ứng dụng sẽ bị treo.
    //nếu chỉ set một vài cái thì dùng main thread cũng đc. Cái nào tác vụ nặng như sét giao diện vài trăm cái.
    // Sét ảnh bla bla thì tách ra cho tránh lỗi ARN(treo app)
    //tài liệu : https://developer.android.com/guide/components/processes-and-threads.html#WorkerThreads
    private void setText(final TextView text, final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    //thằng này tương tự hàm trên
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
