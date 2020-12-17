package com.chacha.base.viewmodel;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.viewmodel
 * @date 12/17/20
 * @time 11:17 AM
 */

/**
 * 应用模块：ViewModel
 * <p>
 *     定义ViewModel 与 View 的关联
 * </p>
 */
public interface IMVVMBaseViewModel<V> {
    /**
     * 关联View
     */
    void attachUI(V view);

    /**
     * 获取View
     */
    V getPageView();

    /**
     * 是否已关联View
     */
    boolean isUIAttached();

    /**
     * 解除关联View
     */
    void detachUI();
}
