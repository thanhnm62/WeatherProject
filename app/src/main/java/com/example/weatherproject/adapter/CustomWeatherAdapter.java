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

    //Adapter có nhiệm vụ là trung gian để chứa data hiển thị lên listview
public class CustomWeatherAdapter extends ArrayAdapter<Weather> {
    //Màn hình (Main2Activity) sử dụng giao diện này
    private Context context; //Activity or context tức là màn hình hiện tại đang sử dụng layout này
    //iteam_listview là resource
    private int resource;
    //danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    private List<Weather> arrWeather;
    //Constructor có tham số của Class CustomWeatherAdapter
    public CustomWeatherAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
        //this biến tham chiếu
        this.context = context;
        this.resource = resource;
        this.arrWeather = objects;
    }
    //Giao diện(ListView) xấu hay đẹp là do thằng GetView này, cho nên phải hiệu chỉnh nó.
    //Thằng getView này dùng để tạo ra các view rồi thêm vào Listview
    //GetView này là từng row trong listView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        //(View là gì : View là tất cả các TextView,ImageView, Button,layout,item,....đc hiển thị lên giao diện thì gọi là View)
        //Nếu cái convertView == null (tức là chưa có view nào được khởi tạo (lần đầu tiên gọi tới hàm)) thì nó sẽ nhảy vào hàm này và tìm
        //các view (cụ thể trong bài là TextView, và ImageView) với lệnh findViewById() và gán nó vào viewHolder ,sau đó thiết lập
        //viewHolder thành thẻ của convertView.(setTag)
        if(convertView==null){
            viewHolder = new ViewHolder();
            //LayoutInflater là lớp dùng để buil cái layout bình thưởng trở thành code java mà anddroid có thể sử dụng đc
            //Ví dụ nó sẽ chuyển thằng weather_item_listview từ thằng Textfile(xml) bình thường vào hệ thống và buil thành file coding trong bộ nhớ mà nó có thể hiểu
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_item_listview,parent,false);
            viewHolder.imgItemWeather = convertView.findViewById(R.id.img_item_weather);
            viewHolder.tvItemDay =  convertView.findViewById(R.id.tv_item_day);
            viewHolder.tvItemThu =  convertView.findViewById(R.id.tv_item_thu);
            viewHolder.tvItemGio = convertView.findViewById(R.id.tv_item_gio);
            viewHolder.tvItemTempMinMax = convertView.findViewById(R.id.tv_item_temp_min_max);
            viewHolder.tvItemStatus = convertView.findViewById(R.id.tv_item_status);
            viewHolder.tvItemDoam = convertView.findViewById(R.id.tv_item_doam);

            //set viewHolder thành tag của convertView
            convertView.setTag(viewHolder);
        }

        //Các lần tiếp theo gọi tới hàm ,tức các view đã được khởi tạo và gán vào Tag , nó sẽ không phải tìm lại ID nữa mà nó sẽ lấy
        //trực tiếp các view từ trong Tag ra và sử dụng nó như 1 thành phần của viewHolder.
        // Tóm lại, thằng viewHolder này đc dùng để gán thẻ cho View tránh phải gọi thằng findByViewId nhiều lần, sẽ giúp vuốt listView mượt hơn.
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //weather là đối tượng của Class Weather trong package model.
        //Lấy ra từng đối tượng weather trong arrListWeather được truyền từ Activity1 thông qua customWeatherAdapter (getpositon ở đây là vị trí của mỗi thằng trong danh sách ví dụ có 16 thằng thì nó sẽ lấy ra vị trí từ 0-15)
        //Sau đó gán giá trị lần lượt cho 16 thằng đó.
        Weather weather = arrWeather.get(position);
        viewHolder.tvItemDay.setText(weather.getDay());
        viewHolder.tvItemThu.setText(weather.getThu());
        viewHolder.tvItemGio.setText(weather.getGio());
        viewHolder.tvItemTempMinMax.setText(weather.getMaxTemp()+"/"+weather.getMinTemp());
        viewHolder.tvItemStatus.setText(weather.getStatus());
        viewHolder.tvItemDoam.setText(weather.getDoAm());

        //Set icon theo key "icon" API trả về
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
        //Sau khi set dữ liệu xong thì sẽ trả ra convertView.
        return convertView;
    }

    //Tạo class ViewHolder để sử dụng ở bên trên
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
