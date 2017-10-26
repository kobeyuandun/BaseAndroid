package com.baseandroid.gaode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baseandroid.config.Global;
import com.baseandroid.repository.json.MyLocation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GaoDeMap {

    public static GaoDeMap instance = null;

    // 获取CrashHandler实例 ,单例模式
    public static GaoDeMap getInstance() {
        if (instance == null) {
            instance = new GaoDeMap();
        }
        return instance;
    }

    public void init(final Application application) {

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            private int mCount = 0;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (mCount == 1) {
                    //进入前台
                    GaoDeMap.getInstance().initMap(application);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {
                    //进入后台
                    GaoDeMap.getInstance().stopMap();
                    GaoDeMap.getInstance().destroyMap();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public void initMap(Context context) {
        // 初始化定位
        mLocationClient = new AMapLocationClient(context);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        // 初始化AMapLocationClientOption对象
        // 高德定位服务包含GPS和网络定位（Wi-Fi和基站定位）两种能力
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式:
        // AMapLocationMode.Hight_Accuracy，高精度模式:会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息
        // AMapLocationMode.Battery_Saving，低功耗模式:不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）
        // AMapLocationMode.Device_Sensors，仅设备模式:不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

        // 获取一次定位结果：
        // 该方法默认为false表示使用默认的连续定位策略。
        mLocationOption.setOnceLocation(true);

        // 获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)
        // 接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        // false表示使用默认的连续定位策略
        mLocationOption.setOnceLocationLatest(true);

        // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        // mLocationOption.setInterval(1000);

        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        // 设置是否强制刷新WIFI，默认为true，强制刷新。
        // mLocationOption.setWifiActiveScan(false);

        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        // mLocationOption.setMockEnable(false);

        // 设置定位请求超时时间,单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        // 设置是否开启定位缓存机制,缓存机制默认开启。可以关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        // 启动定位,给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    // 声明定位回调监听器,用于接收异步返回的定位结果
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                // 当定位错误码类型为0时定位成功
                if (aMapLocation.getErrorCode() == 0) {
                    getMapLocation(aMapLocation);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因。
                    Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation
                            .getErrorInfo());
                }
            }
        }
    };

    public void getMapLocation(AMapLocation amapLocation) {
        MyLocation myLocation = new MyLocation();
        myLocation.setLatitude(amapLocation.getLatitude());
        myLocation.setLongitude(amapLocation.getLongitude());
        myLocation.setAddress(amapLocation.getAddress());
        myLocation.setProvinceName(amapLocation.getProvince());
        myLocation.setCityName(amapLocation.getCity());
        //Global.setMyLocation(myLocation);

        Log.e("GaodeMap", "type = " + amapLocation.getLocationType());//获取当前定位结果来源,如网络定位结果.
        Log.e("GaodeMap", "latitude=" + amapLocation.getLatitude());//获取纬度
        Log.e("GaodeMap", "longitude=" + amapLocation.getLongitude());//获取经度
        Log.e("GaodeMap", "accuracy = " + amapLocation.getAccuracy());//获取定位精度 单位:米
        Log.e("GaodeMap", "" + amapLocation.getAddress());//地址，网络定位结果中会有地址信息，GPS定位不返回地址信息
        Log.e("GaodeMap", "" + amapLocation.getCountry());//国家信息
        Log.e("GaodeMap", "" + amapLocation.getProvince());//省信息
        Log.e("GaodeMap", "" + amapLocation.getCity());//城市信息
        Log.e("GaodeMap", "" + amapLocation.getDistrict());//城区信息
        Log.e("GaodeMap", "" + amapLocation.getStreet());//街道信息
        Log.e("GaodeMap", "" + amapLocation.getStreetNum());//街道门牌号信息
        Log.e("GaodeMap", "" + amapLocation.getCityCode());//城市编码
        Log.e("GaodeMap", "" + amapLocation.getAdCode());//地区编码
        Log.e("GaodeMap", "" + amapLocation.getPoiName());//获取当前定位点的POI信息
        //获取定位时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(amapLocation.getTime());
        Log.e("GaodeMap", "" + df.format(date));
    }

    public static void getBitmapLocation(String filepath, final LocationResult locationResult) {
        try {
            Location location;
            float output1 = 0.0f;
            float output2 = 0.0f;
            ExifInterface exifInterface = new ExifInterface(filepath);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                output1 = convertRationalLatLonToFloat(latValue, latRef);
                output2 = convertRationalLatLonToFloat(lngValue, lngRef);
            }

            location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(output1);
            location.setLongitude(output2);

            GeocodeSearch geocoderSearch = new GeocodeSearch(Global.getContext());
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

                @Override
                public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                    if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result != null && result.getRegeocodeAddress() != null && result
                                .getRegeocodeAddress()
                                .getFormatAddress() != null) {
                            if (locationResult != null) {
                                locationResult.onLocationResult(result.getRegeocodeAddress()
                                        .getFormatAddress());
                            }
                        } else {
                            locationResult.onLocationResult("");
                        }
                    } else {
                        locationResult.onLocationResult("");
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult result, int rCode) {

                }
            });

            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(location.getLatitude(), location
                    .getLongitude()), 360, GeocodeSearch.GPS);
            geocoderSearch.getFromLocationAsyn(query);
        } catch (Exception e) {
            locationResult.onLocationResult("");
        }
    }

    public static float convertRationalLatLonToFloat(String rationalString, String ref) throws
            Exception {
        String[] parts = rationalString.split(",");
        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
        }

        return (float) result;
    }

    public interface LocationResult {
        void onLocationResult(String addressName);
    }

    public void stopMap() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    public void destroyMap() {
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

}
