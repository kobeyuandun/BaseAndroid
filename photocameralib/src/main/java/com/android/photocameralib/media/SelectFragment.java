package com.android.photocameralib.media;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.photocameralib.R;
import com.android.photocameralib.media.adapter.ImageAdapter;
import com.android.photocameralib.media.adapter.ImageFolderAdapter;
import com.android.photocameralib.media.base.BaseFragment;
import com.android.photocameralib.media.bean.Image;
import com.android.photocameralib.media.bean.ImageFolder;
import com.android.photocameralib.media.config.SelectOptions;
import com.android.photocameralib.media.contract.SelectImageContract;
import com.android.photocameralib.media.crop.CropActivity;
import com.android.photocameralib.media.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SelectFragment extends BaseFragment implements SelectImageContract.View, View.OnClickListener {

    ImageView mBackView;
    RecyclerView mContentView;
    Button mSelectFolderView;
    ImageView mSelectFolderIcon;
    View mToolbar;
    Button mDoneView;
    //    Button mPreviewView;
    FrameLayout mPreviewDoneView;

    private ImageFolderPopupWindow mFolderPopupWindow;
    private ImageFolderAdapter mImageFolderAdapter;
    private ImageAdapter mImageAdapter;

    private List<Image> imageList = new ArrayList<>();
    private List<Image> mSelectedImage;

    private String mCamImageName;
    private LoaderListener mCursorLoader = new LoaderListener();

    private SelectImageContract.Operator mOperator;

    private static SelectOptions mOption;

    public static SelectFragment newInstance(SelectOptions options) {
        mOption = options;
        return new SelectFragment();
    }

    @Override
    public void onAttach(Context context) {
        this.mOperator = (SelectImageContract.Operator) context;
        this.mOperator.setDataView(this);
        super.onAttach(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_image;
    }

    @Override
    protected void setupView() {

        mToolbar = (View) getView().findViewById(R.id.toolbar);
        mBackView = (ImageView) getView().findViewById(R.id.icon_back);
        mSelectFolderView = (Button) getView().findViewById(R.id.btn_title_select);
        mSelectFolderIcon = (ImageView) getView().findViewById(R.id.iv_title_select);

        mContentView = (RecyclerView) getView().findViewById(R.id.rv_image);
        mDoneView = (Button) getView().findViewById(R.id.btn_done);
//        mPreviewView = (Button) getView().findViewById(R.id.btn_preview);
        mPreviewDoneView = (FrameLayout) getView().findViewById(R.id.lay_button);

        mContentView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mContentView.addItemDecoration(new SpaceGridItemDecoration((int) Util.dipTopx(getActivity(), 1)));
        mImageAdapter = new ImageAdapter(getActivity(), imageClickListener);
        mImageAdapter.setSingleSelect(mOption.getSelectCount() <= 1);
        mImageFolderAdapter = new ImageFolderAdapter(getActivity());
        mContentView.setAdapter(mImageAdapter);
        mContentView.setItemAnimator(null);
        mPreviewDoneView.setVisibility(mOption.getSelectCount() == 1 ? View.GONE : View.VISIBLE);

        mBackView.setOnClickListener(this);
        mSelectFolderView.setOnClickListener(this);
//        mPreviewView.setOnClickListener(this);
        mDoneView.setOnClickListener(this);

        this.mOperator.SetToolBarView((RelativeLayout) mToolbar);
        this.mOperator.SetButtonView(mPreviewDoneView);
    }

    private ImageAdapter.OnClickListern imageClickListener = new ImageAdapter.OnClickListern() {

        @Override
        public void itemOnClickListener(int postion) {
            onItemClick(postion);
        }

        @Override
        public void itemLookOnClickListener(int postion) {
            if (imageList.size() > 0) {
                ImageGalleryActivity.show(getActivity(), Util.toArray(imageList), postion, false);
            }
        }
    };

    @Override
    protected void setupData(Bundle savedInstanceState) {
        mSelectedImage = new ArrayList<>();
        if (mOption.getSelectCount() > 1 && mOption.getSelectedImages() != null) {
            List<String> images = mOption.getSelectedImages();
            for (String s : images) {
                if (s != null && new File(s).exists()) {
                    Image image = new Image();
                    image.setSelect(true);
                    image.setPath(s);
                    mSelectedImage.add(image);
                }
            }
        }
        getLoaderManager().initLoader(0, null, mCursorLoader);
    }

    private class LoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID, MediaStore.Images.Media.MINI_THUMB_MAGIC, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(getContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            if (data != null) {
                final ArrayList<Image> images = new ArrayList<>();
                final List<ImageFolder> imageFolders = new ArrayList<>();

                final ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                imageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                        File imageFilePash = new File(path);
                        if (!imageFilePash.exists()) {//不存在的图片不加载到相册
                            continue;
                        }

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);
                        images.add(image);

                        //如果是新拍的照片
                        if (mCamImageName != null && mCamImageName.equals(image.getName())) {
                            image.setSelect(true);
                            mSelectedImage.add(image);
                        }

                        //如果是被选中的图片
                        if (mSelectedImage.size() > 0) {
                            for (Image i : mSelectedImage) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }

                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        if (!imageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            folder.setAlbumPath(image.getPath());//默认相册封面
                            imageFolders.add(folder);
                        } else {
                            // 更新
                            ImageFolder f = imageFolders.get(imageFolders.indexOf(folder));
                            f.getImages().add(image);
                        }
                    } while (data.moveToNext());
                }

                addImagesToAdapter(images);
                defaultFolder.getImages().addAll(images);
                if (mOption.isHasCam()) {
                    defaultFolder.setAlbumPath(images.size() > 1 ? images.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                }
                mImageFolderAdapter.resetItem(imageFolders);

                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedImage.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImage) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImage.removeAll(rs);
                }

                // If add new mCamera picture, and we only need one picture, we result it.
                if (mOption.getSelectCount() == 1 && mCamImageName != null) {
                    handleResult();
                }

                handleSelectSizeChange(mSelectedImage.size());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
        imageList.clear();
        mImageAdapter.clear();
        if (mOption.isHasCam()) {
//            Image cam = new Image();
//            mImageAdapter.addItem(cam);
        }
        imageList.addAll(images);
        mImageAdapter.addAll(images);
    }

    private void handleSelectSizeChange(int size) {
        if (size > 0) {
//            mPreviewView.setEnabled(true);
            mDoneView.setEnabled(true);
            mDoneView.setText(String.format("%s(%s/%s)", getText(R.string.image_select_opt_done), size, mOption.getSelectCount()));
        } else {
//            mPreviewView.setEnabled(false);
            mDoneView.setEnabled(false);
            mDoneView.setText(getText(R.string.image_select_opt_done));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBackView) {
            getActivity().finish();
        }
//        else if (v == mPreviewView) {
//            if (mSelectedImage.size() > 0) {
//                ImageGalleryActivity.show(getActivity(), Util.toArray(mSelectedImage), mSelectedImage.size(), false);
//            }
//        }
        else if (v == mSelectFolderView) {
            showPopupFolderList();
        } else if (v == mDoneView) {
            onSelectComplete();
        }
    }

    public void onItemClick(int position) {
        if (mOption.isHasCam()) {
//            if (position != 0) {
            handleSelectChange(position);
//            }
//            else {
//                if (mSelectedImage.size() < mOption.getSelectCount()) {
//                    mOperator.requestCamera();
//                } else {
//                    Toast.makeText(getActivity(), "最多只能选择 " + mOption.getSelectCount() + " 张图片", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
        } else {
            handleSelectChange(position);
        }
    }

    private void handleSelectChange(int position) {
        Image image = mImageAdapter.getItem(position);
        //如果是多选模式
        final int selectCount = mOption.getSelectCount();
        if (selectCount > 1) {
            if (image.isSelect()) {
                image.setSelect(false);
                mSelectedImage.remove(image);
                mImageAdapter.updateItem(position);
            } else {
                if (mSelectedImage.size() == selectCount) {
                    Toast.makeText(getActivity(), "最多只能选择 " + selectCount + " 张照片", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    image.setSelect(true);
                    mSelectedImage.add(image);
                    mImageAdapter.updateItem(position);
                }
            }
            handleSelectSizeChange(mSelectedImage.size());
        } else {
            mSelectedImage.add(image);
            handleResult();
        }
    }

    /**
     * 完成选择
     */
    public void onSelectComplete() {
        handleResult();
    }

    private void handleResult() {
        if (mSelectedImage.size() != 0) {
            if (mOption.isCrop()) {
                List<String> selectedImage = mOption.getSelectedImages();
                selectedImage.clear();
                selectedImage.add(mSelectedImage.get(0).getPath());
                mSelectedImage.clear();
                CropActivity.show(this, mOption);
            } else {
                List<Image> rs = new ArrayList<>();
                for (Image i : mSelectedImage) {
                    File f = new File(i.getPath());
                    if (!f.exists()) {
                        rs.add(i);
                    }
                }
                if (rs.size() > 0) {
                    Toast.makeText(getActivity(), "已为您过滤" + rs.size() + "张不存在或损坏的图片", Toast.LENGTH_LONG).show();
                    mSelectedImage.removeAll(rs);
                }
                if (mSelectedImage.size() > 0) {
                    mOption.getCallback().doSelected(Util.toArray(mSelectedImage));
                }
                getActivity().finish();
            }
        }
    }

    /**
     * 申请相机权限成功
     */
    @Override
    public void onOpenCameraSuccess() {
//        toOpenCamera();
    }

    @Override
    public void onCameraPermissionDenied() {

    }

    /**
     * 创建弹出的相册
     */
    private void showPopupFolderList() {
        if (mFolderPopupWindow == null) {
            ImageFolderPopupWindow popupWindow = new ImageFolderPopupWindow(getActivity(), new ImageFolderPopupWindow.Callback() {
                @Override
                public void onSelect(ImageFolderPopupWindow popupWindow, ImageFolder model) {
                    addImagesToAdapter(model.getImages());
                    mContentView.scrollToPosition(0);
                    popupWindow.dismiss();
                    mSelectFolderView.setText(model.getName());
                }

                @Override
                public void onDismiss() {
                    mSelectFolderIcon.setImageResource(R.drawable.ic_arrow_bottom);
                }

                @Override
                public void onShow() {
                    mSelectFolderIcon.setImageResource(R.drawable.ic_arrow_top);
                }
            });
            popupWindow.setAdapter(mImageFolderAdapter);
            mFolderPopupWindow = popupWindow;
        }
        mFolderPopupWindow.showAsDropDown(mToolbar);
    }

    /**
     * 打开相机
     */
    private void toOpenCamera() {
        // 判断是否挂载了SD卡
        mCamImageName = null;
        String savePath = "";
        if (Util.hasSDCard()) {
            savePath = Util.getCameraPath();
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(getActivity(), "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
            return;
        }

        mCamImageName = Util.getSaveImageFullName();
        File out = new File(savePath, mCamImageName);

        /**
         * android N 系统适配
         */
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(), getString(R.string.image_photo_provider), out);
        } else {
            uri = Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0x03);
    }

    /**
     * 拍照完成通知系统添加照片
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case 0x03:
                    if (mCamImageName == null) {
                        return;
                    }
                    Uri localUri = Uri.fromFile(new File(Util.getCameraPath() + mCamImageName));
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                    getActivity().sendBroadcast(localIntent);
                    break;
                case 0x04:
                    if (data == null) {
                        return;
                    }
                    mOption.getCallback().doSelected(new String[]{data.getStringExtra("crop_path")});
                    getActivity().finish();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mOption = null;
        super.onDestroy();
    }
}
