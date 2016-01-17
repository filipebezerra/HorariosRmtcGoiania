/*
 * Copyright 2012 Uwe Trottmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package mx.x10.filipebezerra.horariosrmtcgoiania.utils;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;

public class AndroidUtils {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isKitKatOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isJellyBeanMR1OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isICSOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isGingerbreadOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean isFroyoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean isGoogleTV(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager != null && packageManager.hasSystemFeature("com.google.android.tv");
    }

    /**
     * Checks if {@link Environment}.MEDIA_MOUNTED is returned by {@code getExternalStorageState()}
     * and therefore external storage is read- and writeable.
     */
    public static boolean isExtStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRtlLayout() {
        if (AndroidUtils.isJellyBeanMR1OrHigher()) {
            int direction = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
            return direction == View.LAYOUT_DIRECTION_RTL;
        }
        return false;
    }

    /**
     * Copies the contents of one file to the other using {@link FileChannel}s.
     *
     * @param src source {@link File}
     * @param dst destination {@link File}
     */
    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }

        in.close();
        out.close();
    }

    /**
     * Copies data from one input stream to the other using a buffer of 8 kilobyte in size.
     *
     * @param input  {@link InputStream}
     * @param output {@link OutputStream}
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Execute an {@link android.os.AsyncTask} on a thread pool.
     *
     * @param task Task to execute.
     * @param args Optional arguments to pass to {@link AsyncTask#execute(Object[])}.
     * @param <T>  Task argument type.
     */
    @SafeVarargs
    @TargetApi(11)
    public static <T> void executeOnPool(AsyncTask<T, ?, ?> task, T... args) {
        // TODO figure out how to subclass abstract and generalized AsyncTask,
        // then put this there
        if (AndroidUtils.isHoneycombOrHigher()) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        } else {
            task.execute(args);
        }
    }

    public static void openAppRating(@NonNull Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(webIntent);
        }
    }

    @SuppressWarnings("deprecation")
    public static Intent createShareIntent(@NonNull Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text));

        if (!resolveActivity(shareIntent, context)) {
            return null;
        }

        return Intent.createChooser(shareIntent, context.getString(R.string.share_dialog_title));
    }

    public static Intent createSpeechInputIntent(@NonNull Context context) {
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.ACTION_WEB_SEARCH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(
                R.string.voice_prompt_text));

        if (!resolveActivity(speechIntent, context)) {
            return null;
        }

        return speechIntent;
    }

    public static boolean resolveActivity(@NonNull Intent intent, @NonNull Context context) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    /**
     * Checks the network and the wifi state and notifies with a toast to the user.
     *
     * @param context application context.
     * @param animateViews
     * @return if was notified
     */
    public static boolean checkAndNotifyNetworkState(@NonNull Context context,
            View... animateViews) {
        if (!NetworkUtils.isDeviceConnectedToInternet(context)) {
            SnackBarHelper.show(context,
                    context.getString(R.string.no_internet_connectivity), animateViews);
            return true;
        } else {
            return false;
        }
    }

    public static String getVersion(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "UnknownVersion";
        }
        return version;
    }
}