package com.chacha.base.loadsir;

import com.chacha.base.R;
import com.kingja.loadsir.callback.Callback;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.loadsir
 * @date 1/13/21
 * @time 11:41 AM
 *
 * 应用模块：loadsir
 * <p>
 *     空页面
 * </p>
 */
public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.base_layout_empty;
    }
}
