package com.chacha.common.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.adapter
 * @date 2/19/21
 * @time 11:17 PM
 *
 * <p>
 * 屏幕适配:
 * 1. 首先在application中的setup()方法初始化一下
 * 2. 手动在Activity中调用match()方法做适配，必须在setContentView()之前
 * 3. 建议使用dp做宽度适配，大多数时候宽度适配才是主流需要
 * 4. 在写布局的时候，可以多用dp，如果是使用px，建议转化成dp
 * 5. 入侵性很低，不需要改动原来的代码
 * </p>
 * @see <a href="https://zhuanlan.zhihu.com/p/58092930">Android开发之显示篇（弄懂ppi、dpi、pt、px、dp、dip、sp之间的关系）</a>
 */
public class ScreenAutoAdapter {
    // 屏幕适配基准
    private static final int MATCH_BASE_WIDTH = 0;

    private static final int MATCH_BASE_HEIGHT = 1;

    // 适配单位
    private static final int MATCH_UNIT_DP = 0;

    private static final int MATCH_UNIT_PT = 1;

    // 适配信息
    private static MatchInfo matchInfo;

    // Activity 生命周期检测
    private static Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    public static void setup(@NonNull final Application application) {
        /*
          <p>
              获取屏幕分辨率信息的三种方法：
              1. DisplayMetrics metrics = new DisplayMetrics();
                 Display display = activity.getWindowManager().getDefaultDisplay();
                 display.getMetrics(metrics);
              2. DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
              3. Resources.getSystem().getDisplayMetrics();
          </p>
         */
        // 获取系统的display metrics
        DisplayMetrics metrics = application.getResources().getDisplayMetrics();

        if (matchInfo == null) {
            matchInfo = new MatchInfo();

            matchInfo.setScreenWidth(metrics.widthPixels);
            matchInfo.setScreenHeight(metrics.heightPixels);
            matchInfo.setAppDensity(metrics.density);
            matchInfo.setAppDensityDpi(metrics.densityDpi);
            matchInfo.setAppScaledDensity(metrics.scaledDensity);
            matchInfo.setAppXdpi(metrics.xdpi);
        }

        // 添加字体变化的监听
        // 调用 Application.registerComponentCallbacks 注册 onConfigurationChanged 监听即可
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(@NonNull Configuration configuration) {
                // 字体改变后，将appScaledDensity重新赋值
                if (configuration != null && configuration.fontScale > 0) {
                    float scaledDensity = metrics.scaledDensity;
                    matchInfo.setAppScaledDensity(scaledDensity);
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
    }

    // 在 application 中全局激活适配，也可以单独使用 match() 方法在指定页面中配置适配。
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void register(@NonNull final Application application, final float designSize, final int matchBase, final int matchUnit) {
        if (activityLifecycleCallbacks == null) {
            activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                    if (activity != null) {
                        match(activity, designSize, matchBase, matchUnit);
                    }
                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {

                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {

                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {

                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {

                }
            };

            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    /**
     * 全局取消所有的适配
     *
     * @param application Application
     * @param matchUnit 适配单位
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void unregister(@NonNull final Application application, @NonNull int... matchUnit) {
        if (activityLifecycleCallbacks != null) {
            application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            activityLifecycleCallbacks = null;
        }

        for (int unit : matchUnit) {
            cancelMatch(application, unit);
        }
    }

    /**
     * 适配屏幕，放在 Activity 的 setContentView() 之前执行
     *
     * @param context 上下文
     * @param designSize 设计图尺寸
     */
    public static void match(@NonNull final Context context, final float designSize) {
        match(context, designSize, MATCH_BASE_WIDTH, MATCH_UNIT_DP);
    }

    /**
     * 适配屏幕，放在 Activity 的 setContentView() 之前执行
     *
     * @param context 上下文
     * @param designSize 设计图尺寸
     * @param matchBase 适配基准
     */
    public static void match(@NonNull final Context context, final float designSize, int matchBase) {
        match(context, designSize, matchBase, MATCH_UNIT_DP);
    }

    /**
     * 适配屏幕，放在 Activity 的 setContentView() 之前执行
     *
     * @param context 上下文
     * @param designSize 设计图的尺寸
     * @param matchBase 适配基准
     * @param matchUnit 使用的适配单位
     */
    public static void match(@NonNull final Context context, final float designSize, int matchBase, int matchUnit) {
        if (designSize == 0) {
            throw new UnsupportedOperationException("The designSize cannot be equal to 0");
        }

        if (matchUnit == MATCH_UNIT_DP) {
            matchByDp(context, designSize, matchBase);
        }

        if (matchUnit == MATCH_UNIT_PT) {
            matchByPt(context, designSize, matchBase);
        }
    }

    /**
     *
     * 使用 dp 作为适配单位（适合在新项目中使用，在老项目中使用会对原来既有的 dp 值产生影响）
     * <ul>
     *     dp 与 px 之间的换算：
     *     <li>px = density * dp</li>
     *     <li>density = dpi / 160</li>
     *     <li>px = dp * (dpi / 160)</li>
     * </ul>
     *
     * @param context 上下文
     * @param designSize 设计图的宽/高（单位: dp）
     * @param base 适配基准
     */
    private static void matchByDp(@NonNull final Context context, final float designSize, int base) {
        final float targetDensity;

        if (base == MATCH_BASE_WIDTH) {
            targetDensity = matchInfo.getScreenWidth() * 1f / designSize;
        } else if (base == MATCH_BASE_HEIGHT) {
            targetDensity = matchInfo.getScreenHeight() * 1f / designSize;
        } else {
            targetDensity = matchInfo.getScreenWidth() * 1f / designSize;
        }

        final int targetDensityDpi = (int)(targetDensity * 160);
        final float targetScaledDensity = targetDensity * (matchInfo.getAppScaledDensity() / matchInfo.getAppDensity());
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        metrics.density = targetDensity;
        metrics.densityDpi = targetDensityDpi;
        metrics.scaledDensity = targetScaledDensity;
    }

    /**
     * 使用 pt 作为适配单位（1pt＝1/72 inch）
     *
     * <p>
     *     pt 转 px 算法：pt * metrics.xdpi * (1f / 72)
     * </p>
     *
     * @param context 上下文
     * @param designSize 设计图的宽/高（单位: dp）
     * @param base 适配基准
     */
    private static void matchByPt(@NonNull final Context context, final float designSize, int base) {
        final float targetXdpi;

        if (base == MATCH_BASE_WIDTH) {
            targetXdpi = matchInfo.getScreenWidth() * 72f / designSize;
        } else if (base == MATCH_BASE_HEIGHT) {
            targetXdpi = matchInfo.getScreenHeight() * 72f / designSize;
        } else {
            targetXdpi = matchInfo.getScreenWidth() * 72f / designSize;
        }

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        metrics.xdpi = targetXdpi;
    }

    /**
     * 重置适配信息，取消适配
     *
     * @param context 上下文
     */
    public static void cancelMatch(@NonNull final Context context) {
        cancelMatch(context, MATCH_UNIT_DP);
        cancelMatch(context, MATCH_UNIT_PT);
    }

    /**
     * 重置适配信息，取消适配
     *
     * @param context 上下文
     * @param matchUnit 需要取消适配的单位
     */
    private static void cancelMatch(@NonNull final Context context, int matchUnit) {
        if (matchInfo != null) {
            final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

            if (matchUnit == MATCH_UNIT_DP) {
                if (metrics.density != matchInfo.getAppDensity()) {
                    metrics.density = matchInfo.getAppDensity();
                }

                if (metrics.densityDpi != matchInfo.getAppDensityDpi()) {
                    metrics.densityDpi = (int)matchInfo.getAppDensityDpi();
                }

                if (metrics.scaledDensity != matchInfo.getAppScaledDensity()) {
                    metrics.scaledDensity = matchInfo.getAppScaledDensity();
                }
            } else if (matchUnit == MATCH_UNIT_PT) {
                if (metrics.xdpi != matchInfo.getAppXdpi()) {
                    metrics.xdpi = matchInfo.getAppXdpi();
                }
            }
        }
    }

    public static MatchInfo getMatchInfo() {
        return matchInfo;
    }

    private static class MatchInfo {
        private int screenWidth;

        private int screenHeight;

        private float appDensity;

        private float appDensityDpi;

        private float appScaledDensity;

        private float appXdpi;

        int getScreenWidth() {
            return screenWidth;
        }

        void setScreenWidth(int screenWidth) {
            this.screenWidth = screenWidth;
        }

        int getScreenHeight() {
            return screenHeight;
        }

        void setScreenHeight(int screenHeight) {
            this.screenHeight = screenHeight;
        }

        float getAppDensity() {
            return appDensity;
        }

        void setAppDensity(float density) {
            this.appDensity = density;
        }

        float getAppDensityDpi() {
            return appDensityDpi;
        }

        void setAppDensityDpi(float densityDpi) {
            this.appDensityDpi = densityDpi;
        }

        float getAppScaledDensity() {
            return appScaledDensity;
        }

        void setAppScaledDensity(float scaledDensity) {
            this.appScaledDensity = scaledDensity;
        }

        float getAppXdpi() {
            return appXdpi;
        }

        void setAppXdpi(float xdpi) {
            this.appXdpi = xdpi;
        }
    }
}
