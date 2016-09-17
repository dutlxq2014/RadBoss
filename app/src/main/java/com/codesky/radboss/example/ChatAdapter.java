package com.codesky.radboss.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codesky.radboss.R;

import java.util.List;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatBean> mData;
    private int heIcon;
    private int meIcon;

    public ChatAdapter(Context context, List<ChatBean> data, int heIcon, int meIcon) {
        mContext = context;
        mData = data;
        this.heIcon = heIcon;
        this.meIcon = meIcon;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).who.ordinal();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null || (view.getTag() instanceof ViewHolder)) {
            if (getItemViewType(i) == ChatBean.WHO.LEFT.ordinal()) {
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left_layout, viewGroup, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right_layout, viewGroup, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.chat_icon);
            viewHolder.text = (TextView) view.findViewById(R.id.chat_text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ChatBean bean = mData.get(i);
        if (getItemViewType(i) == ChatBean.WHO.LEFT.ordinal()) {
            viewHolder.icon.setImageResource(heIcon);
        } else {
            viewHolder.icon.setImageResource(meIcon);
        }
        viewHolder.text.setText(bean.text);

        return view;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView text;
    }
}
