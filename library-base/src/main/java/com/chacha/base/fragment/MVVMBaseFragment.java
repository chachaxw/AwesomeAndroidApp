package com.chacha.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chacha.base.activity.IBaseView;
import com.chacha.base.loadsir.EmptyCallback;
import com.chacha.base.loadsir.ErrorCallback;
import com.chacha.base.loadsir.LoadingCallback;
import com.chacha.base.utils.ToastUtils;
import com.chacha.base.viewmodel.IMVVMBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

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
 * @time 10:17 PM
 */
public abstract class MVVMBaseFragment<V extends ViewDataBinding, VM extends IMVVMBaseViewModel> extends Fragment implements IBaseView {

    protected V viewDataBinding;

    protected VM viewModel;

    protected String fragmentTag = this.getClass().getSimpleName();

    private LoadService loadService;

    private boolean isShowedContent = false;

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
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        Log.d(fragmentTag, " : onCreateView");
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = getViewModel();

        if (viewModel != null) {
            viewModel.attachUI(this);
        }

        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }

        Log.d(fragmentTag, " : onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(fragmentTag, " : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(fragmentTag, " : onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(fragmentTag, " : onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }
        Log.d(fragmentTag, " : onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }

        Log.d(fragmentTag, " : onDetach");
    }

    /**
     * 注册LoadSir, loadsir使用说明(https://github.com/KingJA/LoadSir)
     *
     * @param view 界面View
     */
    public void setLoadSir(View view) {
        if (loadService == null) {
            loadService = LoadSir.getDefault().register(view, (Callback.OnReloadListener) v -> onRetryButtonClick());
        }
    }

    @Override
    public void showContent() {
        if (loadService != null) {
            isShowedContent = true;
            loadService.showSuccess();
        }
    }

    @Override
    public void showLoading() {
        if (loadService != null) {
            loadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void showEmpty() {
        if (loadService != null) {
            loadService.showCallback(EmptyCallback.class);
        }
    }

    @Override
    public void showError(String message) {
        if (loadService != null) {
            if (!isShowedContent) {
                loadService.showCallback(ErrorCallback.class);
            } else {
                ToastUtils.show(getContext(), message);
            }
        }
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
