package com.example.may.myproject.Remote;


import com.example.may.myproject.Model.MyResponse;
import com.example.may.myproject.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAArsuXHF8:APA91bFZ2Q6PAbdo-K2BCw-dQ4jMfvnI5gcDx8YuZhZCgQ-RClY4afZAmGq5Av9Jy70X3V7cv_9eRgEfgJFI62ryHlZ7AgC4VACJF2e_OLk7rcu7EPgabipYAWRotzEr1BVszRAoarwb"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
