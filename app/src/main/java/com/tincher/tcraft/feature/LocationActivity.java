package com.tincher.tcraft.feature;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.IntRange;
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

public class LocationActivity extends BaseActivity implements LocationListener {
    private TextView tv_status;
    private Button   bt_is_gps_open;
    private TextView tv_location;

    private LocationUpdateProperty property;

    @Override
    protected String[] addCheckPermissions() {
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onAllPermissionsGrantedByUser() {
        // 首次获取权限时，需要重新注册，否则，因代码时序问题，无法定位
        LocationHelper.register(property, this);

    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_location;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView() {
        tv_status = findViewById(R.id.tv_status);
        bt_is_gps_open = findViewById(R.id.bt_is_gps_open);
        tv_location = findViewById(R.id.tv_location);

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

        property = new LocationUpdateProperty.Builder()
                .criteria(criteria)
                .mMinDistance(50)
                .mMinTime(5000).build();

        LocationHelper.register(property, this);

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
        LocationHelper.unRegister(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        tv_location.setText("Location: \n Longitude: " + location.getLongitude() + "\n Latitude: " + location.getLatitude());
//        LogUtils.e("Location: \n Longitude: " + location.getLongitude() + "\n Latitude: " + location.getLatitude());
//        String add = LocationHelper.getAddress(location.getLatitude(), location.getLongitude()).toString();
//        LogUtils.e(add);


//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        ObjectAnimator


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
}
