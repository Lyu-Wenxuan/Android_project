package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingWindowService extends Service implements View.OnClickListener, View.OnTouchListener {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private ImageView buttonFloat;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    WindowManager.LayoutParams params;

    public FloatingWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化悬浮窗视图和按钮
        initFloatingView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        // 显示悬浮窗
//        if (mFloatingView != null) {
//            params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
//                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
//                            WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//
//            params.gravity = Gravity.TOP | Gravity.END;
//
//            buttonFloat.setOnTouchListener(this);  // 设置触摸监听器在按钮上
//
//            mWindowManager.addView(mFloatingView, params);
//        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 移除悬浮窗视图
        if (mFloatingView != null && mFloatingView.getParent() != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_float) {
            Log.e("FLOAT", "CLICK");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingView.getLayoutParams();
        int x_cord = (int) event.getRawX();
        int y_cord = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = layoutParams.x;
                initialY = layoutParams.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                mWindowManager.updateViewLayout(mFloatingView, layoutParams);
                return true;
        }
        return false;
    }

    private void initFloatingView() {
        // 加载悬浮窗视图
        LayoutInflater inflater = LayoutInflater.from(this);
        mFloatingView = inflater.inflate(R.layout.floating_window_layout, null);

        // 初始化按钮
        buttonFloat = mFloatingView.findViewById(R.id.button_float);
        buttonFloat.setOnClickListener(this);
        buttonFloat.setOnTouchListener(this);  // 将触摸监听器设置在按钮上

        // 获取 WindowManager 实例
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 创建 WindowManager.LayoutParams 对象，设置悬浮窗的类型、标志和初始位置等信息
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // 设置悬浮窗的位置为屏幕的右上角
        params.gravity = Gravity.TOP | Gravity.END;

        // 设置悬浮窗的初始偏移量（距离屏幕右上角的偏移量）
        params.x = 0;
        params.y = 0;

//        // 如果悬浮窗已经显示，则先将其移除
//        if (mFloatingView.getParent() != null) {
//            mWindowManager.removeView(mFloatingView);
//        }
//
//        // 将视图添加到 WindowManager
//        mWindowManager.addView(mFloatingView, params);
        if (mFloatingView.getParent() == null) {
            mWindowManager.addView(mFloatingView, params);
        }

        // 设置悬浮窗的触摸监听器
        mFloatingView.setOnTouchListener(this);
    }

}