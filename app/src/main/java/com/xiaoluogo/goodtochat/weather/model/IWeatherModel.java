package com.xiaoluogo.goodtochat.weather.model;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public interface IWeatherModel {
    void getCityOrWeatherFromNet(String url,WeatherCallback callback);
    void getBingPic(WeatherCallback callback);
}
