# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn **.**

######## 通用 ########
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment

#不混淆资源类
-keep class **.R$*
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 保证 自定义View不被混淆 ###
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}


## support annotation @Keep
-keep,allowobfuscation @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

# If you do not use Rx:
-dontwarn rx.**
## eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

## avs
-keepclasseswithmembers class * extends com.zhixin.roav.avs.api.Event {
    <fields>;
    <methods>;
}
-keepclasseswithmembers class * extends com.zhixin.roav.avs.api.Directive {
    <fields>;
    <methods>;
}

#gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *;}
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }

-keep class com.zhixin.roav.base.Transaction { *; }
-keep class com.zhixin.roav.base.data.network.BaseResponse { *; }
-keep class com.zhixin.roav.base.data.network.BaseRequest { *; }
-keep class * extends com.zhixin.roav.base.Transaction { *; }
-keep class * extends com.zhixin.roav.base.data.network.BaseResponse { *; }
-keep class * extends com.zhixin.roav.base.data.network.BaseRequest { *; }

## okhttp
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


-keep class com.google.** { *;}
-keep class rx.** { *;}

-keep class org.** { *;}
-keep class butterknife.** { *;}
-keep class android.support.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}