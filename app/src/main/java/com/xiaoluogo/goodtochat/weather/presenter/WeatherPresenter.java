package com.xiaoluogo.goodtochat.weather.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaoluogo.goodtochat.doman.CityBean;
import com.xiaoluogo.goodtochat.doman.WeatherBean;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.weather.model.IWeatherModel;
import com.xiaoluogo.goodtochat.weather.model.WeatherCallback;
import com.xiaoluogo.goodtochat.weather.model.WeatherModelFactory;
import com.xiaoluogo.goodtochat.weather.view.IWeatherView;

import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public class WeatherPresenter implements IWeatherPresenter {

    private IWeatherView view;
    private IWeatherModel weatherModel;
    private List<CityBean.ResultBean> cities;

    private static final int SHOW_WEATHER = 1;
    private static final int FILE_WEATHER = 2;
    private static final int SHOW_CITY = 3;
    private static final int FILE_CITY = 4;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_WEATHER:
                    String weatherJson = (String) msg.obj;
                    removeMessages(SHOW_WEATHER);
                    processWeatherData(weatherJson);
                    break;
                case FILE_WEATHER:
                    removeMessages(FILE_WEATHER);
                    Toast.makeText((Activity) view, "请求天气失败,稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_CITY:
                    String cityJson = (String) msg.obj;
                    removeMessages(SHOW_CITY);
                    processCityData(cityJson);
                    break;
                case FILE_CITY:
                    removeMessages(FILE_CITY);
                    Toast.makeText((Activity) view, "请求城市列表失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public WeatherPresenter(IWeatherView view) {
        this.view = view;
        weatherModel = WeatherModelFactory.getInstance();
        String weather = CacheUtils.getString((Context) view, "weather");
        if (!TextUtils.isEmpty(weather)) {
            processWeatherData(weather);
        } else {
            showWeather();
        }
    }

    @Override
    public void showWeather() {
        view.showRefresh();
        String cityName = CacheUtils.getString((Context) view, "cityName");
        if (TextUtils.isEmpty(cityName)) {
            cityName = "北京";
        }
        String url = Constants.WEATHER_URL + cityName;
        weatherModel.getCityOrWeatherFromNet(url, new WeatherCallback() {
            @Override
            public void Success(String json) {
                Message msg = Message.obtain();
                msg.what = SHOW_WEATHER;
                msg.obj = json;
                handler.sendMessage(msg);
                CacheUtils.putString((Context) view, "weather", json);
            }

            @Override
            public void Failure(Exception e) {
                Message msg = Message.obtain();
                msg.what = FILE_WEATHER;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取bing每日一图
     */
    @Override
    public void getBingPic() {
        weatherModel.getBingPic(new WeatherCallback() {
            @Override
            public void Success(String json) {
                CacheUtils.putString((Context) view,"bingPic",json);
            }

            @Override
            public void Failure(Exception e) {
                e.printStackTrace();
                L.e(e.getMessage());
            }
        });
    }

    private void processWeatherData(String json) {
        WeatherBean weatherBean = parseWeatherJson(json);
        WeatherBean.ResultBean weather = weatherBean.getResult().get(0);
        view.showWeaher(weather);
        view.hideRefresh();
    }

    private WeatherBean parseWeatherJson(String json) {
        return new Gson().fromJson(json, WeatherBean.class);
    }


    @Override
    public void showDialog() {
        String cityList = CacheUtils.getString((Context) view, "cityList");
        if (!TextUtils.isEmpty(cityList)) {
            processCityData(cityList);
        } else {
            String url = Constants.WEATHER_CITY;
            getCityList(url);
        }
    }

    private void getCityList(String url) {
        weatherModel.getCityOrWeatherFromNet(url, new WeatherCallback() {
            @Override
            public void Success(String json) {
                Message msg = Message.obtain();
                msg.what = SHOW_CITY;
                msg.obj = json;
                handler.sendMessage(msg);
                CacheUtils.putString((Context) view, "cityList", json);
            }

            @Override
            public void Failure(Exception e) {
                Message msg = Message.obtain();
                msg.what = FILE_CITY;
                handler.sendMessage(msg);

                L.e(e.getMessage());
            }
        });
    }

    private void processCityData(String json) {
        CityBean cityBean = parseCityJson(json);
        cities = cityBean.getResult();
        view.showDialog(cities);
    }

    private CityBean parseCityJson(String json) {
        return new Gson().fromJson(json, CityBean.class);
    }
}
