package com.codesky.radboss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codesky.radboss.example.ChatAdapter;

public class ChatActivity extends AppCompatActivity {

    public static final String CHAT_ICON = "chat_icon";
    public static final String CHAT_NAME = "chat_name";
    public static final String CHAT_LAST_MSG = "chat_last_msg";

    private String mName;
    private int heIcon;
    private int meIcon;

    private EditText mEditText;
    private ListView mChatListView;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mName = getIntent().getStringExtra(CHAT_NAME);
        heIcon = getIntent().getIntExtra(CHAT_ICON, 0);
        meIcon = R.drawable.monkey;

        findViewById(R.id.chat_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeftSendMessage();
            }
        });

        findViewById(R.id.chat_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightSendMessage();
            }
        });

        mEditText = (EditText) findViewById(R.id.chat_text);
        mChatListView = (ListView) findViewById(R.id.chat_content_list) ;

        mChatAdapter = new ChatAdapter(this, null);
        mChatListView.setAdapter(mChatAdapter);
    }

    private String getSendText() {
        String text = mEditText.getText().toString();
        mEditText.setText("");
        return text;
    }

    public void onLeftSendMessage() {
        String text = getSendText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(ChatActivity.this, "输入内容空!", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }
    }

    public void onRightSendMessage() {
        String text = getSendText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(ChatActivity.this, "输入内容空!", Toast.LENGTH_SHORT).show();
            return;
        } else {

        }
    }
}
