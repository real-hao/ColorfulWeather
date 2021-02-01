package com.example.colorfulweather.Resource;

import com.qweather.sdk.view.HeConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class InternetResource {

    public static void sendHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
