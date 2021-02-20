package com.chacha.common;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chacha.base.base.BaseApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import androidx.annotation.Nullable;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common
 * @date 2/9/21
 * @time 6:14 PM
 *
 * <p>
 *     通用库和基础库自身初始化操作
 * </p>
 *
 */
public class CommonModuleInit implements IModuleInit {

    @Override
    public void onInitAhead(BaseApplication application) {
        // 初始化日志库
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return application.isDebug();
            }
        });

        // ARouter 使用说明(https://github.com/alibaba/ARouter)
        if (application.isDebug()) {
            ARouter.openLog(); // ARouter开启日志
            ARouter.openDebug(); // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
        }

        ARouter.init(application);
        MMKV.initialize(application);
        Logger.i("基础层初始化完毕 -- onInitAhead");
    }

    @Override
    public boolean onInitBehind(BaseApplication application) {
        return false;
    }
}
