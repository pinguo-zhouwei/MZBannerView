package com.zhouwei.mzbannerview;

import android.app.Application;

/**
 * Created by zhouwei on 17/6/10.
 */

public class MZApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       //ANR检测
       // new ANRWatchDog().start();
    }
}
