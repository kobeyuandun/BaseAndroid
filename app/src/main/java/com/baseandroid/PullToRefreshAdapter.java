package com.baseandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baseandroid.config.Global;
import com.baseandroid.recycleadpter.BaseRecycleViewAdapter;
import com.baseandroid.utils.SpannableStringUtils;
import com.baseandroid.utils.ToastUtils;

public class PullToRefreshAdapter extends BaseRecycleViewAdapter<Status> {
    public PullToRefreshAdapter(Context context) {
        super(context);
    }

    public class PtrViewHolder extends RecyclerView.ViewHolder {

        public PtrViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected void onBindBaseViewHolder(RecyclerView.ViewHolder holder, Status item) {
        switch (holder.getLayoutPosition() % 3) {
            case 0:
                holder.itemView.findViewById(R.id.img).setBackgroundResource(R.drawable.animation_img1);
                break;

            case 1:
                holder.itemView.findViewById(R.id.img).setBackgroundResource(R.drawable.animation_img2);

                break;

            case 2:
                holder.itemView.findViewById(R.id.img).setBackgroundResource(R.drawable.animation_img3);
                break;
        }
        TextView tweetName = (TextView) holder.itemView.findViewById(R.id.tweetName);
        tweetName.setText("Hoteis in Rio de Janeiro");
        TextView tweetText = (TextView) holder.itemView.findViewById(R.id.tweetText);
        String msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
        tweetText.setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
        tweetText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new PtrViewHolder(View.inflate(mContext, R.layout.layout_animation, null));
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            ToastUtils.showShortToast("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Global.getContext().getResources().getColor(R.color.black_alpha_16));
            ds.setUnderlineText(true);
        }
    };

    @Override
    protected int getBaseItemViewType(int position) {
        return 0;
    }
}
