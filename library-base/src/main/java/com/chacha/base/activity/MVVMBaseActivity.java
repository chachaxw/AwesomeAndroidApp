package com.chacha.base.activity;

import android.os.Bundle;
import android.view.View;

import com.chacha.base.loadsir.EmptyCallback;
import com.chacha.base.loadsir.ErrorCallback;
import com.chacha.base.loadsir.LoadingCallback;
import com.chacha.base.utils.ToastUtils;
import com.chacha.base.viewmodel.IMVVMBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.viewmodel
 * @date 1/16/21
 * @time 11:21 PM
 *
 * 应用模块: activity
 * <p>
 *     activity抽象基类
 * </p>
 */
public abstract class MVVMBaseActivity<V extends ViewDataBinding, VM extends IMVVMBaseViewModel> extends AppCompatActivity implements IBaseView {

    protected VM viewModel;

    protected V viewDataBinding;

    private boolean isShowedContent = false;

    // Raw use of parameterized class 'LoadService'
    // https://stackoverflow.com/questions/24672749/raw-use-of-parameterized-class
    protected LoadService mLoadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != viewModel && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }
    }

    @Override
    public void showContent() {
        if (mLoadService != null) {
            isShowedContent = true;
            mLoadService.showSuccess();
        }
    }

    @Override
    public void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void showEmpty() {
        if (mLoadService != null) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    @Override
    public void showError(String message) {
        if (mLoadService != null) {
            if (!isShowedContent) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                ToastUtils.show(this, message);
            }
        }
    }

    // 初始化 viewModel
    private void initViewModel() {
        viewModel = getViewModel();

        if (viewModel != null) {
            viewModel.attachUI(this);
        }
    }

    // 初始化 viewDataBinding
    // 安卓视图绑定文档(https://developer.android.com/topic/libraries/view-binding?hl=zh-cn)
    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;

        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }

        viewDataBinding.executePendingBindings();
    }

    /**
     * 注册LoadSir, loadsir使用说明(https://github.com/KingJA/LoadSir)
     *
     * @param view 界面View
     */
    public void setLoadSir(View view) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault().register(view, (Callback.OnReloadListener)v -> onRetryButtonClick());
        }
    }

    /**
     * 获取参数Variable
     */
    protected abstract int getBindingVariable();

    // 获取layout Id
    @LayoutRes
    protected abstract int getLayoutId();

    // 获取viewModel抽象方法
    protected abstract VM getViewModel();

    // 重试按钮点击事件
    protected abstract void onRetryButtonClick();
}
