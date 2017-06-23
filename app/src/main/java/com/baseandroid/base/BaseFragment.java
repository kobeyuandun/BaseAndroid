package com.baseandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baseandroid.BuildConfig;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends RxFragment {
    protected Context mContext;
    protected Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        setupView();
        setupData(savedInstanceState);

    }

    protected abstract int getLayoutId();

    protected abstract void setupView();

    protected abstract void setupData(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (BuildConfig.DEBUG) {
            // use the RefWatcher to watch for fragment leaks:
            RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
            refWatcher.watch(this);
        }
    }
}

