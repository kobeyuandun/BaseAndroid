package com.baseandroid.base;

import android.app.Activity;

import java.util.Stack;

public class BaseAppManager {

    private static Stack<Activity> activityStack;
    private static BaseAppManager instance;

    private BaseAppManager() {

    }

    public static BaseAppManager getAppManager() {
        if (instance == null) {
            synchronized (BaseAppManager.class) {
                if (instance == null) {
                    instance = new BaseAppManager();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    public void AppExit() {
        try {
            finishAllActivity();
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}