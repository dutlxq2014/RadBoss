package com.codesky.radboss.example;

import com.codesky.radboss.util.SafeJsonUtil;
import com.codesky.radlib.support.RadRecord;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by xueqiulxq on 9/17/16.
 */
public class ChatTrans {

    public static List<ChatBean> toChatBean(List<RadRecord> input) {
        List<ChatBean> output = new ArrayList<ChatBean>();
        if (input != null && input.size() > 0 ) {
            for (RadRecord record : input) {
                JSONObject jsonObj = SafeJsonUtil.fromJsonObj(record.data);
                ChatBean bean = ChatBean.fromJson(jsonObj);
                if (bean != null) {
                    output.add(bean);
                }
            }
        }
        return output;
    }
}
