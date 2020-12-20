package com.chacha.base.model;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.model
 * @date 12/17/20
 * @time 4:32 PM
 *
 * 应用模块：model
 * <p>
 *     通用 Model 业务类型监听器
 * </p>
 */
public interface IModelListener<T> extends IBaseModelListener {

    /**
     * 数据加载完成
     *
     * @param model
     * @param data
     */
    void onLoadFinish(BaseModel model, T data);

    /**
     * 数据加载失败
     *
     * @param model
     * @param prompt
     */
    void onLoadFail(BaseModel model, String prompt);
}
