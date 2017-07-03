package com.android.photocameralib.media;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.photocameralib.R;
import com.android.photocameralib.media.base.BaseActivity;
import com.android.photocameralib.media.config.SelectOptions;
import com.android.photocameralib.media.contract.SelectImageContract;
import com.android.photocameralib.media.utils.Util;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SelectImageActivity extends BaseActivity
        implements SelectImageContract.Operator {

    private static SelectOptions mOption;
    private SelectImageContract.View mView;

    public static void show(Context context, SelectOptions options) {
        mOption = options;
        context.startActivity(new Intent(context, SelectImageActivity.class));
    }

    public static void show(Context context, SelectOptions options, Class<?> cls) {
        mOption = options;
        context.startActivity(new Intent(context, cls));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void setupData(Bundle savedInstanceState) {
        requestExternalStorage();
    }

    @Override
    public void requestExternalStorage() {
        boolean isPermissionsGranted = new RxPermissions(SelectImageActivity.this).isGranted(Manifest.permission.CAMERA);
        if (!isPermissionsGranted) {
            new RxPermissions(SelectImageActivity.this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                if (mView == null) {
                                    handleView();
                                }
                            } else {
                                removeView();
                                Util.getConfirmDialog(SelectImageActivity.this, "", "没有权限, " + "你需要去设置中开启读取手机存储权限.", "去设置", "取消", false, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        goSetPremission();
                                        finish();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
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
            if (mView == null) {
                handleView();
            }
        }
    }

    @Override
    public void requestCamera() {
        boolean isPermissionsGranted = new RxPermissions(SelectImageActivity.this).isGranted(Manifest.permission.CAMERA);
        if (!isPermissionsGranted) {
            new RxPermissions(SelectImageActivity.this).request(Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                if (mView != null) {
                                    mView.onOpenCameraSuccess();
                                }
                            } else {
                                if (mView != null) {
                                    mView.onCameraPermissionDenied();
                                }
                                Util.getConfirmDialog(SelectImageActivity.this, "", "没有权限, " + "你需要去设置中开启相机权限.", "去设置", "取消", false, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        goSetPremission();
                                        finish();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
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
            if (mView != null) {
                mView.onOpenCameraSuccess();
            }
        }
    }

    @Override
    public void setDataView(SelectImageContract.View view) {
        mView = view;
    }

    @Override
    public void SetToolBarView(RelativeLayout relativeLayout) {

    }

    @Override
    public void SetButtonView(FrameLayout frameLayout) {

    }

    @Override
    protected void onDestroy() {
        mOption = null;
        super.onDestroy();
    }

    private void removeView() {
        SelectImageContract.View view = mView;
        if (view == null) {
            return;
        }
        try {
            getSupportFragmentManager().beginTransaction()
                    .remove((Fragment) view)
                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleView() {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_content, SelectFragment.newInstance(mOption))
                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goSetPremission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
