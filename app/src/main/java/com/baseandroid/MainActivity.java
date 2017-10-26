package com.baseandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.photocameralib.media.ImageGalleryActivity;
import com.android.photocameralib.media.config.SelectOptions;
import com.baseandroid.base.BaseActivity;
import com.baseandroid.config.Global;
import com.baseandroid.config.WebDataUtils;
import com.baseandroid.jpush.JPushBizutils;
import com.baseandroid.repository.ConfigRepository;
import com.baseandroid.repository.json.CheckUpdate;
import com.baseandroid.repository.json.Data;
import com.baseandroid.repository.json.Result;
import com.baseandroid.repository.json.ServerTime;
import com.baseandroid.repository.json.UserDate;
import com.baseandroid.repository.json.UserTokenInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayfeng.lesscode.core.DisplayLess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.test_id1)
    Button test_id1;
    @BindView(R.id.test_id2)
    Button test_id2;
    @BindView(R.id.test_id3)
    Button test_id3;
    @BindView(R.id.test_id4)
    Button test_id4;

    @BindView(R.id.test_id5)
    TextView test_id5;

    @BindView(R.id.test_id6)
    Button test_id6;
    @BindView(R.id.test_id7)
    Button test_id7;
    @BindView(R.id.test_id8)
    Button test_id8;

    @BindView(R.id.test_id9)
    ImageView test_id9;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        test_id1.setOnClickListener(this);
        test_id2.setOnClickListener(this);
        test_id3.setOnClickListener(this);
        test_id4.setOnClickListener(this);
        test_id6.setOnClickListener(this);
        test_id7.setOnClickListener(this);
        test_id8.setOnClickListener(this);
        test_id9.setOnClickListener(this);
    }

    public static void measureView(View view) {
        if (view == null) {
            return;
        }
        int nWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int nHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(nWidth, nHeight);
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {
        //checkUpdate();
        //addVisit();


        Log.e("++++++", "=====result 1 ====" + calday(200, 3, 1));//87,11,24
        Log.e("++++++", "=====result 111 ====" + calday2(200, 3, 1));
        Log.e("++++++", "=====result 2 ====" + toJulianDate(1040, 9, 2, 12, 0, 0, 0));
    }

    int calday(int year, int month, int day) {

        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        int result = day + (153 * m + 3) / 5 + y * 365 + y / 4 - y / 100 + y / 400 - 32045;
        return result;
    }

    int calday2(int year, int month, int day) {

        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        int result = day + (153 * m + 3) / 5 + y * 365 + y / 4 - 32083;
        return result;
    }

    public static int dayOfYear(int year, int month, int day) {
        int day_of_year;
        if (isLeapYear(year)) {
            day_of_year = ((275 * month) / 9) - ((month + 9) / 12) + day - 30;
        } else {
            day_of_year = ((275 * month) / 9) - (((month + 9) / 12) << 1) + day - 30;
        }
        return day_of_year;
    }

    public static double toJulianDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {

        // month range fix
        if ((month > 12) || (month < -12)) {
            month--;
            int delta = month / 12;
            year += delta;
            month -= delta * 12;
            month++;
        }
        if (month < 0) {
            year--;
            month += 12;
        }

        // decimal day fraction
        double frac = (hour / 24.0) + (minute / 1440.0) + (second / 86400.0) + (millisecond / 86400000.0);
        if (frac < 0) {        // negative time fix
            int delta = ((int) (-frac)) + 1;
            frac += delta;
            day -= delta;
        }
        //double gyr = year + (0.01 * month) + (0.0001 * day) + (0.0001 * frac) + 1.0e-9;
        double gyr = year + (0.01 * month) + (0.0001 * (day + frac)) + 1.0e-9;

        // conversion factors
        int iy0;
        int im0;
        if (month <= 2) {
            iy0 = year - 1;
            im0 = month + 12;
        } else {
            iy0 = year;
            im0 = month;
        }
        int ia = iy0 / 100;
        int ib = 2 - ia + (ia >> 2);

        // calculate julian date
        int jd;
        if (year <= 0) {
            jd = (int) ((365.25 * iy0) - 0.75) + (int) (30.6001 * (im0 + 1)) + day + 1720994;
        } else {
            jd = (int) (365.25 * iy0) + (int) (30.6001 * (im0 + 1)) + day + 1720994;
        }
        if (gyr >= 1582.1015) {                        // on or after 15 October 1582
            jd += ib;
            Log.e("++++++++++++", "ib = " + ib);
        }

        return jd + frac + 0.5;

    }


    public static boolean isLeapYear(int y) {
        boolean result = false;
        if (((y % 4) == 0) &&            // must be divisible by 4...
                ((y < 1582) ||                // and either before reform year...
                        ((y % 100) != 0) ||        // or not a century...
                        ((y % 400) == 0))) {        // or a multiple of 400...
            result = true;            // for leap year.
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.test_id1:
                //getServerTime();
                //gettestOcrIdcard();
                //gettestDriverLicense();
                //gettestShopSign();
                //checkUpdate();

                uploadfile();

                break;

            case R.id.test_id2:
                loginApp();
                break;

            case R.id.test_id3:
                getUseInfo();
                break;

            case R.id.test_id4:
                startActivity(new Intent(this, PullToRefreshUseActivity.class));
                break;

            case R.id.test_id6:
                SelectImageCustomActivit.show(MainActivity.this, new SelectOptions.Builder()
                        .setHasCam(true)
                        .setSelectCount(9)
                        .setCallback(new SelectOptions.Callback() {
                            @Override
                            public void doSelected(String[] images) {
                                for (String imgpath : images) {
                                    Log.e("++++++++", "SelectImageActivity imgpath = " + imgpath);
                                }
                            }
                        })
                        .build());
                break;

            case R.id.test_id7:
                ImageGalleryActivity.show(MainActivity.this, new String[]{"http://c.hiphotos.baidu.com/zhidao/pic/item/b64543a98226cffc3cef5decbe014a90f703eaa3.jpg", "http://img0.imgtn.bdimg.com/it/u=3164822311,573063053&fm=26&gp=0.jpg"}, 0, true);
                break;

            case R.id.test_id8:
                SelectImageCustomActivit.show(MainActivity.this, new SelectOptions.Builder()
                        .setHasCam(true)
                        .setSelectCount(1)
                        .setCrop(DisplayLess.$dp2px(256), DisplayLess.$dp2px(256))
                        .setCallback(new SelectOptions.Callback() {
                            @Override
                            public void doSelected(String[] images) {
                                for (String imgpath : images) {
                                    Log.e("++++++++", " imgpath = " + imgpath);
                                }
                            }
                        })
                        .build(), SelectImageCustomActivit.class);
                break;

            case R.id.test_id9:

                break;

            default:
                break;
        }
    }


    private void uploadfile() {

        Map<String, String> hasMap = new HashMap<>();
        String filepath = "/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1500360940917.jpg";
        ConfigRepository.getInstance()
                .uploadFileWithPartMap(hasMap, "file", filepath)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<ResponseBody>bindToLifecycle())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            String repone = responseBody.string();
                            Log.e("=========", "repone = " + repone);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("=========", "Throwable = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getServerTime() {
        ConfigRepository.getInstance()
                .getServerTime()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<ServerTime>>bindToLifecycle())
                .subscribe(new Observer<Data<ServerTime>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<ServerTime> serverTimeData) {
                        if (WebDataUtils.checkJsonCode(serverTimeData, true)) {
                            test_id5.setText(serverTimeData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loginApp() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userName", "15947860108");
        hashMap.put("password", "12345678");
        hashMap.put("passType", "0");
        hashMap.put("audience", "");

        ConfigRepository.getInstance()
                .loginApp(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<UserTokenInfo>>bindToLifecycle())
                .subscribe(new Observer<Data<UserTokenInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<UserTokenInfo> userTokenInfoData) {
                        if (WebDataUtils.checkJsonCode(userTokenInfoData, true)) {
                            UserTokenInfo userInfo = userTokenInfoData.getResult();
                            userInfo.getUser()
                                    .setMobile("49a52de65c23aa57251dd3701b29e54e");
                            Global.setUserInfo(userInfo);
                            Log.e("+++++++++++", "=====UserTokenInfo token===" + userInfo.getAc_token());
                            Log.e("+++++++++++", "=====UserTokenInfo mobile===" + userInfo
                                    .getUser()
                                    .getMobile());
                            test_id5.setText(userInfo.toString());
                            JPushBizutils.initJPush();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        WebDataUtils.errorThrowable(e, true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUseInfo() {
        ConfigRepository.getInstance()
                .getUserinfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<UserDate>>bindToLifecycle())
                .subscribe(new Observer<Data<UserDate>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<UserDate> userDateData) {
                        if (WebDataUtils.checkJsonCode(userDateData, true)) {
                            UserDate userDate = userDateData.getResult();
                            Log.e("+++++++", "====UserDate mobile====" + userDate.getUser()
                                    .getMobile());
                            test_id5.setText(userDate.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getNewGroundedCount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String formatdata = simpleDateFormat.format(new Date());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", formatdata);

        ConfigRepository.getInstance()
                .getNewGroundedCount(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<Result>>bindToLifecycle())
                .subscribe(new Observer<Data<Result>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<Result> resultData) {
                        if (WebDataUtils.checkJsonCode(resultData, true)) {
                            test_id5.setText(resultData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkUpdate() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("classcode", "ANDROID_CRM_VERSION");

        ConfigRepository.getInstance()
                .checkUpdate(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<CheckUpdate>>bindToLifecycle())
                .subscribe(new Observer<Data<CheckUpdate>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<CheckUpdate> checkUpdateData) {
                        if (WebDataUtils.checkJsonCode(checkUpdateData, true)) {
                            test_id5.setText(checkUpdateData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addVisit() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("areaId", "50804");
        hashMap.put("mobile", "13652365236");
        hashMap.put("openorno", "2");
        hashMap.put("type", "2");
        hashMap.put("content", "+2    2+");
        hashMap.put("merchantName", "徐家汇银行");
        hashMap.put("vcpPosition", "中山南二路");
        hashMap.put("visitAddress", "上海市中心");

        ConfigRepository.getInstance()
                .addVisit(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data>bindToLifecycle())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data checkUpdateData) {
                        if (WebDataUtils.checkJsonCode(checkUpdateData, true)) {
                            test_id5.setText(checkUpdateData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;" + "charset=utf-8");

    private void gettestOcrIdcard() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, Object> idcardMap = new HashMap<>();
        List<Object> objectList = new ArrayList<>();
        Map<String, Object> inputs = new HashMap<>();

        Map<String, Object> imageitem = new HashMap<>();
        imageitem.put("dataType", 50);


        // 对图像进行base64编码
        String imgBase64 = "";
        try {
            File file = new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1500359837353.jpg");
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(Base64.encode(content, Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        imageitem.put("dataValue", imgBase64);

        inputs.put("image", imageitem);

        Map<String, Object> configitem = new HashMap<>();
        Map<String, Object> itemsub = new HashMap<>();
        itemsub.put("side", "face");
        String configstr = gson.toJson(itemsub, type);
        configitem.put("dataType", 50);
        configitem.put("dataValue", configstr);

        inputs.put("configure", configitem);


        objectList.add(inputs);
        idcardMap.put("inputs", objectList);
        RequestBody requestBody;


        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(idcardMap, type));

        ConfigRepository.getInstance()
                .testOcrIdcard(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<String>bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String testOcrIdcard) {
                        Log.e("++++++++", "" + testOcrIdcard);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void gettestDriverLicense() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, Object> idcardMap = new HashMap<>();
        List<Object> objectList = new ArrayList<>();
        Map<String, Object> inputs = new HashMap<>();

        Map<String, Object> imageitem = new HashMap<>();
        imageitem.put("dataType", 50);


        // 对图像进行base64编码
        String imgBase64 = "";
        try {
            File file = new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1500360940917.jpg");
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(Base64.encode(content, Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        imageitem.put("dataValue", imgBase64);

        inputs.put("image", imageitem);


        objectList.add(inputs);
        idcardMap.put("inputs", objectList);
        RequestBody requestBody;

        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(idcardMap, type));

        ConfigRepository.getInstance()
                .testDriverLicense("http://dm-52.data.aliyun.com/rest/160601/ocr/ocr_driver_license.json", requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<String>bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String testOcrIdcard) {
                        Log.e("++++++++", "" + testOcrIdcard);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void gettestShopSign() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, Object> idcardMap = new HashMap<>();
        List<Object> objectList = new ArrayList<>();
        Map<String, Object> inputs = new HashMap<>();

        Map<String, Object> imageitem = new HashMap<>();
        imageitem.put("dataType", 50);


        // 对图像进行base64编码
        String imgBase64 = "";
        try {
            File file = new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1500363747380.jpg");
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(Base64.encode(content, Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        imageitem.put("dataValue", imgBase64);

        inputs.put("image", imageitem);

        objectList.add(inputs);
        idcardMap.put("inputs", objectList);
        RequestBody requestBody;

        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(idcardMap, type));

        ConfigRepository.getInstance()
                .testShopSign("http://dm-54.data.aliyun.com/rest/160601/ocr/ocr_shop_sign.json", requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<String>bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String testOcrIdcard) {
                        Log.e("++++++++", "" + testOcrIdcard);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
