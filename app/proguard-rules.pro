# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/jay/Tools/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
# -ignorewarnings

# support
-keep class android.support.**{*;}

# jayfeng
-dontwarn com.jayfeng.lesscode.**

# ProGuard configurations for squareup
-keep class retrofit2.** { *; }
-keep class com.squareup.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn retrofit2.**
-dontwarn com.squareup.**
-dontwarn okhttp3.**
-dontwarn okio.**
# End squareup

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

## Wechat
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**
-keep class com.baidu.**{*;}
-dontwarn com.baidu.**
-keep class com.sina.**{*;}
-dontwarn com.sina.**
-keep class com.alipay.**{*;}
-dontwarn com.alipay.**
-keep class com.xiaomi.**{*;}
-dontwarn com.xiaomi.**
-keep class com.bumptech.**{*;}
-dontwarn com.bumptech.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends io.dcloud.DHInterface.IPlugin
-keep public class * extends io.dcloud.DHInterface.IFeature
-keep public class * extends io.dcloud.DHInterface.IBoot
-keep public class * extends io.dcloud.DHInterface.IReflectAble
-keep public class * extends io.dcloud.js.geolocation.IGeoManager

# Talking Data
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

# Talking umengAnalytics
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# JPush
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# -keep public class [您的应用包名].R$*{
# public static final int *;
# }

