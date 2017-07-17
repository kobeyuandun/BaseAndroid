package com.android.photocameralib.media.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.WindowManager;

import com.android.photocameralib.R;
import com.android.photocameralib.media.bean.Image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getCameraPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";// filePath:/sdcard/
    }

    public static String getSaveImageFullName() {
        return "Qianbao_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";// 照片命名
    }

    public static ArrayList<String> toArrayList(List<Image> images) {
        ArrayList<String> strings = new ArrayList<>();
        for (Image i : images) {
            strings.add(i.getPath());
        }
        return strings;
    }

    public static String[] toArray(List<Image> images) {
        if (images == null) {
            return null;
        }
        int len = images.size();
        if (len == 0) {
            return null;
        }

        String[] strings = new String[len];
        int i = 0;
        for (Image image : images) {
            strings[i] = image.getPath();
            i++;
        }
        return strings;
    }

    /**
     * 获得屏幕的宽度
     * @param context context
     * @return width
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获得屏幕的高度
     * @param context context
     * @return height
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * dp转px
     * @param context context
     * @param dpValue dp
     * @return px
     */
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context context
     * @param pxValue px
     * @return dp
     */
    public static float pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static String getExtension(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String mimeType = options.outMimeType;
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }

    public static boolean copyFile(final File srcFile, final File saveFile) {
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return false;
            }
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context, R.style.App_Theme_Dialog_Alert);
    }

    /**
     * 获取一个验证对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String message, String positiveText, String negativeText, boolean cancelable, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        return getDialog(context).setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener);
    }

    /**
     * 获取一个验证对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        return getDialog(context).setMessage(message)
                .setPositiveButton("确定", positiveListener)
                .setNegativeButton("取消", negativeListener);
    }

}
