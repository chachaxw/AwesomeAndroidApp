package com.chacha.base.model;

import java.lang.ref.WeakReference;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.model
 * @date 12/22/20
 * @time 2:04 PM
 *
 * <p>
 *     适用于需要加载分页的model
 * </p>
 */
public abstract class BasePagingModel<T> extends SuperBaseModel<T> {
    /**
     * 是否还有下一页
     */
    protected boolean hasNextPage = false;

    /**
     * 下一页的URL
     */
    protected String nextPageUrl = "";

    /**
     * 上拉刷新 true or 下拉加载 false
     */
    protected boolean isRefresh = true;


    /**
     * 数据加载成功
     *
     * @param data 数据
     * @param isEmpty 数据是否为空
     * @param isFirstPage 当前是否是第一页
     */
    public void loadSuccess(T data, boolean isEmpty, boolean isFirstPage) {
        synchronized (this) {
            uiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : weakReferenceDeque) {
                    if (weakListener.get() instanceof IPagingModelListener) {
                        IPagingModelListener<T> item = (IPagingModelListener<T>)weakListener.get();

                        if (item != null) {
                            item.onLoadFinish(BasePagingModel.this, data, isEmpty, isFirstPage);
                        }
                    }
                }
            }, 0);
        }
    }

    /**
     *
     * @param prompt 错误信息
     * @param isFirstPage 是否是第一页
     */
    public void loadFail(String prompt, boolean isFirstPage) {
        synchronized (this) {
            uiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : weakReferenceDeque) {
                    if (weakListener.get() instanceof IPagingModelListener) {
                        IPagingModelListener<T> item = (IPagingModelListener<T>) weakListener.get();

                        if (item != null) {
                            item.onLoadFail(BasePagingModel.this, prompt, isFirstPage);
                        }
                    }
                }
            }, 0);
        }
    }

    @Override
    protected void notifyCacheData(T data) {
        loadSuccess(data, false, true);
    }
}
