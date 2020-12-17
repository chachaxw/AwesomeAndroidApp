package com.chacha.base.activity;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.activity
 * @date 12/16/20
 * @time 11:42 PM
 */
public interface IBaseView {
    /**
     * 显示内容
     */
    void showContent();

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 显示空白页面
     */
    void showEmpty();

    /**
     * 显示错误页面
     */
    void showError(String message);
}
