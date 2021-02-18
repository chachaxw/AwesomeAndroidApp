package com.chacha.common.services;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.services
 * @date 2/18/21
 * @time 11:48 AM
 *
 * <p>
 *     与设备相关配置信息
 * </p>
 *
 */
public interface IDeviceService extends IProvider {
    String DEVICE_SERVICE_NAME = "device_service";

    /**
     * 获取设备默认语言
     */
    String getDefaultLanguage();

    /**
     * 获取ClientId
     *
     * @return 返回ClientId
     */
    String getClientId();

    /**
     * 获取当前设备安卓系统版本号
     */
    String getSystemVersion();

    /**
     * 获取手机品牌
     *
     * @return 返回手机品牌
     */
    String getPhoneBrand();

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return 返回API等级
     */
    int getBuildLevel();

    /**
     * 获取设备宽度（px）
     *
     * @return 返回设备宽度
     */
    int getDeviceWidth();

    /**
     * 获取设备高度（px）
     *
     * @return 返回设备高度
     */
    int getDeviceHeight();

    /**
     * SD卡判断
     *
     * @return SDK是否可用
     */
    boolean isSDCardAvailable();

    /**
     * 是否有网
     *
     * @return 网络状态
     */
    boolean isNetworkConnected();

    /**
     * 是否这次安装时新安装
     */
    boolean isNewInstall();
}
