package com.chacha.common.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.views
 * @date 2/18/21
 * @time 4:10 PM
 *
 * <p>
 *     自定义TextView
 * </p>
 *
 */
@SuppressLint("AppCompatCustomView")
public class CommonCustomTextView extends TextView {

    public CommonCustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CommonCustomTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CommonCustomTextView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context);
    }

    public CommonCustomTextView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        AssetManager assets = context.getAssets();
        Typeface tf = Typeface.createFromAsset(assets, "fonts/Lobster-1.4.otf");
        setTypeface(tf);
    }
}
