package com.chacha.base.model;

import java.lang.ref.WeakReference;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.model
 * @date 12/17/20
 * @time 4:35 PM
 */
public abstract class BaseModel<T> extends SuperBaseModel<T> {
    /**
     * 网络数据加载成功，通知所有注册者加载数据
     *
     * @param data 数据bean
     */
    public void loadSuccess(T data) {

        /**
         * synchronized（用来修饰方法或者代码块）
         * 1. synchronized 是同步锁，用来实现互斥同步。
         * 2. 关键字synchronized 可以保证在同一个时刻，只有一个线程可以执行某个方法或者某个代码块（主要是对方法或者代码块中存在共享数据的操作）
         * 3. synchronized 还可以保证一个线程的变化（主要是共享数据的变化）被其他线程所看到
         */
        synchronized (this) {
            uiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : weakReferenceDeque) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener<T> item = (IModelListener<T>)weakListener.get();

                        if (item != null) {
                            item.onLoadFinish(BaseModel.this, data);
                        }
                    }
                }
            }, 0);
        }
    }

    /**
     * 加载数据失败，通知所有注册者
     */
    protected void loadFail(String prompt) {
        synchronized (this) {
            uiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener: weakReferenceDeque) {
                    if (weakListener.get() instanceof IModelListener) {
                        IModelListener<T> item = (IModelListener<T>)weakListener.get();

                        if (item != null) {
                            item.onLoadFail(BaseModel.this, prompt);
                        }
                    }
                }
            }, 0);
        }
    }

    @Override
    protected void notifyCacheData(T data) {
        loadSuccess(data);
    }
}
