package com.tincher.tcraft.feature.main;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.LogUtils;
import com.tincher.tcraft.R;
import com.tincher.tcraft.data.MyApiService;
import com.tincher.tcraftlib.base.BaseHttpActivity;
import com.tincher.tcraftlib.network.RetrofitClient;
import com.tincher.tcraftlib.network.TincherInterceptorCallback;
import com.tincher.tcraftlib.network.networkstatus.NetInfo;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class MainActivity extends BaseHttpActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    public LocationClient mLocationClient = null;

    @Override
    protected void initView() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                LogUtils.e(bdLocation.getAltitude());
                LogUtils.e(bdLocation.getAddress().city);
//                TextView tv1 = findViewById(R.id.tv_1);
//                tv1.setText(bdLocation.describeContents());
            }
        });
        mLocationClient.registerNotify(new BDNotifyListener() {
        });


        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.restart();
    }

//    private final LifecycleProvider<ActivityEvent> provider
//            = NaviLifecycle.createActivityLifecycleProvider(this);

    //    private final LifecycleProvider<Lifecycle.Event> provider
//            = AndroidLifecycle.createLifecycleProvider(this);
    @Override
    protected void initData() {
        RetrofitClient.getInstance().createService(MyApiService.TestService.class, null, new TincherInterceptorCallback() {
            @Override
            public ResponseBody OnResponse(ResponseBody responseBody) {
                byte[]       bytes;
                MediaType    mediaType;
                ResponseBody newRb = null;
                try {
                    bytes = responseBody.bytes();
                    mediaType = responseBody.contentType();
                    newRb = ResponseBody.create(mediaType, bytes);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException： ");

                }
                Log.e(TAG, "length： ");

                return newRb;
            }
        })
                .getTopMovie()

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String movie) {
                        Log.d(TAG, "onNext: " + movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                    }
                });

    }


    @Override
    protected void onNetworkStateChanged(boolean isNetworkAvailable, NetInfo netInfo) {

    }
}
