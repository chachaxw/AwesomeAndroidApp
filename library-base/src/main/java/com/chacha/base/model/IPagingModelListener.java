package com.chacha.base.model;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.model
 * @date 12/22/20
 * @time 2:03 PM
 */
public interface IPagingModelListener<T>  extends IBaseModelListener {
    /**
     * 数据加载完成
     *
     * @param model model
     * @param data 数据
     * @param isEmpty 数据是否为空
     * @param isFirstPage 是否是第一页
     */
    void onLoadFinish(BasePagingModel<T> model, T data, boolean isEmpty, boolean isFirstPage);

    /**
     * 数据加载失败
     *
     * @param model model
     * @param prompt 错误
     * @param isFirstPage 是否是第一页
     */
    void onLoadFail(BasePagingModel<T> model, String prompt, boolean isFirstPage);
}
