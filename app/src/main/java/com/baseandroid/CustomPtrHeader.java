package com.baseandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class CustomPtrHeader extends FrameLayout implements PtrUIHandler {

    private ImageView animation_iv;

    public CustomPtrHeader(Context context) {
        super(context);
        init();
    }

    public CustomPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public CustomPtrHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.view_custom_ptr_header, this);
        animation_iv = (ImageView) view.findViewById(R.id.animation_iv);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.e("+++++", "====onUIReset======");
        animation_iv.setImageResource(R.drawable.pull_down_drawable);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.e("+++++", "====onUIRefreshPrepare======");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.e("+++++", "====onUIRefreshBegin======");
        AnimationDrawable animationDrawable = (AnimationDrawable) animation_iv.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.e("+++++", "====onUIRefreshComplete======");
        AnimationDrawable animationDrawable = (AnimationDrawable) animation_iv.getDrawable();
        animationDrawable.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        Log.e("+++++", "====ptrIndicator.getCurrentPercent()======" + ptrIndicator.getCurrentPercent());
    }

}
