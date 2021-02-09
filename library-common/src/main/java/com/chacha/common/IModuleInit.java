package com.chacha.common;

import com.chacha.base.base.BaseApplication;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common
 * @date 2/9/21
 * @time 5:47 PM
 *
 * <p>
 *     动态配置组件Application，有需要初始化的组件实现该接口，统一在宿主App的Application进行初始化
 * </p>
 */
public interface IModuleInit {
    // 需要优先初始化的
    boolean onInitAhead(BaseApplication application);

    // 可以后初始化的
    boolean onInitBehind(BaseApplication application);
}
