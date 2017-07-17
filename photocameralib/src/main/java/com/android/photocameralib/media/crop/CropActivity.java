package com.android.photocameralib.media.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.photocameralib.R;
import com.android.photocameralib.media.base.BaseActivity;
import com.android.photocameralib.media.config.SelectOptions;
import com.android.photocameralib.media.utils.Util;
import com.bumptech.glide.Glide;

import java.io.FileOutputStream;

import static com.android.photocameralib.R.id.tv_cancel;

public class CropActivity extends BaseActivity implements View.OnClickListener {
    private TextView mCropConfig;
    private TextView mCropCancel;
    private CropLayout mCropLayout;
    private static SelectOptions mOption;

    public static void show(Fragment fragment, SelectOptions options) {
        Intent intent = new Intent(fragment.getActivity(), CropActivity.class);
        mOption = options;
        fragment.startActivityForResult(intent, 0x04);
    }

    public static void show(Activity activity, SelectOptions options) {
        Intent intent = new Intent(activity, CropActivity.class);
        mOption = options;
        activity.startActivityForResult(intent, 0x04);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crop;
    }

    @Override
    protected void setupView() {
        setTitle("");
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mCropLayout = (CropLayout) findViewById(R.id.cropLayout);
        mCropConfig = (TextView) findViewById(R.id.tv_crop);
        mCropCancel = (TextView) findViewById(tv_cancel);
        mCropConfig.setOnClickListener(this);
        mCropCancel.setOnClickListener(this);
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {
        String url = mOption.getSelectedImages().get(0);
        Glide.with(CropActivity.this)
                .load(url)
                .fitCenter()
                .into(mCropLayout.getImageView());
        mCropLayout.setCropWidth(mOption.getCropWidth());
        mCropLayout.setCropHeight(mOption.getCropHeight());
        mCropLayout.start();
    }

    @Override
    public void onClick(View view) {
        if (view == mCropConfig) {
            Bitmap bitmap = null;
            FileOutputStream os = null;
            try {
                bitmap = mCropLayout.cropBitmap();
                String path = getFilesDir() + Util.getSaveImageFullName();
                os = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();

                Intent intent = new Intent();
                intent.putExtra("crop_path", path);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        } else if (view == mCropCancel) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mOption = null;
        super.onDestroy();
    }
}
