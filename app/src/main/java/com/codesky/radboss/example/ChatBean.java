package com.codesky.radboss.example;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class ChatBean {

    public static final String KEY_WHO = "who";
    public static final String KEY_TEXT = "text";

    public enum WHO {
        LEFT("left"), RIGHT("right");

        private String who;
        WHO(String who) {
            this.who = who;
        }
        @Override
        public String toString() {
            return who;
        }

    }


    public WHO who;

    public String text;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_TEXT, text);
        map.put(KEY_WHO, who != null ? who.toString() : WHO.RIGHT.toString());
        return map;
    }

    public static ChatBean fromJson(JSONObject jsonObj) {
        if (jsonObj != null) {
            ChatBean bean = new ChatBean();
            try {
                bean.text = jsonObj.getString(KEY_TEXT);
                if (WHO.LEFT.toString().equals(jsonObj.getString(KEY_WHO))) {
                    bean.who = WHO.LEFT;
                } else {
                    bean.who = WHO.RIGHT;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }
        return null;
    }
}
