package com.chacha.common.config;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.recyclerview
 * @date 2/18/21
 * @time 5:36 PM
 *
 * <p>
 *     组件生命周期初始化类的类目管理者，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * </p>
 *
 */
public class ModuleLifecycleReflexes {
    // 公共库
    private static final String commonInit = "com.chacha.common.CommonModuleInit";

    public static String[] initModuleNames = {commonInit};
}
