package com.chacha.common.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chacha.common.router.RouterActivityPath;
import com.chacha.common.services.ILoginService;
import com.orhanobut.logger.Logger;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.interceptor
 * @date 2/18/21
 * @time 4:29 PM
 *
 * <p>
 *     登录拦截器
 * </p>
 *
 */
public class LoginInterceptor implements IInterceptor {
    private Context context;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getPath().equals(RouterActivityPath.User.PAGER_ATTENTION)) { // 如果当前页面路径为PAGER_ATTENTION
            ILoginService iLoginService = ARouter.getInstance().navigation(ILoginService.class);

            if (iLoginService.isLogin()) {
                // 如果App已经登录，处理完成交还控制权
                callback.onContinue(postcard);
            } else { // 否则跳转到登录界面
                ARouter.getInstance().build(RouterActivityPath.User.PAGER_LOGIN).greenChannel().navigation(context);
                callback.onInterrupt(null);
            }
        } else {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        this.context = context;
        Logger.d("登录拦截器被初始化了");
    }
}
