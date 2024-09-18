package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GPTV2ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    String AndroidID_Role;

    private static boolean isResponseing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gptv2_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        //配置recycle view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener(v -> {
            if(isResponseing){
                Toast.makeText(getApplicationContext(), "回复中...", Toast.LENGTH_SHORT).show();
                return;
            }
            String question = messageEditText.getText().toString().trim();

            if(question.isEmpty()){
                Toast.makeText(getApplicationContext(), "要提问才可以哦~", Toast.LENGTH_SHORT).show();
                return;
            }
            addToChat(question, Message.SEND_BY_ME);
            messageEditText.setText("");
            callAPI(question);
        });

        AndroidID_Role = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        showChat();
    }

    void addToChat(String message, String sendBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();//通知适配器数据已更新
                //将RecyclerView滚动到最新消息的位置,每次新消息添加到聊天列表时，RecyclerView都会滚动到最底部
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);

        final ChatLogHelper dbHelper = new ChatLogHelper(getApplicationContext());
        ContentValues values = new ContentValues();
        values.put("value",Message.toString(Message.SEND_BY_BOT,response));
        dbHelper.insert(values);

        isResponseing = false;
    }

    void errorResponse(){
        messageList.remove(messageList.size() - 1);
        addToChat("AI开小差了...", Message.SEND_BY_BOT);

        final ChatLogHelper dbHelper = new ChatLogHelper(getApplicationContext());
        ContentValues values = new ContentValues();
        values.put("value",Message.toString(Message.SEND_BY_BOT,"AI开小差了..."));
        dbHelper.insert(values);

        isResponseing = false;
    }

    void callAPI(String question) {

        final ChatLogHelper dbHelper = new ChatLogHelper(getApplicationContext());
        ContentValues values = new ContentValues();
        values.put("value",Message.toString(Message.SEND_BY_ME,question));
        dbHelper.insert(values);

        isResponseing = true;
        messageList.add(new Message("正在回复", Message.SEND_BY_BOT));
        OpenAIManager openAIManager = new OpenAIManager();
        openAIManager.queryGPTV3(question, new GPTV2ResponseCallback() {
            @Override
            public void onSuccess(Integer integer, String content) {
                if (integer != -1) {
                    addResponse(content.trim());
                } else {
                    errorResponse();
                }
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Query failed! Exception: " + e.getMessage());
                errorResponse();
            }
        });
    }

    public void showChat(){
        final ChatLogHelper dbHelper = new ChatLogHelper(getApplicationContext());
        Cursor chathistory = dbHelper.select();
        if (chathistory != null) {
            if (chathistory.moveToFirst()) {
                do {
                    int retCode = chathistory.getColumnIndex("value");
                    if(retCode != -1){
                        String value = chathistory.getString(retCode);
                        messageList.add(Message.toMessage(value));
                    }
                } while (chathistory.moveToNext());
            }
            chathistory.close();
        }
        if(messageList.size() > 0){
            Collections.reverse(messageList);
            Message message = messageList.get(messageList.size() - 1);
            messageList.remove(messageList.size() - 1);
            addToChat(message.getMessage(),message.getSendBy());
        }
    }

    public void delete(View view) {
        final ChatLogHelper dbHelper = new ChatLogHelper(getApplicationContext());
        dbHelper.deleteAll();

        messageList.clear();
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

        Toast.makeText(getApplicationContext(), "新建成功...", Toast.LENGTH_SHORT).show();
    }
}