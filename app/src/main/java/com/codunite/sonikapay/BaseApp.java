package com.codunite.sonikapay;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.codunite.commonutility.GlobalData;
import com.codunite.commonutility.PreferenceConnector;

public class BaseApp extends Application {
    private static final int SCHEMA_VERSION = 0;
    public static final String TAG = BaseApp.class.getSimpleName();
    private static BaseApp mInstance;

    public static BaseApp getInstance(Context context) {
        return (BaseApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceConnector.writeString(this, PreferenceConnector.DEVICE_ID, GlobalData.getDeviceId(this));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
