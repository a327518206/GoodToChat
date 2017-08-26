package com.xiaoluogo.goodtochat.doman;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/18.
 * Email: angel-lwl@126.com
 */
public class CityBean {

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
        private String province;

        @SerializedName("city")
        private List<City> cities;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<City> getCities() {
            return cities;
        }

        public void setCities(List<City> cities) {
            this.cities = cities;
        }

        public static class City{
            @SerializedName("city")
            private String cityName;

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String city) {
                this.cityName = cityName;
            }
        }
    }

}
