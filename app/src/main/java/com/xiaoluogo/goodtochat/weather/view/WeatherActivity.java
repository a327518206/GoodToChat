package com.xiaoluogo.goodtochat.weather.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.doman.CityBean;
import com.xiaoluogo.goodtochat.doman.WeatherBean;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.weather.presenter.IWeatherPresenter;
import com.xiaoluogo.goodtochat.weather.presenter.WeatherPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public class WeatherActivity extends AppCompatActivity implements IWeatherView {

    private IWeatherPresenter weatherPresenter;

    private Toolbar toolbar_weather;
    private Button btn_setting;
    private ImageView iv_weather_bg;
    private SwipeRefreshLayout srl_weather;
    private ScrollView sv_weather;
    private TextView tv_city;
    private TextView tv_updatetime;
    private TextView tv_current;
    private TextView tv_weather;
    private TextView tv_airCondition;
    private TextView tv_pollutionIndex;
    private TextView tv_wind;
    private TextView tv_humidity;
    private TextView tv_sunrise;
    private TextView tv_sunset;
    private TextView tv_coldIndex;
    private TextView tv_dressingIndex;
    private TextView tv_exerciseIndex;
    private TextView tv_washIndex;
    private LinearLayout forecast_layout;

    private Spinner spin_province;
    private Spinner spin_city;
    private Button btn_dialog_cancel;
    private Button btn_dialog_ok;
    private Dialog dialog;
    private List<String> provinces;
    private List<CityBean.ResultBean> city;
    private List<String> cities;
    private String currentCity;
    private String tempCity;
    /**
     * 选中的省份
     */
    private int provincePosition;
    /**
     * 缓存中的省份
     */
    private int cacheProvince;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        weatherPresenter = new WeatherPresenter(this);
        initListener();
    }

    private void initListener() {
        srl_weather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherPresenter.showWeather();
                weatherPresenter.getBingPic();
                showBingPic();
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherPresenter.showDialog();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_weather);
        toolbar_weather = (Toolbar) findViewById(R.id.toolbar_weather);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        iv_weather_bg = (ImageView) findViewById(R.id.iv_weather_bg);
        srl_weather = (SwipeRefreshLayout) findViewById(R.id.srl_weather);
        sv_weather = (ScrollView) findViewById(R.id.sv_weather);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_updatetime = (TextView) findViewById(R.id.tv_updatetime);
        tv_current = (TextView) findViewById(R.id.tv_current);
        tv_weather = (TextView) findViewById(R.id.btn_weather);
        tv_airCondition = (TextView) findViewById(R.id.tv_airCondition);
        tv_pollutionIndex = (TextView) findViewById(R.id.tv_pollutionIndex);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_sunrise = (TextView) findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) findViewById(R.id.tv_sunset);
        tv_coldIndex = (TextView) findViewById(R.id.tv_coldIndex);
        tv_dressingIndex = (TextView) findViewById(R.id.tv_dressingIndex);
        tv_exerciseIndex = (TextView) findViewById(R.id.tv_exerciseIndex);
        tv_washIndex = (TextView) findViewById(R.id.tv_washIndex);
        forecast_layout = (LinearLayout) findViewById(R.id.forecast_layout);
        showBingPic();
    }


    @Override
    public void showDialog(List<CityBean.ResultBean> city) {
        if (city != null && city.size() > 0) {
            this.city = city;
            dialog = new Dialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.city_dialog_layout, null);
            initDialog(view);
            dialog.setContentView(view);
            dialog.show();
            provinces = new ArrayList<>(34);
            for (CityBean.ResultBean result : city) {
                provinces.add(result.getProvince());
            }
            spin_province.setAdapter(new ArrayAdapter<>(WeatherActivity.this, android.R.layout.simple_list_item_1, provinces));
            spin_province.setOnItemSelectedListener(new ProvinceSelectedListener());
            spin_city.setOnItemSelectedListener(new CitySelectedListener());
            cacheProvince = CacheUtils.getInt(this,"provincePosition");
            spin_province.setSelection(cacheProvince);
        }
    }

    class CitySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (cities != null) {
                tempCity = cities.get(position);
                L.e(tempCity);
            }
            L.e(position+"当前位置");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class ProvinceSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cities = new ArrayList<>();
            for (CityBean.ResultBean.City c : city.get(position).getCities()) {
                cities.add(c.getCityName());
            }
            spin_city.setAdapter(new ArrayAdapter<>(WeatherActivity.this, android.R.layout.simple_list_item_1, cities));
            provincePosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void initDialog(View view) {
        spin_province = (Spinner) view.findViewById(R.id.spin_province);
        spin_city = (Spinner) view.findViewById(R.id.spin_city);
        btn_dialog_cancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
        btn_dialog_ok = (Button) view.findViewById(R.id.btn_dialog_ok);
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentCity.equals(tempCity)) {
                    currentCity = tempCity;
                    CacheUtils.putString(WeatherActivity.this,"cityName",currentCity);
                    if(cacheProvince != provincePosition){
                        CacheUtils.putInt(WeatherActivity.this,"provincePosition",provincePosition);
                    }
                    weatherPresenter.showWeather();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showWeaher(WeatherBean.ResultBean weather) {
        if (weather != null) {
            tv_city.setText(weather.getCity());
            currentCity = tv_city.getText().toString();
            tv_updatetime.setText("更新时间 - " + weather.getTime());
            tv_current.setText(weather.getTemperature());
            tv_weather.setText(weather.getWeather());
            tv_airCondition.setText(weather.getAirCondition());
            tv_pollutionIndex.setText(weather.getPollutionIndex());
            tv_wind.setText(weather.getWind());
            tv_humidity.setText(weather.getHumidity());
            tv_sunrise.setText(weather.getSunrise());
            tv_sunset.setText(weather.getSunset());
            tv_coldIndex.setText(weather.getColdIndex());
            tv_dressingIndex.setText(weather.getDressingIndex());
            tv_exerciseIndex.setText(weather.getExerciseIndex());
            tv_washIndex.setText(weather.getWashIndex());
            forecast_layout.removeAllViews();
            for (WeatherBean.ResultBean.FutureBean futureBean : weather.getFuture()) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecast_layout, false);
                TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
                TextView tv_dayTime = (TextView) view.findViewById(R.id.tv_dayTime);
                TextView tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
                TextView tv_everywind = (TextView) view.findViewById(R.id.tv_everywind);
                tv_data.setText(futureBean.getDate());
                tv_dayTime.setText(futureBean.getDayTime());
                tv_temperature.setText(futureBean.getTemperature());
                tv_everywind.setText(futureBean.getWind());
                forecast_layout.addView(view);
            }
        }
    }

    @Override
    public void hideRefresh() {
        srl_weather.setRefreshing(false);
    }

    @Override
    public void showRefresh() {
        srl_weather.setRefreshing(true);
    }

    @Override
    public void showBingPic() {
        String url = CacheUtils.getString(this, "bingPic");
        if (url != null) {
//            RequestOptions options = new RequestOptions()
//                    .error();
            Glide.with(this).load(url).into(iv_weather_bg);
        }
        hideRefresh();
    }

    @Override
    protected void onDestroy() {
        weatherPresenter.removeMessage();
        super.onDestroy();
    }
}

