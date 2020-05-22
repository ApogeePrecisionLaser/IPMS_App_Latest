package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class MyInternetConnection extends Application {
    private static MyInternetConnection mInstance;

    public static Context appcontext;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        mInstance = this;
        appcontext = getApplicationContext();

    }

    public static synchronized MyInternetConnection getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReciever.ConnectivityReceiverListener listener) {
        ConnectivityReciever.connectivityReceiverListener = listener;
    }

//    public ApiComponent getApiComponent(Context mContext) {
//
//        apiComponent = DaggerApiComponent.builder().appModule(new AppModule(mContext)).retrofitApiModule(new RetrofitApiModule(Constants.baseUrl())).build();
//
//        return apiComponent;
//    }
}
