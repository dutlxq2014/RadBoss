package com.codesky.radboss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codesky.radboss.example.MemberAdapter;
import com.codesky.radboss.example.MemberBean;
import com.codesky.radboss.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final int REQUEST_CODE_TO_CHAT = 1001;

    ListView mMemberListView;
    MemberAdapter mMemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMemberListView = (ListView) findViewById(R.id.chat_member_list);
        mMemberAdapter = new MemberAdapter(this, requestMembers());
        mMemberListView.setAdapter(mMemberAdapter);
        mMemberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object data = adapterView.getAdapter().getItem(i);
                if (data instanceof MemberBean) {
                    MemberBean bean = (MemberBean) data;
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.CHAT_ICON, bean.iconId);
                    intent.putExtra(ChatActivity.CHAT_NAME, bean.name);
                    startActivityForResult(intent, REQUEST_CODE_TO_CHAT);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TO_CHAT) {
            if (resultCode == RESULT_OK && data != null) {
                String lastMsg = data.getStringExtra(ChatActivity.CHAT_LAST_MSG);
                String name = data.getStringExtra(ChatActivity.CHAT_NAME);
                // update data here
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<MemberBean> requestMembers() {
        List<MemberBean> datas = new ArrayList<MemberBean>();
        JSONArray src = FileUtil.loadJsonArray("members.json");
        if (src != null && src.length() > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
            for (int i=0; i<src.length(); ++i) {
                try {
                    JSONObject ith = src.getJSONObject(i);
                    MemberBean bean = new MemberBean();
                    bean.name = ith.getString("name");
                    bean.icon = ith.getString("icon");
                    bean.iconId = getResources().getIdentifier(bean.icon, "mipmap", getPackageName());
                    bean.time = formatter.format(new Date());
                    datas.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
