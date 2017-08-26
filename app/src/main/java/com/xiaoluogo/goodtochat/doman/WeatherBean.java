package com.xiaoluogo.goodtochat.doman;

import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/19.
 * Email: angel-lwl@126.com
 */
public class WeatherBean {

    /**
     * msg : success
     * result : [{"airCondition":"优","city":"赤峰","coldIndex":"低发期","date":"2017-08-19","distrct":"赤峰","dressingIndex":"单衣类","exerciseIndex":"适宜","future":[{"date":"2017-08-19","dayTime":"雷阵雨","night":"多云","temperature":"25°C / 17°C","week":"今天","wind":"东南风 小于3级"},{"date":"2017-08-20","dayTime":"多云","night":"晴","temperature":"28°C / 19°C","week":"星期日","wind":"南风 小于3级"},{"date":"2017-08-21","dayTime":"多云","night":"多云","temperature":"30°C / 19°C","week":"星期一","wind":"东风 小于3级"},{"date":"2017-08-22","dayTime":"中雨","night":"中雨","temperature":"27°C / 19°C","week":"星期二","wind":"东南风 小于3级"},{"date":"2017-08-23","dayTime":"多云","night":"多云","temperature":"27°C / 15°C","week":"星期三","wind":"西南风 小于3级"},{"date":"2017-08-24","dayTime":"小雨","night":"多云","temperature":"23°C / 13°C","week":"星期四","wind":"西北风 小于3级"},{"date":"2017-08-25","dayTime":"阴","night":"多云","temperature":"27°C / 11°C","week":"星期五","wind":"西北风 小于3级"},{"date":"2017-08-26","dayTime":"少云","night":"少云","temperature":"26°C / 14°C","week":"星期六","wind":"西风 3级"},{"date":"2017-08-27","dayTime":"晴","night":"晴","temperature":"24°C / 14°C","week":"星期日","wind":"西北偏西风 3级"},{"date":"2017-08-28","dayTime":"晴","night":"少云","temperature":"24°C / 13°C","week":"星期一","wind":"西北偏北风 3级"}],"humidity":"湿度：68%","pollutionIndex":"44","province":"内蒙古","sunrise":"05:16","sunset":"19:00","temperature":"25℃","time":"13:50","updateTime":"20170819140704","washIndex":"不适宜","weather":"晴","week":"周六","wind":"东北风2级"}]
     * retCode : 200
     */

    private String msg;
    private String retCode;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * airCondition : 优
         * city : 赤峰
         * coldIndex : 低发期
         * date : 2017-08-19
         * distrct : 赤峰
         * dressingIndex : 单衣类
         * exerciseIndex : 适宜
         * future : [{"date":"2017-08-19","dayTime":"雷阵雨","night":"多云","temperature":"25°C / 17°C","week":"今天","wind":"东南风 小于3级"},{"date":"2017-08-20","dayTime":"多云","night":"晴","temperature":"28°C / 19°C","week":"星期日","wind":"南风 小于3级"},{"date":"2017-08-21","dayTime":"多云","night":"多云","temperature":"30°C / 19°C","week":"星期一","wind":"东风 小于3级"},{"date":"2017-08-22","dayTime":"中雨","night":"中雨","temperature":"27°C / 19°C","week":"星期二","wind":"东南风 小于3级"},{"date":"2017-08-23","dayTime":"多云","night":"多云","temperature":"27°C / 15°C","week":"星期三","wind":"西南风 小于3级"},{"date":"2017-08-24","dayTime":"小雨","night":"多云","temperature":"23°C / 13°C","week":"星期四","wind":"西北风 小于3级"},{"date":"2017-08-25","dayTime":"阴","night":"多云","temperature":"27°C / 11°C","week":"星期五","wind":"西北风 小于3级"},{"date":"2017-08-26","dayTime":"少云","night":"少云","temperature":"26°C / 14°C","week":"星期六","wind":"西风 3级"},{"date":"2017-08-27","dayTime":"晴","night":"晴","temperature":"24°C / 14°C","week":"星期日","wind":"西北偏西风 3级"},{"date":"2017-08-28","dayTime":"晴","night":"少云","temperature":"24°C / 13°C","week":"星期一","wind":"西北偏北风 3级"}]
         * humidity : 湿度：68%
         * pollutionIndex : 44
         * province : 内蒙古
         * sunrise : 05:16
         * sunset : 19:00
         * temperature : 25℃
         * time : 13:50
         * updateTime : 20170819140704
         * washIndex : 不适宜
         * weather : 晴
         * week : 周六
         * wind : 东北风2级
         */

        private String airCondition;
        private String city;
        private String coldIndex;
        private String date;
        private String distrct;
        private String dressingIndex;
        private String exerciseIndex;
        private String humidity;
        private String pollutionIndex;
        private String province;
        private String sunrise;
        private String sunset;
        private String temperature;
        private String time;
        private String updateTime;
        private String washIndex;
        private String weather;
        private String week;
        private String wind;
        private List<FutureBean> future;

        public String getAirCondition() {
            return airCondition;
        }

        public void setAirCondition(String airCondition) {
            this.airCondition = airCondition;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getColdIndex() {
            return coldIndex;
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistrct() {
            return distrct;
        }

        public void setDistrct(String distrct) {
            this.distrct = distrct;
        }

        public String getDressingIndex() {
            return dressingIndex;
        }

        public void setDressingIndex(String dressingIndex) {
            this.dressingIndex = dressingIndex;
        }

        public String getExerciseIndex() {
            return exerciseIndex;
        }

        public void setExerciseIndex(String exerciseIndex) {
            this.exerciseIndex = exerciseIndex;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPollutionIndex() {
            return pollutionIndex;
        }

        public void setPollutionIndex(String pollutionIndex) {
            this.pollutionIndex = pollutionIndex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWashIndex() {
            return washIndex;
        }

        public void setWashIndex(String washIndex) {
            this.washIndex = washIndex;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public List<FutureBean> getFuture() {
            return future;
        }

        public void setFuture(List<FutureBean> future) {
            this.future = future;
        }

        public static class FutureBean {
            /**
             * date : 2017-08-19
             * dayTime : 雷阵雨
             * night : 多云
             * temperature : 25°C / 17°C
             * week : 今天
             * wind : 东南风 小于3级
             */

            private String date;
            private String dayTime;
            private String night;
            private String temperature;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDayTime() {
                return dayTime;
            }

            public void setDayTime(String dayTime) {
                this.dayTime = dayTime;
            }

            public String getNight() {
                return night;
            }

            public void setNight(String night) {
                this.night = night;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }
        }
    }
}
