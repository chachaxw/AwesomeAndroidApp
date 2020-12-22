package com.chacha.base.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.utils
 * @date 12/22/20
 * @time 12:08 PM
 *
 * 应用模块: utils
 * <p>
 *      通用Toast工具
 * <p>
 */
public class ToastUtils {
    private static Toast toast;

    public static void show(Context context, String msg) {
        try {
            if (context != null && !TextUtils.isEmpty(msg)) {
                if (toast != null) {
                    toast.cancel();
                }

                toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
                toast.setText(msg);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
