package com.android.photocameralib.media.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(getLayoutId(), null);
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
    }
}

