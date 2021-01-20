package com.chacha.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chacha.base.activity.IBaseView;
import com.chacha.base.viewmodel.IMVVMBaseViewModel;
import com.kingja.loadsir.core.LoadService;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.fragment
 * @date 1/17/21
 * @time 10:54 PM
 */
public abstract class MVVMLazyFragment<V extends ViewDataBinding, VM extends IMVVMBaseViewModel> extends Fragment implements IBaseView {

    protected V viewDataBinding;

    protected VM viewModel;

    protected String fragmentTag = this.getClass().getSimpleName();

    private LoadService loadService;

    /**
     * Fragment生命周期 onAttach -> onCreate -> onCreatedView -> onActivityCreated
     * -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy
     * -> onDetach 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有： onCreatedView +
     * onActivityCreated + onResume + onPause + onDestroyView
     */
    protected View rootView = null;

    // 布局是否完成
    protected boolean isViewCreated = false;

    // 当前可见状态
    protected boolean currentVisibleState = false;

    // 是否第一次可见
    protected boolean isFirstVisible = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        Log.d(fragmentTag, " : onCreate");
    }

    // 初始化参数
    private void initParams() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(fragmentTag, " : onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            rootView = viewDataBinding.getRoot();
        }

        isViewCreated = true;
        Log.d(fragmentTag, " : onCreateView");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(fragmentTag, " : onViewCreated");

        // 初始化的时候，判断当前Fragment可见状态
        // isHidden在使用FragmentTransaction的show/hidden时会调用,可见返回的是false

        viewModel = getViewModel();

        if (viewModel != null) {
            viewModel.attachUI(this);
        }

        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }

        if (isHidden() && getUserVisibleHint()) {
            // 可见状态，进行事件派发
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void dispatchUserVisibleHint(boolean isVisible) {

    }

    // 获取参数Variable
    protected abstract int getBindingVariable();

    // 获取viewModel抽象方法
    protected abstract VM getViewModel();

    // 获取layout Id
    @LayoutRes
    public abstract int getLayoutId();

    // 重试按钮点击事件
    protected abstract void onRetryButtonClick();
}
