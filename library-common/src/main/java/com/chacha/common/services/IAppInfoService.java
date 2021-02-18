package com.chacha.common.services;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.services
 * @date 2/18/21
 * @time 1:47 PM
 *
 * <p>
 *     App相关信息
 * </p>
 *
 */
public interface IAppInfoService extends IProvider {
    String APP_INFO_SERVICE_NAME = "app_info_service";

    String getApplicationName();

    String getApplicationVersionName();

    String getApplicationVersionCode();

    boolean getApplicationDebug();
}
