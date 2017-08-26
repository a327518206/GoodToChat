// IWeatherService.aidl
package com.xiaoluogo.goodtochat;

// Declare any non-default types here with import statements

interface IWeatherService {
    void autoUpdate();

    boolean updateWeather();
}
