package com.chacha.base.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.base
 * @date 12/16/20
 * @time 5:30 PM
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;
    private static boolean isDebug = false;
    private static final String TAG = BaseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
    }

    /**
     * 当宿主没有继承自该 Application 时,可以使用set方法进行初始化 BaseApplication
     * @param application BaseApplication类实例
     */
    private void setApplication(@NonNull BaseApplication application) {
        instance = application;

        // 使用ActivityLifecycleCallbacks类，我们可以很方便的监听到 Activity 的状态，从而可以判断 APP 是否在前台或者后台等
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                Log.i(TAG, "onActivityCreated()");
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.i(TAG, "onActivityStarted()");
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.i(TAG, "onActivityResumed()");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.i(TAG, "onActivityPaused()");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.i(TAG, "onActivityStopped()");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
                Log.i(TAG, "onActivitySaveInstanceState()");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.i(TAG, "onActivityDestroyed()");
            }
        });
    }

    /**
     * 获得当前app运行的Application
     */
    public static BaseApplication getInstance() {
        if (instance == null) {
            throw new NullPointerException("Please inherit BaseApplication or call setApplication.");
        }
        return instance;
    }

    /**
     * 是否开启debug模式
     * @param debug
     */
    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();

        if (runningApps == null) return null;

        for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
            if (processInfo.pid == android.os.Process.myPid()) {
                if (processInfo.processName != null) {
                    return processInfo.processName;
                }
            }
        }

        return null;
    }
}
