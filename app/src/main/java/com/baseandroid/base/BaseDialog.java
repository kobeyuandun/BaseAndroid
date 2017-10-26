package com.baseandroid.base;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jayfeng.lesscode.core.DisplayLess;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends DialogFragment {

    protected int mGravity = Gravity.CENTER;
    protected Unbinder mUnbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        setup();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow()
                .setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().getWindow()
                .getDecorView()
                .setPadding(DisplayLess.$dp2px(4), DisplayLess.$dp2px(4), DisplayLess.$dp2px(4), DisplayLess
                        .$dp2px(4));
        if (mGravity != Gravity.CENTER) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = mGravity;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        } else {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    private void setup() {
        setupView();
    }

    protected abstract int getLayoutId();

    protected abstract void setupView();

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        if (isAdded()) {
            ft.remove(this);
        }
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}