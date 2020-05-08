package com.example.weatherproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherproject.R;
import com.example.weatherproject.model.Weather;

import java.util.List;

public class CustomWeatherAdapter extends ArrayAdapter<Weather> {
    //Màn hình sử dụng giao diện này
    private Context context; //Activity or context tức là màn hình hiện tại đang sử dụng layout này
    //giao diện(layout) từng dòng muốn hiển thị -->iteam_listview là resource
    private int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    private List<Weather> arrWeather;
    public CustomWeatherAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects); //this biến tham chiếu
        this.context = context;
        this.resource = resource;
        this.arrWeather = objects;
    }
    //Giao diện xấu hay đẹp là do thằng GetView này, cho nên phải hiệu chỉnh nó.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder = new ViewHolder();
            //LayoutInflater là lớp dùng để buil cái layout bình thưởng trở thành code java mà anddroid có thể sử dụng đc
            //Ví dụ nó sẽ chuyển thằng weather_item_listview từ thằng Textfile(xml) bình thường vào hệ thống và buil thành file coding trong bộ nhớ mà nó có thể hiểu
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_item_listview,parent,false);
            viewHolder.imgItemWeather = (ImageView) convertView.findViewById(R.id.img_item_weather);
            viewHolder.tvItemDay = (TextView) convertView.findViewById(R.id.tv_item_day);
            viewHolder.tvItemThu = (TextView) convertView.findViewById(R.id.tv_item_thu);
            viewHolder.tvItemGio =(TextView) convertView.findViewById(R.id.tv_item_gio);
            viewHolder.tvItemTempMinMax = (TextView) convertView.findViewById(R.id.tv_item_temp_min_max);
            viewHolder.tvItemStatus = (TextView) convertView.findViewById(R.id.tv_item_status);
            viewHolder.tvItemDoam = (TextView) convertView.findViewById(R.id.tv_item_doam);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Weather weather = arrWeather.get(position);
        viewHolder.tvItemDay.setText(weather.getDay());
        viewHolder.tvItemThu.setText(weather.getThu());
        viewHolder.tvItemGio.setText(weather.getGio());
        viewHolder.tvItemTempMinMax.setText(weather.getMaxTemp()+"/"+weather.getMinTemp());
        viewHolder.tvItemStatus.setText(weather.getStatus());
        viewHolder.tvItemDoam.setText(weather.getDoAm());

        switch (weather.getIcons()){
            case "01d": viewHolder.imgItemWeather.setImageResource(R.drawable.d01d);
                break;
            case "01n": viewHolder.imgItemWeather.setImageResource(R.drawable.d01n);
                break;
            case "02d": viewHolder.imgItemWeather.setImageResource(R.drawable.d02d);
                break;
            case "02n": viewHolder.imgItemWeather.setImageResource(R.drawable.d02n);
                break;
            case "03d": viewHolder.imgItemWeather.setImageResource(R.drawable.d03d);
                break;
            case "03n": viewHolder.imgItemWeather.setImageResource(R.drawable.d03n);
                break;
            case "04d": viewHolder.imgItemWeather.setImageResource(R.drawable.d04d);
                break;
            case "04n": viewHolder.imgItemWeather.setImageResource(R.drawable.d04n);
                break;
            case "09d": viewHolder.imgItemWeather.setImageResource(R.drawable.d09d);
                break;
            case "09n": viewHolder.imgItemWeather.setImageResource(R.drawable.d09n);
                break;
            case "10d": viewHolder.imgItemWeather.setImageResource(R.drawable.d10d);
                break;
            case "10n": viewHolder.imgItemWeather.setImageResource(R.drawable.d10n);
                break;
            case "11d": viewHolder.imgItemWeather.setImageResource(R.drawable.d11d);
                break;
            case "11n": viewHolder.imgItemWeather.setImageResource(R.drawable.d11d);
                break;
            case "13d": viewHolder.imgItemWeather.setImageResource(R.drawable.d13d);
                break;
            case "13n": viewHolder.imgItemWeather.setImageResource(R.drawable.d13d);
                break;
            case "50d": viewHolder.imgItemWeather.setImageResource(R.drawable.d50d);
                break;
            default:
                viewHolder.imgItemWeather.setImageResource(R.drawable.fail);
        }

        return convertView;
    }
    public class ViewHolder{
        ImageView imgItemWeather;
        TextView tvItemDay;
        TextView tvItemThu;
        TextView tvItemGio;
        TextView tvItemTempMinMax;
        TextView tvItemStatus;
        TextView tvItemDoam;


    }
}
