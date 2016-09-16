package com.codesky.radboss.example;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatBean> mData;

    public ChatAdapter(Context context, List<ChatBean> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List<ChatBean> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData == null ? null : mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
