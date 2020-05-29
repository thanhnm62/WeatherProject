package com.example.weatherproject.chatpackage.fragments;

import com.example.weatherproject.chatpackage.Notification.MyResponse;
import com.example.weatherproject.chatpackage.Notification.Sender;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEonzVsQ:APA91bEbOr9sGoM1Yz778MsyDJe0MljNAJ9B7mcOHfr2Y342MTeCXsnNfKrt1LuiLCM839AKCaNaa7Td09Uw6nkx6RnKxex1Dszg_jzBvuAQ69QGyzzVs9eg4ZwVFrrRC13Qe5YAz1eg"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
