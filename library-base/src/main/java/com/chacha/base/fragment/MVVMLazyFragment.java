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
 * @time 10:54 PM
 *
 * <a href="https://developer.android.com/guide/components/fragments?hl=zh-cn>
 *     Fragment片段官方文档
 * </a>
 *
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

    private boolean isShowedContent = false;

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

    /**
     * 修改Fragment的可见性，setUserVisibleHint被调用有两种情况：
     * 1) 在切换tab的时候，会先于所有Fragment的其他生命周期，先调用这个函数，可以看log
     * 2) 对于之前已经调用过setUserVisibleHint方法的fragment后，让fragment从可见到不可见之间状态的变化
     *
     * @param isVisibleToUser 是否对用户可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(fragmentTag,"setUserVisibleHint: " + isVisibleToUser);

        if (isViewCreated) {
            // 必须是可见的（isVisibleToUser为true）而且只有当可见状态进行改变的时候才需要切换，
            // 否则会出现反复调用的情况，从而导致事件分发带来的多次更新
            if (isVisibleToUser && !currentVisibleState) {
                // 从不可见 -> 可见
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(fragmentTag,"onHiddenChanged: " + hidden);

        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    /**
     * 统一处理用户可见事件分发
     *
     * @param isVisible 是否可见
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        Log.d(fragmentTag,"dispatchUserVisibleHint: " + isVisible);

        // 首先考虑一下fragment嵌套fragment的情况(只考虑2层嵌套)
        if (isVisible && isParentInvisible()) {
            // 父Fragment此时不可见，直接return不做处理
            return;
        }

        // 如果当前状态与需要设置的状态本来就一致了，就不处理了
        if (isVisible == currentVisibleState) {
            return;
        }

        currentVisibleState = isVisible;

        if (isVisible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                // 第一次可见,进行全局初始化
                onFragmentFirstVisible();
            }

            onFragmentResume();

            // 分发事件给内嵌的Fragment
            dispatchUserVisibleHint(true);
        } else {
            onFragmentPause();
            dispatchUserVisibleHint(false);
        }
    }

    // Fragment真正的Pause，暂停一切网络耗时操作
    private void onFragmentPause() {
        Log.d(fragmentTag,"onFragmentPause: " + "暂停并结束相关操作耗时");
    }

    // Fragment真正的Resume，开始处理网络加载等耗时操作
    private void onFragmentResume() {
        Log.d(fragmentTag,"onFragmentPause: " + "恢复并开始相关操作耗时");
    }

    // Fragment第一次可见，根据业务进行初始化操作
    private void onFragmentFirstVisible() {
        Log.d(fragmentTag,"onFragmentFirstVisible  第一次可见");
    }

    // 父Fragment是否不可见
    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof MVVMLazyFragment) {
            MVVMLazyFragment fragment = (MVVMLazyFragment) parentFragment;
            return !fragment.isSupportVisible();
        }

        return false;
    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 如果不是第一次可见
        if (!isFirstVisible) {
            // 如果此时进行Activity跳转，会将所有缓存的Fragment进行onResume
            // 只需要对可见的Fragment进行加载
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 只有当当前状态由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint，
     * currentVisibleState && getUserVisibleHint() 能够限定时当前可见的Fragment，
     * 当前Fragment包含子Fragment的时候，dispatchUserVisibleHint内部本身就会
     * 通知子Fragment走到这里的时候自身又会调用一遍。
     */
    @Override
    public void onPause() {
        super.onPause();

        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        Log.d(fragmentTag,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }

        Log.d(fragmentTag,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(fragmentTag,"onDetach");
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
