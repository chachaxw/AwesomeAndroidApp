package com.chacha.common.services;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.services
 * @date 2/18/21
 * @time 11:09 AM
 *
 * <p>
 *     App设置相关
 * </p>
 */
public interface ISettingService extends IProvider {
    String SETTINGS_SERVICE_NAME = "settings_service";

    // 语言
    int CODE_LANGUAGE = 1;

    // 主题
    int CODE_THEME = 2;

    // 字体
    int CODE_FONT_SCHEME = 3;

    int getThemeValue();

    void setThemeValue(String theme);
}
