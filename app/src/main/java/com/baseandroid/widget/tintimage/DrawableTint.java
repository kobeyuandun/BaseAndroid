package com.baseandroid.widget.tintimage;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableTint {

    // 支持使用ColorStateList着色,可以根据View的状态着色成不同的颜色.
    // getResources().getColorStateList(R.color.tint_colors)
    // res/color/tint_colors.xml
    // <?xml version="1.0" encoding="utf-8"?>
    // <selector xmlns:android="http://schemas.android.com/apk/res/android">
    // <item android:color="@color/red" android:state_pressed="true" />
    // <item android:color="@color/gray" />
    // </selector>

    // <com.baseandroid.widget.tintimage.TintImageView
    //    android:layout_width="wrap_content"
    //    android:layout_height="wrap_content"
    //    android:src="@drawable/animation_img"/>

    public static Drawable tint(Drawable originDrawable, int color) {
        return tint(originDrawable, ColorStateList.valueOf(color));
    }

    public static Drawable tint(Drawable originDrawable, int color, PorterDuff.Mode tintMode) {
        return tint(originDrawable, ColorStateList.valueOf(color), tintMode);
    }

    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList) {
        return tint(originDrawable, colorStateList, null);
    }

    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList, PorterDuff.Mode tintMode) {
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable);
        if (tintMode != null) {
            DrawableCompat.setTintMode(tintDrawable, tintMode);
        }
        DrawableCompat.setTintList(tintDrawable, colorStateList);
        return tintDrawable;
    }

}
