package com.example.myapplication;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onCreate() {
        super.onCreate();

        ArrayList<String> app_pkg_all = MainActivity.get_app_pkg_all();
        Log.e("APP APK", " " + app_pkg_all);
        // 打印 app_pkg_all to String[]
        for (String appPackage : app_pkg_all.toArray(new String[0])) {
            Log.e("dynamicSetServiceInfo", "" + appPackage);
        }
        dynamicSetServiceInfo(app_pkg_all.toArray(new String[0]));


// 在Service中注册广播接收器
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                request = intent.getStringExtra("data");
                // 处理收到的数据
                Log.e("RECEIVE", request);
            }
        };
        registerReceiver(receiver, new IntentFilter("custom-action"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        ArrayList<String> app_pkg_all = MainActivity.get_app_pkg_all();
//        Log.e("APP APK", " " + app_pkg_all);
//        // 打印 app_pkg_all to String[]
//        for (String appPackage : app_pkg_all.toArray(new String[0])) {
//            Log.e("dynamicSetServiceInfo onStartCommand", "" + appPackage);
//        }
//        dynamicSetServiceInfo(app_pkg_all.toArray(new String[0]));

//        String app_pkg_used = MainActivity.get_app_pkg_used();
//        Log.e("APP APK onStartCommand", " " + app_pkg_used);
//        // 打印 app_pkg_all to String[]
//        dynamicSetServiceInfo(app_pkg_used);

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        // 停止服务
        stopSelf();
    }

    @Override
    public void onServiceConnected() {
//        ArrayList<String> app_pkg_all = MainActivity.get_app_pkg_all();
//        Log.e("APP APK", " " + app_pkg_all);
//        // 打印 app_pkg_all to String[]
//        for (String appPackage : app_pkg_all.toArray(new String[0])) {
//            Log.e("dynamicSetServiceInfo onServiceConnected", "" + appPackage);
//        }
//        dynamicSetServiceInfo(app_pkg_all.toArray(new String[0]));\

//        String app_pkg_used = MainActivity.get_app_pkg_used();
//        Log.e("APP APK onServiceConnected", " " + app_pkg_used);
//        // 打印 app_pkg_all to String[]
//        dynamicSetServiceInfo(app_pkg_used);

    }

    public void dynamicSetServiceInfo(String[] app_pkt) {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // 配置服务信息...
//        info.eventTypes = AccessibilityEvent.TYPE_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
//        info.canRetrieveWindowContent = true;
        info.notificationTimeout = 100;
        info.packageNames = app_pkt;
//        info.canPerformGestures = true;
        Log.e("setServiceInfo", "is Successfully??? ");
        setServiceInfo(info);
        Log.e("setServiceInfo", "Successfully!!!");
    }

    public void dynamicSetServiceInfo(String app_pkt) {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // 配置服务信息...
//        info.eventTypes = AccessibilityEvent.TYPE_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
//        info.canRetrieveWindowContent = true;
        info.notificationTimeout = 100;
        info.packageNames = new String[]{app_pkt};
//        info.canPerformGestures = true;
        Log.e("setServiceInfo", "is Successfully??? ");
        setServiceInfo(info);
        Log.e("setServiceInfo", "Successfully!!!");
    }


    //    TYPE_VIEW_CLICKED - 点击事件
//    TYPE_VIEW_LONG_CLICKED - 长按事件
//    TYPE_VIEW_SELECTED - 选中事件
//    TYPE_VIEW_FOCUSED - 获取焦点事件
//    TYPE_VIEW_TEXT_CHANGED - 文本变化事件
//    TYPE_WINDOW_STATE_CHANGED - 窗口状态变化事件
//    TYPE_NOTIFICATION_STATE_CHANGED - 通知状态变化事件
//    TYPE_VIEW_HOVER_ENTER - 悬停进入事件
//    TYPE_VIEW_HOVER_EXIT - 悬停退出事件
//    TYPE_TOUCH_EXPLORATION_GESTURE_START - 触摸浏览手势开始事件
//    TYPE_TOUCH_EXPLORATION_GESTURE_END - 触摸浏览手势结束事件
//    TYPE_VIEW_SCROLLED - 滚动事件
//    TYPE_VIEW_TEXT_SELECTION_CHANGED - 文本选中变化事件
//    TYPE_ANNOUNCEMENT - 公告事件
//    TYPE_VIEW_ACCESSIBILITY_FOCUSED - 获取辅助功能焦点事件
//    TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED - 清除辅助功能焦点事件
//    TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY - 在运动粒度遍历文本事件
//    TYPE_GESTURE_DETECTION_START - 手势识别开始事件
//    TYPE_GESTURE_DETECTION_END - 手势识别结束事件
//    TYPE_TOUCH_INTERACTION_START - 触摸交互开始事件
//    TYPE_TOUCH_INTERACTION_END - 触摸交互结束事件
//    TYPE_WINDOWS_CHANGED - 窗口变化事件
//    TYPE_VIEW_CONTEXT_CLICKED - 上下文点击事件
//    TYPE_ASSIST_READING_CONTEXT - 辅助阅读上下文事件
    private static String request;

    /**
     * 表示已经添加的标签的个数
     */
    static int index = 0;

    static boolean STATE_CHANGE_FLAG = true;

    static String currentUI = "";

    private Vector<Position> ID_TO_POSITION = new Vector<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if ((event.getEventType() == TYPE_VIEW_CLICKED || STATE_CHANGE_FLAG)) {
//            ID_TO_POSITION.clear();
            Log.e("OPEN", "TYPE_VIEW_CLICKED");
            try {
                QueryRequest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("执行完毕");

            STATE_CHANGE_FLAG = false;
        }


    }

    public void QueryRequest() throws IOException {
        currentUI = "";
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            traverseAndPrintNode(rootNode);
        }
        index = 0;
//        request = "choose a post you like";
        Log.e("RECEIVE !!!", request);
        OpenAIManager openAIManager = new OpenAIManager();
        String prompt = "You are a smartphone assistant to help users complete tasks by interacting\n" +
                "with mobile apps. Given a task, the previous UI actions, and the content of\n" +
                "current UI state, your job is to decide whether the task is already finished by\n" +
                "the previous actions, and if not, decide which UI element in current UI state\n" +
                "should be interacted.\n" +
                "\n" +
                " Task: " + request + "\n" +
                "Previous UI actions:\n" +
                "- Start the File manager app. Current UI state:\n" +
                currentUI +
                " Which action should you choose next? Fill in\n" +
                "the blanks about the next one interaction:-\n" +
                "id=<id number> - action=<tap/input> -\n" +
                "input text=<text or N/A>. (if you think the\n" +
                "task has been completed, the id should be -1";
        prompt = prompt.replace("\n", " ");
        openAIManager.queryGPTV2(prompt, new GPTV2ResponseCallback() {
            @Override
            public void onSuccess(Integer integer, String content) {

                if (integer != -1) {

                    String action = extractActionValue(content, patternString);
                    if (action == "tap") {
                        Position position = ID_TO_POSITION.get(integer);
                        System.out.println("click ID:" + integer);
                        triggerAccessibilityEvent(position.getX(), position.getY());
                    } else {
                        String text = extractActionValue(content, regex);
                        ID_TO_POSITION.clear();
                        index = 0;
                        AccessibilityNodeInfo rootNode2 = getRootInActiveWindow();
                        traverseAndPrintNode(rootNode2, integer, text);
                    }

                } else {
                    System.out.println("Error!");
                }

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Query failed! Exception: " + e.getMessage());
                // 在这里处理失败的情况
            }
        });
    }

    static String regex = "input text=(\\S+)";
    static String patternString = "action=\\s*([^\\s]+)";

    public static String extractActionValue(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static int MAX_STRLEN = 4;

    private void traverseAndPrintNode(AccessibilityNodeInfo node) {
        String label = "";
        if (node != null) {
            String nodeName = node.getClassName().toString();
            if ("android.widget.EditText".equals(nodeName)) {

                String nodeId = "id=" + index++;

                String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
                label = "<input " + nodeId + nodeLabel + ">" + node.getText() + "</input>";


                Position position = getRect(node);
                ID_TO_POSITION.add(position);

                if (index <= MAX_STRLEN) {
                    currentUI += label + "\n";
                    System.out.println(label);
                } else {
                    return;
                }
            }
        } else if (hasContent(node) && node.isClickable()) {
            String nodeId = "id=" + index++;
            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
            label = "<button " + nodeId + nodeLabel + ">" + node.getText() + "</button>";

            Position position = getRect(node);
            ID_TO_POSITION.add(position);
            if (index <= MAX_STRLEN) {
                currentUI += label + "\n";
                System.out.println(label);
            } else {
                return;
            }
        } else {
            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
            if (nodeLabel != "" && hasContent(node)) {

                String nodeId = "id=" + index++;
                label = "<p " + nodeId + nodeLabel + "></p>";


                Position position = getRect(node);
                ID_TO_POSITION.add(position);

                if (index <= MAX_STRLEN) {
                    currentUI += label + "\n";
                    System.out.println(label);
                } else {
                    return;
                }
            }
        }


        for (int i = 0; i < node.getChildCount(); i++) {
            traverseAndPrintNode(node.getChild(i));
        }

    }

    private Bundle getBundleForText(String text) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text
        );
        return arguments;
    }

    private void traverseAndPrintNode(AccessibilityNodeInfo node, Integer integer, String text) {
        String label = "";
        if (node != null) {
            String nodeName = node.getClassName().toString();
            if ("android.widget.EditText".equals(nodeName)) {

                String nodeId = "id=" + index++;
                if (Integer.parseInt(nodeId) == integer) {
                    // 设置焦点到 EditText
                    node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    // 清空 EditText 中的文本
                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, getBundleForText(""));
                    // 输入指定文本
                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, getBundleForText(text));
                    return;
                }
                String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
                label = "<input " + nodeId + nodeLabel + ">" + node.getText() + "</input>";


                Position position = getRect(node);
                ID_TO_POSITION.add(position);

                if (index <= MAX_STRLEN) {
                    currentUI += label + "\n";
                    System.out.println(label);
                } else {
                    return;
                }
            }
        } else if (hasContent(node) && node.isClickable()) {
            String nodeId = "id=" + index++;
            if (Integer.parseInt(nodeId) > integer) {
                return;
            }
            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
            label = "<button " + nodeId + nodeLabel + ">" + node.getText() + "</button>";

            Position position = getRect(node);
            ID_TO_POSITION.add(position);
            if (index <= MAX_STRLEN) {
                currentUI += label + "\n";
                System.out.println(label);
            } else {
                return;
            }
        } else {
            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
            if (nodeLabel != "" && hasContent(node)) {

                String nodeId = "id=" + index++;
                if (Integer.parseInt(nodeId) > integer) {
                    return;
                }
                label = "<p " + nodeId + nodeLabel + "></p>";


                Position position = getRect(node);
                ID_TO_POSITION.add(position);

                if (index <= MAX_STRLEN) {
                    currentUI += label + "\n";
                    System.out.println(label);
                } else {
                    return;
                }
            }
        }


        for (int i = 0; i < node.getChildCount(); i++) {
            traverseAndPrintNode(node.getChild(i), integer, text);
        }

    }

    public Position getRect(AccessibilityNodeInfo node) {
        Rect boundsInScreen = new Rect();
        node.getBoundsInScreen(boundsInScreen);
        int left = boundsInScreen.left;
        int top = boundsInScreen.top;
        int right = boundsInScreen.right;
        int bottom = boundsInScreen.bottom;
        return new Position((left + right) / 2, (top + bottom) / 2);
    }
//    boundsInParent：描述该界面元素在其父容器中的边界矩形。
//    boundsInScreen：描述该界面元素在屏幕上的边界矩形。
//    packageName：界面元素所属的应用程序包名。
//    className：界面元素的类名。
//    text：界面元素显示的文本内容。
//    error：如果该界面元素表示一个错误状态，则提供相关的错误信息。
//    maxTextLength：如果界面元素是一个可编辑文本输入框，该属性表示可输入的最大文本长度。
//    stateDescription：描述界面元素的状态信息。
//    contentDescription：界面元素的内容描述，通常用于辅助功能。
//    tooltipText：界面元素的工具提示文本。
//    viewIdResName：界面元素的资源 ID 名称。
//    checkable：指示界面元素是否可选中。
//    checked：指示界面元素是否已选中。
//    focusable：指示界面元素是否可以获得焦点。
//    focused：指示界面元素是否具有焦点。
//    selected：指示界面元素是否被选中。
//    clickable：指示界面元素是否可点击。
//    longClickable：指示界面元素是否支持长按操作。
//    contextClickable：指示界面元素是否支持上下文菜单点击操作。
//    enabled：指示界面元素是否启用。
//    password：指示界面元素是否是密码字段。
//    scrollable：指示界面元素是否可以滚动。
//    importantForAccessibility：指示界面元素对于辅助功能的重要性。
//    visible：指示界面元素是否可见。
//    actions：表示可以对该界面元素执行的操作列表。


    private boolean hasContent(AccessibilityNodeInfo node) {
        CharSequence text = node.getText();
        return text != null && !text.toString().isEmpty();
    }

    private void traverseAndPrintNode2(AccessibilityNodeInfo node) {
        if (node != null) {

            System.out.println(node);

//
//            String nodeName = node.getClassName().toString();
//            String nodeId = "id=" + node.getWindowId();
//            String nodeLabel = node.getContentDescription() != null ? " label='" + node.getContentDescription() + "'" : "";
//            System.out.println("<" + nodeName + " " + nodeId + nodeLabel + "></" + nodeName + ">");

            for (int i = 0; i < node.getChildCount(); i++) {
                traverseAndPrintNode2(node.getChild(i));
            }
        }
    }

    private static void findByID(AccessibilityNodeInfo rootInfo, String text) {
        if (rootInfo.getChildCount() > 0) {
            Log.e("START", "start to find 1");
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootInfo.getChild(i);
                Log.e("START", "start to find 2");
                try {
                    if (child.findAccessibilityNodeInfosByViewId(text).size() > 0) {
                        Log.e("START", "start to find 3");
                        for (AccessibilityNodeInfo info : child.findAccessibilityNodeInfosByViewId(text)) {
//                            child.findAccessibilityNodeInfosByViewId()
                            Log.e("START", "start to find 4");
                            getClickable(info).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.e("CLICK", "have clicked");
//                            performClick(getClickable(info));
                            //模仿全局手势
//                        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS);
                        }
                    }
                } catch (NullPointerException e) {
                }
                findByID(child, text);//递归一直找一层层的全部遍历
            }
        }
    }

    private AccessibilityNodeInfo findByText(AccessibilityNodeInfo rootInfo, String text) {
        if (rootInfo.getChildCount() > 0) {
            for (int i = 0; i < rootInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = rootInfo.getChild(i);
                try {
                    if (child.findAccessibilityNodeInfosByText(text).size() > 0) {
                        for (AccessibilityNodeInfo info : child.findAccessibilityNodeInfosByText(text)) {

//                            getClickable(info).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            performAction(getClickable(info));
                            return null;
                        }
                    }
                } catch (NullPointerException e) {
                }
                findByText(child, text);
            }
        }
        return null;

    }
///        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AutoClickUtil.autoClick(getApplicationContext(), 0.5F, 0.5F);
//            }
//        }).start();

    //        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//            AccessibilityNodeInfo nodeInfo = event.getSource();
//            if (nodeInfo != null) {
//                // 执行自动点击操作
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.example.myapp:id/button");
//                if (list != null && list.size() > 0) {
//                    AccessibilityNodeInfo button = list.get(0);
//                    button.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
//            }
//        }
//        try {
//            //拿到根节点
//            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
//            if (rootInfo == null) {
//                return;
//            }
//            //开始找目标节点，这里拎出来细讲，直接往下看正文
//            if (rootInfo.getChildCount() != 0) {
//                if (rootInfo == null || TextUtils.isEmpty(rootInfo.getClassName())) {
//                    return;
//                }
//                Log.e("START", "start to find");
//                //开始去找
//                findByID(rootInfo, "com.tencent.mobileqq:id/chat_item_content_layout");
//            }
//        } catch (Exception e) {
//            Log.e("not found ERROR!", "not found!");
//        }
    //有些节点不可点击 点击交给父级甚至父级的父级...来做的。
    private static AccessibilityNodeInfo getClickable(AccessibilityNodeInfo info) {
        Log.i(TAG, info.getClassName() + ": " + info.isClickable());
        if (info.isClickable()) {
            return info;//如果可以点击就返回
        } else {//不可点击就检查父级 一直递归
            return getClickable(info.getParent());
        }
    }


    private void changeInput(AccessibilityNodeInfo info, String text) {  //改变editText的内容
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text);
        info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    private void MyGesture() {//仿滑动
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Path path = new Path();
            path.moveTo(1000, 1000);//滑动起点
            path.lineTo(2000, 1000);//滑动终点
            GestureDescription.Builder builder = new GestureDescription.Builder();
            GestureDescription description = builder.addStroke(new GestureDescription.StrokeDescription(path, 100L, 100L)).build();
            //100L 第一个是开始的时间，第二个是持续时间
            dispatchGesture(description, new MyCallBack(), null);
        }
    }

    //模拟手势的监听
    @RequiresApi(api = Build.VERSION_CODES.N)
    private class MyCallBack extends GestureResultCallback {
        public MyCallBack() {
            super();
        }

        @Override
        public void onCompleted(GestureDescription gestureDescription) {
            super.onCompleted(gestureDescription);

        }

        @Override
        public void onCancelled(GestureDescription gestureDescription) {
            super.onCancelled(gestureDescription);

        }

    }

    @Override
    public void onInterrupt() {
        // 当服务被中断时，执行清理操作
    }

    public void triggerAccessibilityEvent(int x, int y) {
        // 创建一个模拟点击的手势事件
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path clickPath = new Path();
        clickPath.moveTo(x, y); // 设置点击的位置
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(clickPath, 0, 1000));

        // 发送手势事件来触发辅助功能服务的onAccessibilityEvent()方法
        dispatchGesture(gestureBuilder.build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
//                Log.e("OPEN", "Triggered onAccessibilityEvent()");
            }
        }, null);
    }
//    public void simulateClick() {
//        Log.e("OPEN", "2hello");
//        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow(); // 获取当前活动的根节点
//        if (nodeInfo != null && nodeInfo.isClickable()) {
//            Log.e("OPEN", "2hello");
//            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }else{
//
//        }
//    }
}

