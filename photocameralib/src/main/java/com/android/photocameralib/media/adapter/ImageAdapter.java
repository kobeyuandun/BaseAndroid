package com.android.photocameralib.media.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.photocameralib.R;
import com.android.photocameralib.media.bean.Image;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter {

    protected List<Image> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnClickListern mOnClickListern;

    private boolean isSingleSelect;

    public ImageAdapter(Context context, OnClickListern onClickListern) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mOnClickListern = onClickListern;
        mItems = new ArrayList<>();
    }

    public void setSingleSelect(boolean singleSelect) {
        isSingleSelect = singleSelect;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new CamViewHolder(mInflater.inflate(R.layout.item_list_cam, parent, false));
        }

        return new ImageViewHolder(mInflater.inflate(R.layout.item_list_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Image item = mItems.get(position);
        if (item.getId() != 0) {
            ImageViewHolder h = (ImageViewHolder) holder;
            h.mCheckView.setSelected(item.isSelect());
            h.mMaskView.setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
            h.mGifMask.setVisibility(item.getPath()
                    .toLowerCase()
                    .endsWith("gif") ? View.VISIBLE : View.GONE);
            Glide.with(mContext)
                    .load(item.getPath())
                    .asBitmap()
                    .centerCrop()
                    .error(R.drawable.ic_split_graph)
                    .into(h.mImageView);
            h.mCheckView.setVisibility(isSingleSelect ? View.GONE : View.VISIBLE);
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListern.itemOnClickListern((Integer) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Image image = mItems.get(position);
        if (image.getId() == 0) {
            mItems.get(position);
            return 0;
        }
        return 1;
    }

    public Image getItem(int position) {
        return mItems.get(position);
    }

    public void addAll(List<Image> items) {
        if (items != null) {
            this.mItems.addAll(items);
            notifyItemRangeInserted(this.mItems.size(), items.size());
        }
    }

    public final void addItem(Image item) {
        if (item != null) {
            this.mItems.add(item);
            notifyItemChanged(mItems.size());
        }
    }

    public void addItem(int position, Image item) {
        if (item != null) {
            this.mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void updateItem(int position) {
        if (getItemCount() > position) {
            notifyItemChanged(position);
        }
    }

    public final void removeItem(Image item) {
        if (this.mItems.contains(item)) {
            int position = mItems.indexOf(item);
            this.mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public final void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    private static class CamViewHolder extends RecyclerView.ViewHolder {
        CamViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        ImageView mCheckView;
        ImageView mGifMask;
        View mMaskView;

        ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            mCheckView = (ImageView) itemView.findViewById(R.id.cb_selected);
            mMaskView = itemView.findViewById(R.id.lay_mask);
            mGifMask = (ImageView) itemView.findViewById(R.id.iv_is_gif);
        }
    }

    public interface OnClickListern {
        public void itemOnClickListern(int postion);
    }
}
