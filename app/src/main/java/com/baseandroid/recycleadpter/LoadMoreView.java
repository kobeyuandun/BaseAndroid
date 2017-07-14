package com.baseandroid.recycleadpter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void convert(ViewHolder holder) {

        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;

            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;

            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;

            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
        }
    }

    private void visibleLoading(ViewHolder holder, boolean visible) {
        holder.itemView.findViewById(getLoadingViewId()).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void visibleLoadFail(ViewHolder holder, boolean visible) {
        holder.itemView.findViewById(getLoadFailViewId()).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void visibleLoadEnd(ViewHolder holder, boolean visible) {
        holder.itemView.findViewById(getLoadEndViewId()).setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    public final void setLoadMoreEndGone(boolean loadMoreEndGone) {
        this.mLoadMoreEndGone = loadMoreEndGone;
    }

    public final boolean isLoadEndMoreGone() {
        if (getLoadEndViewId() == 0) {
            return true;
        }
        return mLoadMoreEndGone;
    }

    public abstract int getLayoutId();

    protected abstract int getLoadingViewId();

    protected abstract int getLoadFailViewId();

    protected abstract int getLoadEndViewId();
}
