package com.xiaoluogo.goodtochat.weather.model;

import com.xiaoluogo.goodtochat.utils.L;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public class WeatherModel implements IWeatherModel {

    /**
     * 在网络中获取天气
     */
    public void getCityOrWeatherFromNet(String url, final WeatherCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e(e.getMessage());
                e.printStackTrace();
                callback.Failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                L.e(json);
                callback.Success(json);
            }
        });
    }

    @Override
    public void getBingPic(final WeatherCallback callback) {
        String url = "http://guolin.tech/api/bing_pic";
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e(e.getMessage());
                e.printStackTrace();
                callback.Failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                L.e(json);
                callback.Success(json);
            }
        });
    }
}
