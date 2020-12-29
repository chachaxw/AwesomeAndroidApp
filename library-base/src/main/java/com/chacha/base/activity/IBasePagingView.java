package com.chacha.base.activity;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.activity
 * @date 12/22/20
 * @time 2:24 PM
 */
public interface IBasePagingView extends IBaseView {
    /**
     * 加载更多失败
     */
    void onLoadMoreFailure(String message);

    /**
     * 没有更多了
     */
    void onLoadMoreEmpty();
}
