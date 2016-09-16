package com.codesky.radboss.util;

import android.text.TextUtils;

import com.codesky.radboss.RadGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class FileUtil {

    public static JSONObject loadJsonObject(String fileName) {
        String jsonStr = loadAssetsFile(fileName);
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                return new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONArray loadJsonArray(String fileName) {
        String jsonStr = loadAssetsFile(fileName);
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                return new JSONArray(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String loadAssetsFile(String fileName) {
        try {
            InputStream inStream = RadGlobal.getAppContext().getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            char[] buf = new char[4096];
            int count = 0;
            StringBuilder builder = new StringBuilder();
            while ((count = reader.read(buf)) > 0) {
                builder.append(buf, 0, count);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
