package com.xiaoluogo.goodtochat.weather.view;

import com.xiaoluogo.goodtochat.doman.CityBean;
import com.xiaoluogo.goodtochat.doman.WeatherBean;

import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public interface IWeatherView {
    void showDialog(List<CityBean.ResultBean> city);
    void showWeaher(WeatherBean.ResultBean weather);
    void hideRefresh();
    void showRefresh();
    void showBingPic();
}
