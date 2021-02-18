package com.chacha.common.services;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.chacha.common.contract.UserInfo;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.services
 * @date 2/18/21
 * @time 11:33 AM
 *
 * <p>
 *     登录相关
 * </p>
 *
 */
public interface ILoginService extends IProvider {
    String LOGIN_SERVICE_NAME = "login_service";

    /**
     * 保存登录状态
     *
     * @param status 登录状态
     * @return 返回当前登录状态
     */
    boolean saveStatus(boolean status);

    /**
     * 是否登录
     *
     * @return 返回是否登录状态
     */
    boolean isLogin();

    /**
     * 获取Token
     *
     * @return 返回Token
     */
    String getToken();

    /**
     * 获取UUID
     *
     * @return 返回UUID
     */
    String getUUID();

    /**
     * 刷新用户信息
     */
    void refreshUserInfo();

    /**
     * 登录
     */
    void login();

    /**
     * 是否以管理员身份登录
     * @param isAdmin 是否是管理员
     */
    void login(boolean isAdmin);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取用户信息
     *
     * @return 返回用户信息
     */
    UserInfo getUserInfo();

    /**
     * 取消登录
     */
    void onLoginCancel();

    /**
     * Token过期处理
     */
    void onTokenExpire();
}
