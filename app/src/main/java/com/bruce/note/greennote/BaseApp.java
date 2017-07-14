package com.bruce.note.greennote;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by liq on 2017/5/23.
 */

public class BaseApp extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Timber.plant(new Timber.DebugTree());
    }
}
