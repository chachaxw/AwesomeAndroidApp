package com.chacha.common.config;

import com.chacha.base.base.BaseApplication;
import com.chacha.common.IModuleInit;

import androidx.annotation.Nullable;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.config
 * @date 2/18/21
 * @time 6:13 PM
 *
 * <p>
 *     作为组件生命周期初始化的配置类，通过反射机制，动态调用每个组件初始化逻辑
 * </p>
 *
 */
public class ModuleLifecycleConfig {
    public ModuleLifecycleConfig() {

    }

    private static class SingleHolder {
        private static final ModuleLifecycleConfig instance = new ModuleLifecycleConfig();
    }

    public static ModuleLifecycleConfig getInstance() {
        return SingleHolder.instance;
    }

    // 优先初始化
    private void initModuleAhead(@Nullable BaseApplication application) {
        for (String moduleName: ModuleLifecycleReflexes.initModuleNames) {
            try {
                Class<?> clazz = Class.forName(moduleName);
                IModuleInit init = (IModuleInit)clazz.newInstance();
                init.onInitAhead(application);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
