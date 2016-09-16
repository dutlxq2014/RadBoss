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
public class MemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<MemberBean> mData;

    public MemberAdapter(Context context, List<MemberBean> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List<MemberBean> data) {
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
        ViewHolder holder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            view = LayoutInflater.from(mContext).inflate(R.layout.member_item_layout, viewGroup, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.member_icon);
            holder.name = (TextView) view.findViewById(R.id.member_name);
            holder.lastMsg = (TextView) view.findViewById(R.id.member_last_msg);
            holder.time = (TextView) view.findViewById(R.id.member_time);
            view.setTag(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MemberBean bean = mData.get(i);
        holder.icon.setImageResource(bean.iconId);
        holder.name.setText(bean.name);
        holder.lastMsg.setText(bean.lasgMsg);
        holder.time.setText(bean.time);
        return view;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView lastMsg;
        public TextView time;
    }
}
