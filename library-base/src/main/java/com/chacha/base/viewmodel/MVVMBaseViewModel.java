package com.chacha.base.viewmodel;

import com.chacha.base.model.SuperBaseModel;

import androidx.lifecycle.ViewModel;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.viewmodel
 * @date 12/17/20
 * @time 11:33 AM
 */
public abstract class MVVMBaseViewModel<V, M extends SuperBaseModel> extends ViewModel implements IMVVMBaseViewModel<V> {

}
