package com.android.photocameralib.media;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.photocameralib.R;
import com.android.photocameralib.media.base.BaseActivity;
import com.android.photocameralib.media.utils.AppOperator;
import com.android.photocameralib.media.utils.Util;
import com.android.photocameralib.media.widget.ImagePreviewView;
import com.android.photocameralib.media.widget.Loading;
import com.android.photocameralib.media.widget.PreviewerViewPager;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.concurrent.Future;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ImageGalleryActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener {
    public static final String KEY_IMAGE = "images";
    public static final String KEY_POSITION = "position";
    public static final String KEY_NEED_SAVE = "save";
    private PreviewerViewPager mImagePager;
    private TextView mIndexText;
    private String[] mImageSources;
    private int mCurPosition;
    private boolean mNeedSaveLocal;
    private boolean[] mImageDownloadStatus;
    private Point mDisplayDimens;

    public static void show(Context context, String images) {
        show(context, images, true);
    }

    public static void show(Context context, String images, boolean needSaveLocal) {
        if (images == null) {
            return;
        }
        show(context, new String[]{images}, 0, needSaveLocal);
    }

    public static void show(Context context, String images, boolean needSaveLocal, boolean needCookie) {
        if (images == null) {
            return;
        }
        show(context, new String[]{images}, 0, needSaveLocal, needCookie);
    }

    public static void show(Context context, String[] images, int position) {
        show(context, images, position, true);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal) {
        show(context, images, position, needSaveLocal, false);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal, boolean needCookie) {
        if (images == null || images.length == 0) {
            return;
        }
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra(KEY_IMAGE, images);
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra(KEY_NEED_SAVE, needSaveLocal);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_gallery;
    }

    @Override
    protected void setupView() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setTitle("");

        mImagePager = (PreviewerViewPager) findViewById(R.id.vp_image);
        mIndexText = (TextView) findViewById(R.id.tv_index);
        mImagePager.addOnPageChangeListener(this);

    }

    @Override
    protected void setupData(Bundle savedInstanceState) {
        mImageSources = getIntent().getExtras().getStringArray(KEY_IMAGE);
        mCurPosition = getIntent().getExtras().getInt(KEY_POSITION, 0);
        mNeedSaveLocal = getIntent().getExtras().getBoolean(KEY_NEED_SAVE, true);

        if (mImageSources != null) {
            mImageDownloadStatus = new boolean[mImageSources.length];
        }

        int len = mImageSources.length;
        if (mCurPosition < 0 || mCurPosition >= len) {
            mCurPosition = 0;
        }

        // If only one, we not need the text to show
        if (len == 1) {
            mIndexText.setVisibility(View.GONE);
        }

        mImagePager.setAdapter(new ViewPagerAdapter());
        mImagePager.setCurrentItem(mCurPosition);
        // First we call to init the TextView
        onPageSelected(mCurPosition);

        findViewById(R.id.iv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFileByPermission();
            }
        });
    }

    private void changeSaveButtonStatus(boolean isShow) {
        if (mNeedSaveLocal) {
            findViewById(R.id.iv_save).setVisibility(isShow ? View.VISIBLE : View.GONE);
        } else {
            findViewById(R.id.iv_save).setVisibility(View.GONE);
        }
    }

    private void updateDownloadStatus(int pos, boolean isOk) {
        mImageDownloadStatus[pos] = isOk;
        if (mCurPosition == pos) {
            changeSaveButtonStatus(isOk);
        }
    }

    public void saveToFileByPermission() {
        boolean isPermissionsGranted = new RxPermissions(ImageGalleryActivity.this).isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!isPermissionsGranted) {
            new RxPermissions(ImageGalleryActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                saveToFile();
                            } else {
                                Toast.makeText(ImageGalleryActivity.this, R.string.gallery_save_file_not_have_external_storage_permission, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            saveToFile();
        }
    }

    private void saveToFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, R.string.gallery_save_file_not_have_external_storage, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String path = mImageSources[mCurPosition];

        Object urlOrPath = path;
        // In this save max image size is source
        final Future<File> future = Glide.with(ImageGalleryActivity.this)
                .load(urlOrPath)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                try {
                    File sourceFile = future.get();
                    if (sourceFile == null || !sourceFile.exists()) {
                        return;
                    }
                    String extension = Util.getExtension(sourceFile.getAbsolutePath());
                    String extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + File.separator + "photocamera";
                    File extDirFile = new File(extDir);
                    if (!extDirFile.exists()) {
                        if (!extDirFile.mkdirs()) {
                            // If mk dir error
                            callSaveStatus(false, null);
                            return;
                        }
                    }
                    final File saveFile = new File(extDirFile, String.format("IMG_%s.%s", System
                            .currentTimeMillis(), extension));
                    final boolean isSuccess = Util.copyFile(sourceFile, saveFile);
                    callSaveStatus(isSuccess, saveFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    callSaveStatus(false, null);
                }
            }
        });
    }

    private void callSaveStatus(final boolean success, final File savePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // notify
                    Uri uri = Uri.fromFile(savePath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    Toast.makeText(ImageGalleryActivity.this, R.string.gallery_save_file_success, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(ImageGalleryActivity.this, R.string.gallery_save_file_failed, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
        mIndexText.setText(String.format("%s/%s", (position + 1), mImageSources.length));
        // 滑动时自动切换当前的下载状态
        changeSaveButtonStatus(mImageDownloadStatus[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @SuppressWarnings("deprecation")
    private synchronized Point getDisplayDimens() {
        if (mDisplayDimens != null) {
            return mDisplayDimens;
        }
        Point displayDimens;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            displayDimens = new Point();
            display.getSize(displayDimens);
        } else {
            displayDimens = new Point(display.getWidth(), display.getHeight());
        }

        mDisplayDimens = displayDimens;
        return mDisplayDimens;
    }

    private class ViewPagerAdapter extends PagerAdapter
            implements ImagePreviewView.OnReachBorderListener {

        private View.OnClickListener mFinishClickListener;

        @Override
        public int getCount() {
            return mImageSources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.lay_gallery_page_item_contener, container, false);
            ImagePreviewView previewView = (ImagePreviewView) view.findViewById(R.id.iv_preview);
            previewView.setOnReachBorderListener(this);
            Loading loading = (Loading) view.findViewById(R.id.loading);
            ImageView defaultView = (ImageView) view.findViewById(R.id.iv_default);

            loadImage(position, mImageSources[position], previewView, defaultView, loading);

            previewView.setOnClickListener(getListener());
            container.addView(view);
            return view;
        }

        private View.OnClickListener getListener() {
            if (mFinishClickListener == null) {
                mFinishClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                };
            }
            return mFinishClickListener;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onReachBorder(boolean isReached) {
            mImagePager.isInterceptable(isReached);
        }

        private <T> void loadImage(final int pos, final T urlOrPath, final ImageView previewView, final ImageView defaultView, final Loading loading) {

            loadImageDoDownAndGetOverrideSize(urlOrPath, new DoOverrideSizeCallback() {
                @Override
                public void onDone(int overrideW, int overrideH, boolean isTrue) {
                    DrawableRequestBuilder builder = Glide.with(ImageGalleryActivity.this)
                            .load(urlOrPath)
                            .listener(new RequestListener<T, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, T model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    if (e != null) {
                                        e.printStackTrace();
                                    }
                                    loading.stop();
                                    loading.setVisibility(View.GONE);
                                    defaultView.setVisibility(View.VISIBLE);
                                    updateDownloadStatus(pos, false);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, T model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    loading.stop();
                                    loading.setVisibility(View.GONE);
                                    updateDownloadStatus(pos, true);
                                    return false;
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                    // If download or get option error we not set override
                    if (isTrue && overrideW > 0 && overrideH > 0) {
                        builder = builder.override(overrideW, overrideH).fitCenter();
                    }

                    builder.into(previewView);
                }
            });
        }

        private <T> void loadImageDoDownAndGetOverrideSize(final T urlOrPath, final DoOverrideSizeCallback callback) {
            // In this save max image size is source
            final Future<File> future = Glide.with(ImageGalleryActivity.this)
                    .load(urlOrPath)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File sourceFile = future.get();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // First decode with inJustDecodeBounds=true to checkShare dimensions
                        options.inJustDecodeBounds = true;
                        // First decode with inJustDecodeBounds=true to checkShare dimensions
                        BitmapFactory.decodeFile(sourceFile.getAbsolutePath(), options);

                        int width = options.outWidth;
                        int height = options.outHeight;

                        if (width > 0 && height > 0) {
                            // Get Screen
                            final Point point = getDisplayDimens();

                            // This max size
                            final int maxLen = Math.min(Math.min(point.y, point.x) * 5, 1366 * 3);

                            // Init override size
                            final int overrideW, overrideH;

                            if ((width / (float) height) > (point.x / (float) point.y)) {
                                overrideH = Math.min(height, point.y);
                                overrideW = Math.min(width, maxLen);
                            } else {
                                overrideW = Math.min(width, point.x);
                                overrideH = Math.min(height, maxLen);
                            }

                            // Call back on main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onDone(overrideW, overrideH, true);
                                }
                            });
                        } else {
                            // Call back on main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onDone(0, 0, false);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        // Call back on main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDone(0, 0, false);
                            }
                        });
                    }
                }
            });
        }


    }

    interface DoOverrideSizeCallback {
        void onDone(int overrideW, int overrideH, boolean isTrue);
    }
}
