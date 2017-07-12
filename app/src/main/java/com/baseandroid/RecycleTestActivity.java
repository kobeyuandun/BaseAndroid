package com.baseandroid;

import android.os.Bundle;

import com.baseandroid.base.BaseActivity;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class RecycleTestActivity extends BaseActivity {

    @BindView(R.id.ptr_fresh_layout_id)
    PtrClassicFrameLayout ptr_fresh_layout_id;

    @Override
    protected int getLayoutId() {
        return R.layout.test_recycle_layout;
    }

    @Override
    protected void setupView() {
        // 处理与ViewPager存在的冲突

        ptr_fresh_layout_id.disableWhenHorizontalMove(true);
        ptr_fresh_layout_id.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 2000);
            }
        });
        //默认
        //ptr_fresh_layout_id.setLastUpdateTimeRelateObject(this);
        //自定义
        CustomPtrHeader customPtrHeader = new CustomPtrHeader(RecycleTestActivity.this);
        ptr_fresh_layout_id.setHeaderView(customPtrHeader);
        ptr_fresh_layout_id.addPtrUIHandler(customPtrHeader);
        String str = null;
        int length = str.length();
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {

    }


}
