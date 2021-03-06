package com.dkaishu.tcraftlib.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.RequiresPermission;

import com.dkaishu.tcraftlib.app.AppContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 定位以获取坐标
 * Created by dks on 2018/10/31.
 */

public class LocationHelper {
    @SuppressLint("StaticFieldLeak")
    private static Context                                    mContext;
    private static HashMap<LocationListener, LocationManager> mRegister = new HashMap<>();

    private static long  mMinTime     = LocationUpdateProperty.MIN_TIME_MILLISECOND_DEF;//最短间隔时间
    private static float mMinDistance = LocationUpdateProperty.MIN_DISTANCE_METER_DEF;//最短触发距离

    private LocationHelper() {
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public static void register(LocationUpdateProperty property, LocationListener listener) {
        if (property == null) {
            register(listener);
            return;
        }

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            List<String> providers = locationManager.getProviders(property.getCriteria(), true);
            for (String provider : providers) {// 无权限时，providers是空的
                locationManager.requestLocationUpdates(provider, property.getmMinTime(), property.getmMinDistance(), listener);
            }
            mRegister.put(listener, locationManager);
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public static void register(LocationListener listener) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置定位精准度
        criteria.setAltitudeRequired(false);//是否要求海拔
        criteria.setBearingRequired(false);//是否要求方向
        criteria.setCostAllowed(true);//是否要求收费
        criteria.setSpeedRequired(false);//是否要求速度
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);//设置电池耗电要求
        criteria.setBearingAccuracy(Criteria.NO_REQUIREMENT);//设置方向精确度
        criteria.setSpeedAccuracy(Criteria.NO_REQUIREMENT);//设置速度精确度
        criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);//设置水平方向精确度
        criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);//设置垂直方向精确度

        if (locationManager != null) {
            //TODO GPS 可用与否的判断
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinTime, mMinDistance, listener);
            List<String> providers = locationManager.getProviders(criteria, true);
            for (String provider : providers) {
                locationManager.requestLocationUpdates(provider, mMinTime, mMinDistance, listener);
            }
            mRegister.put(listener, locationManager);
        }

    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION})
    public static void registerOnlyGPS(LocationListener listener) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinTime, mMinDistance, listener);
            mRegister.put(listener, locationManager);
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION})
    public static void registerOnlyGPS(long minTime, float minDistance, LocationListener listener) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
            mRegister.put(listener, locationManager);
        }
    }

    public static void unRegister(LocationListener listener) {
        if (mRegister.isEmpty()) {
            return;
        }
        mRegister.get(listener).removeUpdates(listener);
        mRegister.remove(listener);

    }


    public static Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(AppContext.getsAppContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     */
    public static String getCountryName(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     */
    public static String getLocality(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     */
    public static String getStreet(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }


}
