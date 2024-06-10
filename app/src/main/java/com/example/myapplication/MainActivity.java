package com.example.myapplication;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Spinner mSpinner1;

    private static final int REQUEST_CODE_FLOATING_WINDOW = 1001;

    private final ArrayList<String> app_title_all; // 存放用户手机上的所有 APP 的显示名字
    public static ArrayList<String> app_pkg_all;  // 存放用户手机上的所有 APP 的包名
    public static String app_pkg_used;  // 存放用户手机上的所有 APP 的包名
    public static ArrayList<String> app_launcher_all;  // 存放用户手机上的所有 APP 的包名

    // 构造函数
    public MainActivity() {
        // 初始化成员变量
        app_title_all = new ArrayList<>();
        app_pkg_all = new ArrayList<>();
        app_launcher_all = new ArrayList<>();
        app_pkg_used = "";

    }

    public static ArrayList<String> get_app_pkg_all() {
        return app_pkg_all;
    }

    // 获取用户手机上的 APP 信息
    private List<ResolveInfo> getResolveInfos() {
        List<ResolveInfo> appList;

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getPackageManager();
        appList = pm.queryIntentActivities(intent, 0);
        appList.sort(new ResolveInfo.DisplayNameComparator(pm));

        return appList;
    }

    // 解析用户手机上的 APP 信息
    private void plintPkgAndCls(List<ResolveInfo> resolveInfos) {
        PackageManager packageManager = getPackageManager();

        Log.e("pkg", "####################start######################");
        for (int i = 0; i < resolveInfos.size(); i++) {
            String pkg = resolveInfos.get(i).activityInfo.packageName;
            String cls = resolveInfos.get(i).activityInfo.name;
            String parentActivityName = resolveInfos.get(i).activityInfo.targetActivity;
            String title = null;

            try {
                ApplicationInfo applicationInfo = packageManager.getPackageInfo(pkg, i).applicationInfo;
                title = applicationInfo.loadLabel(packageManager).toString();
            } catch (Exception ignored) {

            }

            app_title_all.add(title);
            app_pkg_all.add(pkg);
            app_launcher_all.add(cls);

            Log.i("pkg", title + "：" + pkg + "/" + cls + "/" + parentActivityName);
        }
        Log.e("pkg", "#####################end#######################");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plintPkgAndCls(getResolveInfos());  //捕获 APP 名称信息，并打印
        setContentView(R.layout.activity_main);
// 获取系统的AccessibilityManager
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);

// 判断是否已打开无障碍服务
        boolean isAccessibilityEnabled = false;
        List<AccessibilityServiceInfo> accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo service : accessibilityServices) {
            if (service.getId().equals(getPackageName() + "/" + MyAccessibilityService.class.getName())) {
                isAccessibilityEnabled = true;
                break;
            }
        }

        Log.e("isAccessibilityEnabled", "" + isAccessibilityEnabled);
        if (isAccessibilityEnabled) {

            Intent intent = new Intent(getApplicationContext(), MyAccessibilityService.class);
            startService(intent);
            Log.e("OPEN", "MyAccessibilityService.class");

        } else {
            // 未打开无障碍服务，跳转到无障碍设置页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }

        setThings();
    }


    public void setThings() {
        Button mBtnClick2 = findViewById(R.id.button2);
        mBtnClick2.setOnClickListener(view -> {
            EditText textView = findViewById(R.id.inputQuestion);
            String question = String.valueOf(textView.getText());
            // 在主界面发送广播
            Intent intent = new Intent("custom-action");
            intent.putExtra("data", question);
            sendBroadcast(intent);

            String app_name = (String) mSpinner1.getSelectedItem();
            int index = app_title_all.indexOf(app_name);
            String app_pkg = app_pkg_all.get(index);
            String app_launcher = app_launcher_all.get(index);
//                String element = app_pkg.get(index);
            Log.e("button", app_name + "：" + app_pkg + "/" + app_launcher);

            Log.e("OPEN", "MyAccessibilityService.class");
            Log.e("OPEN", "openApp");

            // 打开指定 APP
            AppLauncher.openApp(getApplicationContext(), app_pkg, app_launcher);


        });

        mSpinner1 = findViewById(R.id.spinner1);
        Spinner mSpinner1 = findViewById(R.id.spinner1);


        // 创建 APP 选项数据。
        String[] options = app_title_all.toArray(new String[0]);

        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, // 上下文环境
                android.R.layout.simple_spinner_item, // 用于非下拉状态时的item布局
                options) { // 数据源

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                // 获取或创建下拉列表项视图
                TextView textView;
                if (convertView == null) {
                    // 如果没有缓存，则新建一个TextView
                    textView = new TextView(getContext());
                    // 设置textview的一些基本属性
                    textView.setPadding(8, 0, 8, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // 可以设置字体大小
                } else {
                    textView = (TextView) convertView;
                }

                // 设置文字颜色为白色
                textView.setTextColor(getResources().getColor(R.color.white));

                // 设置当前item的文字内容
                textView.setText(getItem(position));

                return textView;
            }
        };
        // 设置下拉框的样式
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // 将适配器设置到 Spinner 上
        mSpinner1.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FLOATING_WINDOW) {
            if (Settings.canDrawOverlays(this)) {
                // 用户已授予悬浮窗权限，启动悬浮窗服务
                startFloatingWindowService();
            }
        }
//        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
//            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//            // 显示语音输入的结果
//            String spokenText = result.get(0);
//            tvResult.setText(spokenText);
//        }
    }

    private void startFloatingWindowService() {
        Intent intent = new Intent(this, FloatingWindowService.class);
        startService(intent);
    }
}
