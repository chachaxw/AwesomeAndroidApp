package com.chacha.base.model;

import android.os.Looper;
import android.os.Handler;

import com.chacha.base.utils.GsonUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.model
 * @date 12/17/20
 * @time 12:54 PM
 *
 * * 应用模块: model
 * <p>
 *     基类抽象model
 * <p>
 */
public abstract class SuperBaseModel<T> {
    protected CompositeDisposable compositeDisposable;

    /**
     * Android Handler机制(https://segmentfault.com/a/1190000022843928)
     */
    protected Handler uiHandler = new Handler(Looper.getMainLooper());

    /**
     * 引用队列
     */
    protected ReferenceQueue<IBaseModelListener> referenceQueue;

    /**
     * 线程安全队列
     */
    protected ConcurrentLinkedDeque<WeakReference<IBaseModelListener>> weakReferenceDeque;

    public SuperBaseModel() {
        referenceQueue = new ReferenceQueue<>();
        weakReferenceDeque = new ConcurrentLinkedDeque<>();
    }

    /**
     * 对具体业务model进行注册区分
     *
     * @param listener 业务监听器
     */
    public void register(IBaseModelListener listener) {
        if (listener == null) return;

        synchronized (this) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends IBaseModelListener> releaseListener = null;

            while ((releaseListener = referenceQueue.poll()) != null) {
                weakReferenceDeque.remove(releaseListener);
            }

            // 如果队列中还存在此对象，就不用再注册
            for (WeakReference<IBaseModelListener> weakListener : weakReferenceDeque) {
                IBaseModelListener item = weakListener.get();

                if (item == listener) {
                    return;
                }
            }

            // 注册此对象
            WeakReference<IBaseModelListener> weakListener = new WeakReference<>(listener, referenceQueue);
            weakReferenceDeque.add(weakListener);
        }
    }

    /**
     * 取消注册
     *
     * @param listener 业务监听器
     */
    public void unregister(IBaseModelListener listener) {
        if (listener == null) return;

        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : weakReferenceDeque) {
                IBaseModelListener item = weakListener.get();

                if (item == listener) {
                    weakReferenceDeque.remove(weakListener);
                }
            }
        }
    }

    /**
     * 需要缓存的数据类型
     */
    protected Type getTClass() {
        return null;
    }

    /**
     * 该model的数据是否有apk预制的数据，如果有请返回
     */
    protected String getApkCache() {
        return null;
    }

    /**
     * 是否需要更新数据,默认每一次都需要更新
     *
     * @return true
     */
    protected boolean isNeedToUpdate() {
        return true;
    }

    /**
     * 加载缓存数据
     */
    protected abstract void notifyCacheData(T data);

    /**
     * 加载网络数据
     */
    protected abstract void load();

    /**
     * 获取缓存数据并加载网络数据
     */
    @SuppressWarnings("unchecked")
    public void getCacheDataAndLoad() {
        // 如果有apk内置数据,加载内置数据
        if (getApkCache() != null) {
            notifyCacheData((T) GsonUtils.fromLocalJson(getApkCache(), getTClass()));

            if (isNeedToUpdate()) {
                load();
            }

            return;
        }

        // 没有缓存数据,直接加载网络数据
        load();
    }

    /**
     * 订阅对象管理
     */
    public void addDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(disposable);
    }

    /**
     * 取消所有订阅
     */
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            // 解除所有所有添加的Disposable类的订阅，防止内存泄漏
            compositeDisposable.dispose();
        }
    }
}
