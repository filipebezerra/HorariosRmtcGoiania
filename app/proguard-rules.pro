# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/filipebezerra/Development/android-sdk/tools/proguard/proguard-android.txt
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

# Only obfuscate
-dontshrink

# Keep source file and line numbers for better crash logs
-keepattributes SourceFile,LineNumberTable

# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
-keep class !android.support.v7.internal.view.menu.** { *; }

# ButterKnife uses some annotations not available on Android.
-dontwarn butterknife.internal.**
# Prevent ButterKnife annotations from getting renamed.
-keepnames class * { @butterknife.InjectView *;}

# Eventbus methods can not be renamed.
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}