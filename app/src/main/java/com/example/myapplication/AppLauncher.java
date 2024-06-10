package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.Collections;
import java.util.List;


public class AppLauncher {
    public static void openApp(Context context, String appName, String appLauncher) {
//        new AppLauncher().getAllApp(context);
//        E  pkg:com.zhihu.android —— cls:com.zhihu.android.app.ui.activity.LauncherActivity
        Intent intent = new Intent();
        //这里跳转的是知乎的启动页
//        ComponentName comp = new ComponentName("com.zhihu.android", "com.zhihu.android.app.ui.activity.LauncherActivity");
        ComponentName comp = new ComponentName(appName, appLauncher);
//        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");

        intent.setComponent(comp);

        //为三方的activity新开任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }

    public void getAllApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> appsInfo = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(appsInfo, new ResolveInfo.DisplayNameComparator(packageManager));
        for (ResolveInfo info : appsInfo) {
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
            Log.e("app_info", "pkg:" + pkg + " —— cls:" + cls);
        }
    }
}
