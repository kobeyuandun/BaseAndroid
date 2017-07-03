package com.android.photocameralib.media.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.photocameralib.R;
import com.android.photocameralib.media.bean.ImageFolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageFolderAdapter extends RecyclerView.Adapter {

    protected List<ImageFolder> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mOnItemClickListener;

    public ImageFolderAdapter(Context context) {
        this.mContext = context;
        this.mItems = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public ImageFolder getItem(int position) {
        return mItems.get(position);
    }

    public final void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public final void resetItem(List<ImageFolder> items) {
        if (items != null) {
            clear();
            addAll(items);
        }
    }

    public void addAll(List<ImageFolder> items) {
        if (items != null) {
            this.mItems.addAll(items);
            notifyItemRangeInserted(this.mItems.size(), items.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderViewHolder(mInflater.inflate(R.layout.item_list_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImageFolder item = mItems.get(position);
        FolderViewHolder h = (FolderViewHolder) holder;
        h.tv_name.setText(item.getName());
        h.tv_size.setText(String.format("(%s)", item.getImages().size()));
        Glide.with(mContext)
                .load(item.getAlbumPath())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_split_graph)
                .into(h.iv_image);

        h.itemView.setTag(position);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });
    }

    private static class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_name, tv_size;

        public FolderViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_folder);
            tv_name = (TextView) itemView.findViewById(R.id.tv_folder_name);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setonItemClick(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
