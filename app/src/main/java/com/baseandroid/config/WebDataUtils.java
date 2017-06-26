package com.baseandroid.config;

import android.widget.Toast;

import com.baseandroid.R;
import com.baseandroid.repository.json.Data;
import com.jayfeng.lesscode.core.LogLess;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class WebDataUtils {
    /**
     * 检查返回code
     *
     * @param data
     * @param showToast
     * @return
     */
    public static boolean checkJsonCode(Data data, boolean showToast) {
        if (data == null) {
            return false;
        }
        if (data.getStatus().startsWith("2")) {
            return true;
        } else {
            if (showToast) {
                Toast.makeText(Global.getContext(), data.getMessage(), Toast.LENGTH_SHORT);
            }
        }
        return false;
    }

    /**
     * 错误异常处理
     *
     * @param throwable
     * @param showToast
     */
    public static boolean errorThrowable(Throwable throwable, boolean showToast) {
        boolean isNetworkError = false;
        if (throwable instanceof ConnectException
                || throwable instanceof SocketTimeoutException
                || throwable instanceof UnknownHostException) {
            isNetworkError = true;
            if (showToast) {
                Toast.makeText(Global.getContext(), Global.getContext().getString(R.string.common_network_weak_try), Toast.LENGTH_SHORT);
            }
        } else {
            if (showToast) {
                Toast.makeText(Global.getContext(), Global.getContext().getString(R.string.common_server_error), Toast.LENGTH_SHORT);
            }
        }
        LogLess.$d("error throwable:" + throwable.getMessage());
        return isNetworkError;
    }
}
