package com.tincher.tcraft.feature;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.tincher.tcraft.R;
import com.tincher.tcraftlib.base.BaseActivity;
import com.tincher.tcraftlib.location.LocationHelper;
import com.tincher.tcraftlib.location.LocationUpdateProperty;

/**
 * Created by dks on 2018/10/31.
 */

public class LocationActivity extends BaseActivity {
    private LocationListener listener;

    @Override
    protected String[] addCheckPermissions() {
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_location;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        final TextView tv_status      = findViewById(R.id.tv_status);
        final Button   bt_is_gps_open = findViewById(R.id.bt_is_gps_open);
        final TextView tv_location    = findViewById(R.id.tv_location);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tv_location.setText("Location: \n Longitude: " + location.getLongitude() + "\n Latitude: " + location.getLatitude());
                LogUtils.e("Location: \n Longitude: " + location.getLongitude() + "\n Latitude: " + location.getLatitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE: {
                        tv_status.setText("OUT_OF_SERVICE");
                    }
                    case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                        tv_status.setText("TEMPORARILY_UNAVAILABLE");

                    }
                    case LocationProvider.AVAILABLE: {
                        tv_status.setText("AVAILABLE");

                    }
                }
                LogUtils.e("provider: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                LogUtils.e("onProviderEnabled: " + provider);

            }

            @Override
            public void onProviderDisabled(String provider) {
                LogUtils.e("onProviderDisabled: " + provider);

            }
        };
//        LocationHelper.register(listener);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置定位精准度
        criteria.setAltitudeRequired(false);//是否要求海拔
        criteria.setBearingRequired(false);//是否要求方向
        criteria.setCostAllowed(false);//是否要求收费
        criteria.setSpeedRequired(false);//是否要求速度
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);//设置电池耗电要求
        criteria.setBearingAccuracy(Criteria.NO_REQUIREMENT);//设置方向精确度
        criteria.setSpeedAccuracy(Criteria.NO_REQUIREMENT);//设置速度精确度
        criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);//设置水平方向精确度
        criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);//设置垂直方向精确度

        LocationUpdateProperty property = new LocationUpdateProperty.Builder()
                .criteria(criteria)
                .mMinDistance(50)
                .mMinTime(2000).build();

        LocationHelper.register(property,listener);

        bt_is_gps_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager
                        = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    bt_is_gps_open.setText("opened");
                } else {
                    bt_is_gps_open.setText("closed");
                }

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null) {
            LocationHelper.unRegister(listener);
            listener = null;
        }
    }
}
