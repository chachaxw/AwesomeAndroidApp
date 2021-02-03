package com.chacha.base.livedatabus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.livedatabus
 * @date 2/1/21
 * @time 3:11 PM
 *
 * <p>
 *     扩展liveData hook源码拦截实现非粘性事件，参考：
 *     <a href="https://yutiantina.github.io/2019/09/20/基于LiveData实现事件总线思路和方案" />基于LiveData实现事件总线思路和方案</a>
 * </p>
 */
public class BusMutableLiveData<T> extends MutableLiveData<T> {
    private final Map<Observer<? super T>, Observer<? super T>> observerMap = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);

        try {
            hook(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, new ObserverWrapper(observer));
        }
        super.observeForever(observerMap.get(observer));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Observer<? super T> realObserver = null;

        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        } else {
            realObserver = observer;
        }

        super.removeObserver(realObserver);
    }

    /**
     * hook源码实现，拦截订阅之前的事件
     *
     * @param observer androidx.lifecycle.Observer
     * @throws Exception 抛出异常
     */
    private void hook(Observer<? super T> observer) throws Exception {
        Class<LiveData> classLiveData = LiveData.class;

        // 获取一个类的所有成员变量，不包括基类，对于私有(private)变量，必须执行setAccessible方法
        Field fieldObservers = classLiveData.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);

        Object objectObservers = fieldObservers.get(this);
        Class<?> classObservers = objectObservers.getClass();
        Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
        methodGet.setAccessible(true);

        Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
        Object objectWrapper = null;

        if (objectWrapperEntry instanceof Map.Entry) {
            objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
        }

        if (objectWrapper == null) {
            throw new NullPointerException("Wrapper can not be bull!");
        }

        Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
        Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
        fieldLastVersion.setAccessible(true);

        // 获取LiveData的版本
        Field fieldVersion = classLiveData.getDeclaredField("mVersion");
        fieldVersion.setAccessible(true);

        // 设置wrapper包装类的版本
        Object objectVersion = fieldVersion.get(this);
        fieldLastVersion.set(objectWrapper, objectVersion);
    }
}
