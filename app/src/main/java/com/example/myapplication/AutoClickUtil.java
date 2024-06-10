package com.example.myapplication;

import android.app.Instrumentation;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;


public class AutoClickUtil {

    public static void autoClick(Context context, float x, float y) {// 定义一个名为autoClick的静态方法，接收三个参数：上下文（Context）对象，要点击的坐标位置的x和y值。

        // 使用DisplayMetrics类获得设备的分辨率，获取屏幕的宽度和高度，并将其保存在screenWidth和screenHeight变量中。
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;

            // 将上一步获取到的屏幕宽度和高度乘以对应坐标的值，得到实际像素位置。这样可以将相对坐标转换为设备的实际像素位置。
            int clickX = (int) (screenWidth * x);
            int clickY = (int) (screenHeight * y);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /// 使用Instrumentation类创建一个新的实例，模拟点击操作。先发送一个MotionEvent.ACTION_DOWN事件表示手
                    // 指按下，再发送一个MotionEvent.ACTION_UP事件表示手指抬起。这样就完成了对指定位置的模拟点击。
                    try {
                        Instrumentation instrumentation = new Instrumentation();
                        long downTime = System.currentTimeMillis();
                        long eventTime = System.currentTimeMillis();

                        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, clickX, clickY, 0);
                        instrumentation.sendPointerSync(downEvent);

                        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, clickX, clickY, 0);
                        instrumentation.sendPointerSync(upEvent);
                    } catch (SecurityException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
