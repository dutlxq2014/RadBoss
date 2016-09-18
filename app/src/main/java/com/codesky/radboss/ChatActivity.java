package com.codesky.radboss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codesky.radboss.example.ChatAdapter;
import com.codesky.radboss.example.ChatBean;
import com.codesky.radboss.example.ChatDataService;
import com.codesky.radboss.example.DataResult;
import com.codesky.radboss.example.IServiceCallback;
import com.codesky.radlib.support.ERadTab;

import java.util.ArrayList;
import java.util.List;

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
    private List<ChatBean> mChatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mName = getIntent().getStringExtra(CHAT_NAME);
        heIcon = getIntent().getIntExtra(CHAT_ICON, 0);
        meIcon = R.drawable.monkey;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.chat_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendLeftMessage();
            }
        });

        findViewById(R.id.chat_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendRightMessage();
            }
        });

        mEditText = (EditText) findViewById(R.id.chat_text);
        mChatListView = (ListView) findViewById(R.id.chat_content_list) ;

        mChatData = new ArrayList<ChatBean>();
        mChatAdapter = new ChatAdapter(this, mChatData, heIcon, meIcon);
        mChatListView.setAdapter(mChatAdapter);

        queryHistoryMessage(mName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.chat_action_clear:
                onClearChatHistory(mName);
                return true;
            case R.id.chat_action_more:
                Toast.makeText(ChatActivity.this, "More to be done.", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getSendText() {
        String text = mEditText.getText().toString();
        mEditText.setText("");
        return text;
    }

    public void onSendLeftMessage() {
        String text = getSendText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(ChatActivity.this, "Say something.", Toast.LENGTH_SHORT).show();
        } else {
            appendMessage(text, ChatBean.WHO.LEFT);
        }
    }

    public void onSendRightMessage() {
        String text = getSendText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(ChatActivity.this, "Say something.", Toast.LENGTH_SHORT).show();
        } else {
            appendMessage(text, ChatBean.WHO.RIGHT);
        }
    }

    public void appendMessage(String text, ChatBean.WHO who) {
        final ChatBean bean = new ChatBean();
        bean.who = who;
        bean.text = text;
        ChatDataService.getInstance().append(ERadTab.MAIN, mName, bean, new IServiceCallback<List<ChatBean>>() {
            @Override
            public void onResult(DataResult<List<ChatBean>> result) {
                if (result.isSuccess()) {
                    mChatData.add(bean);
                    mChatAdapter.setData(mChatData);
                    mChatAdapter.notifyDataSetChanged();
                    mChatListView.setSelection(mChatData.size());
                }
            }
        });
    }

    public void queryHistoryMessage(String key) {
        ChatDataService.getInstance().query(ERadTab.MAIN, key, new IServiceCallback<List<ChatBean>>() {
            @Override
            public void onResult(DataResult<List<ChatBean>> result) {
                if (result.isSuccess()) {
                    mChatData = result.getData();
                    mChatAdapter.setData(mChatData);
                    mChatAdapter.notifyDataSetChanged();
                    mChatListView.setSelection(mChatData.size());
                }
            }
        });
    }

    public void onClearChatHistory(String key) {
        ChatDataService.getInstance().delete(ERadTab.MAIN, key, new IServiceCallback<List<ChatBean>>() {
            @Override
            public void onResult(DataResult<List<ChatBean>> result) {
                if (result.isSuccess()) {
                    mChatData.clear();
                    mChatAdapter.setData(mChatData);
                    mChatAdapter.notifyDataSetChanged();
                    Toast.makeText(ChatActivity.this, "Clear " + result.getData().size() + " records.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
