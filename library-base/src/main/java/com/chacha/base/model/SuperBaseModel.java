package com.chacha.base.model;

import android.os.Looper;
import android.os.Handler;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

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
}
