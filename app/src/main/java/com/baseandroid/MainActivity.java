package com.baseandroid;

import android.os.Bundle;
import android.widget.TextView;

import com.baseandroid.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.test_id1)
    TextView test_id1;
    @BindView(R.id.test_id2)
    TextView test_id2;
    @BindView(R.id.test_id3)
    TextView test_id3;
    @BindView(R.id.test_id4)
    TextView test_id4;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        test_id1.setText("qqqqqqqqqqq");
        test_id2.setText("wwwwwwwww");
        test_id3.setText("aaaaaaaaaaaaaa");
        test_id4.setText("ddddddddddddddd");
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {

    }
}
