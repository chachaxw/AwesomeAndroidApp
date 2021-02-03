package com.chacha.base.livedatabus;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.livedatabus
 * @date 2/1/21
 * @time 2:43 PM
 *
 * <p>
 *     基于liveData的事件总线(EventBus原理分析，https://zhuanlan.zhihu.com/p/77809630)：
 *     1. 关联activity / fragment的生命周期，自动识别活动状态触发更新
 *     2. 可以发送两种事件，普通事件和粘性事件
 * </p>
 */
public class LiveDataBus {

    // 粘性事件集合
    private final Map<String, MutableLiveData> stickyBus;

    // 普通事件集合
    private final Map<String, Object> bus;

    public LiveDataBus() {
        this.stickyBus = new HashMap<>();
        this.bus = new HashMap<>();
    }

    private static class singleHolder {
        private static final LiveDataBus SINGLE_BUS = new LiveDataBus();
    }

    private static LiveDataBus getInstance() {
        return singleHolder.SINGLE_BUS;
    }

    public MutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<T>());
        }
        return (MutableLiveData<T>) bus.get(key);
    }

    public MutableLiveData<Object> withSticky(String key) {
        return withSticky(key, Object.class);
    }

    public <T> MutableLiveData<T> withSticky(String key, Class<T> type) {
        if (!stickyBus.containsKey(key)) {
            stickyBus.put(key, new MutableLiveData<T>());
        }
        return (MutableLiveData<T>) stickyBus.get(key);
    }
}
