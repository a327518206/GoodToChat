package com.xiaoluogo.goodtochat.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;

import com.xiaoluogo.goodtochat.IWeatherService;
import com.xiaoluogo.goodtochat.utils.CacheUtils;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;
import com.xiaoluogo.goodtochat.weather.model.IWeatherModel;
import com.xiaoluogo.goodtochat.weather.model.WeatherCallback;
import com.xiaoluogo.goodtochat.weather.model.WeatherModelFactory;

public class WeatherService extends Service {

    public static final String AUTO_UPDATE_WEATHER = "com.xiaoluogo.goodtochat_AUTO_UPDATE_WEATHER";

    private WeatherService service = WeatherService.this;
    private boolean isAuto;
    private IWeatherModel weatherModel;
    public static boolean updateSuccess;

    @Override
    public IBinder onBind(Intent intent) {
        isAuto = true;
        return stub;
    }

    private IWeatherService.Stub stub = new IWeatherService.Stub() {
        @Override
        public void autoUpdate() throws RemoteException {
            service.autoUpdate();
        }

        @Override
        public boolean updateWeather() throws RemoteException {
            return service.updateWeather();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        weatherModel = WeatherModelFactory.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        autoUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    private void autoUpdate() {
        updateBingPic();
        updateWeather();
        if (isAuto) {
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int hour = 8 * 1000 * 60 * 60;
            long triggerAtTime = SystemClock.elapsedRealtime() + hour;
            Intent i = new Intent(this,WeatherService.class);
            PendingIntent pi = PendingIntent.getService(this,0,i,0);
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        }
    }

    private void updateBingPic() {
        weatherModel.getBingPic(new WeatherCallback() {
            @Override
            public void Success(String json) {
                CacheUtils.putString(service, "bingPic", json);
            }

            @Override
            public void Failure(Exception e) {
                e.printStackTrace();
                L.e(e.getMessage());
            }
        });
    }

    private boolean updateWeather() {
        String cityName = CacheUtils.getString(service, "cityName");
        if (TextUtils.isEmpty(cityName)) {
            cityName = "北京";
        }
        String url = Constants.WEATHER_URL + cityName;
        weatherModel.getCityOrWeatherFromNet(url, new WeatherCallback() {
            @Override
            public void Success(String json) {
                CacheUtils.putString(service, "weather", json);
                updateSuccess = true;
            }

            @Override
            public void Failure(Exception e) {
                L.e(e.getMessage());
                updateSuccess = false;
            }
        });
        return updateSuccess;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isAuto = false;
        return super.onUnbind(intent);
    }
}
