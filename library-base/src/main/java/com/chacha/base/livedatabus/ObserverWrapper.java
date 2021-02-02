package com.chacha.base.livedatabus;

import androidx.lifecycle.Observer;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.livedatabus
 * @date 2/1/21
 * @time 4:19 PM
 *
 * <p>
 *     Observer包装类
 * </p>
 */
public class ObserverWrapper<T> implements Observer<T> {

    private final Observer<T> observer;

    public ObserverWrapper(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void onChanged(T t) {
        if (observer != null) {
            if (isCallOnObserver()) {
                return;
            }
            observer.onChanged(t);
        }
    }

    private boolean isCallOnObserver() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace != null && stackTrace.length > 0) {
            for (StackTraceElement element : stackTrace) {
                if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) && "observerForever".equals(element.getMethodName())) {
                    return true;
                }
            }
        }

        return false;
    }
}
