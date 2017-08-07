package com.baseandroid;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baseandroid.base.BaseActivity;
import com.baseandroid.widget.recycleadpter.BaseRecycleViewAdapter;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class PullToRefreshUseActivity extends BaseActivity implements BaseRecycleViewAdapter.OnLoadMoreListener {

    @BindView(R.id.ptr_fresh_layout_id)
    PtrClassicFrameLayout ptr_fresh_layout_id;

    private RecyclerView mRecyclerView;
    private PullToRefreshAdapter pullToRefreshAdapter;

    private static final int TOTAL_COUNTER = 120;

    private static final int PAGE_SIZE = 12;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;

    private boolean isErr;
    private boolean mLoadMoreEndGone = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pull_to_refresh;
    }

    @Override
    protected void setupView() {
        ptr_fresh_layout_id.disableWhenHorizontalMove(true);
        ptr_fresh_layout_id.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新时，不加载更多
                        pullToRefreshAdapter.setEnableLoadMore(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pullToRefreshAdapter.resetData(DataServer.getSampleData(PAGE_SIZE));
                                isErr = false;
                                mCurrentCounter = PAGE_SIZE;
                                ptr_fresh_layout_id.refreshComplete();
                            }
                        }, delayMillis);
                    }
                }, 2000);
            }
        });
        //显示刷新时间
        ptr_fresh_layout_id.setLastUpdateTimeRelateObject(this);

        //自定义
        CustomPtrHeader customPtrHeader = new CustomPtrHeader(PullToRefreshUseActivity.this);
        ptr_fresh_layout_id.setHeaderView(customPtrHeader);
        ptr_fresh_layout_id.addPtrUIHandler(customPtrHeader);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        addHeadView();
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {

    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView
                .getParent(), false);
        headView.findViewById(R.id.iv).setVisibility(View.GONE);
        ((TextView) headView.findViewById(R.id.tv)).setText("change load view");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreEndGone = true;
                mRecyclerView.setAdapter(pullToRefreshAdapter);
                Toast.makeText(PullToRefreshUseActivity.this, "change complete", Toast.LENGTH_LONG)
                        .show();
            }
        });
        pullToRefreshAdapter.addHeaderView(headView);
    }

    private void initAdapter() {
        pullToRefreshAdapter = new PullToRefreshAdapter(PullToRefreshUseActivity.this);
        pullToRefreshAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
        pullToRefreshAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(pullToRefreshAdapter);
        mCurrentCounter = pullToRefreshAdapter.getData().size();
    }

    @Override
    public void onLoadMore() {
        ptr_fresh_layout_id.setEnabled(false);
        if (mCurrentCounter >= TOTAL_COUNTER) {
            //                    pullToRefreshAdapter.loadMoreEnd();//default visible
            pullToRefreshAdapter.loadMoreEnd(false);//true is gone,false is visible
        } else {
            if (isErr) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                        mCurrentCounter = pullToRefreshAdapter.getData().size();
                        pullToRefreshAdapter.loadMoreComplete();
                        ptr_fresh_layout_id.setEnabled(true);
                    }
                }, 1000);
            } else {
                isErr = true;
                pullToRefreshAdapter.loadMoreFail();
                ptr_fresh_layout_id.setEnabled(true);
            }
        }

    }
}
