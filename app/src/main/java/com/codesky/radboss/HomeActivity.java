package com.codesky.radboss;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codesky.radboss.core.RadCallback;
import com.codesky.radboss.core.RadResult;
import com.codesky.radboss.core.RadDispatcher;
import com.codesky.radboss.core.TableStruct;
import com.codesky.radboss.mock.Mock;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    ListView mAllListView;
    RecordAdapter mAllAdapter;
    ListView mResListView;
    RecordAdapter mResAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.insert_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues record = Mock.singleInsert();
                RadDispatcher.getInstance().insertAsync(record, new RadCallback<ContentValues>() {
                    @Override
                    public void onResult(RadResult<ContentValues> result) {
                        Log.i(TAG, "Callback from insert_one");
                        displayResult(Collections.singletonList(result.getData()));
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.insert_n).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ContentValues> records = Mock.multiInsert();
                RadDispatcher.getInstance().insertAsync(records, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from insert_n");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.delete_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues key = Mock.singleDelete();
                RadDispatcher.getInstance().deleteAsync(key, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from delete_one");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.delete_n).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ContentValues> keys = Mock.multiDelete();
                RadDispatcher.getInstance().deleteAsync(keys, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from delete_n");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.modify_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues record = Mock.singleModify();
                RadDispatcher.getInstance().modifyAsync(record, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from modify_one");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.modify_n).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ContentValues> records = Mock.multiModify();
                RadDispatcher.getInstance().modifyAsync(records, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from modify_n");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.query_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues key = Mock.singleQuery();
                RadDispatcher.getInstance().queryAsync(key, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from query_one");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        findViewById(R.id.query_n).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ContentValues> keys = Mock.multiQuery();
                RadDispatcher.getInstance().queryAsync(keys, new RadCallback<List<ContentValues>>() {
                    @Override
                    public void onResult(RadResult<List<ContentValues>> result) {
                        Log.i(TAG, "Callback from query_n");
                        displayResult(result.getData());
                        dumpStorage();
                    }
                });
            }
        });

        mAllListView = (ListView) findViewById(R.id.all_record_list);
        mAllAdapter = new RecordAdapter(this);
        mAllListView.setAdapter(mAllAdapter);

        mResListView = (ListView) findViewById(R.id.result_record_list);
        mResAdapter = new RecordAdapter(this);
        mResListView.setAdapter(mResAdapter);
    }

    private void displayResult(List<ContentValues> result) {
        mResAdapter.setData(result);
        mResAdapter.notifyDataSetChanged();
    }

    private void dumpStorage() {
        ContentValues key = new ContentValues();
        key.put(TableStruct.KEY, "*");
        RadDispatcher.getInstance().queryAsync(key, new RadCallback<List<ContentValues>>() {
            @Override
            public void onResult(RadResult<List<ContentValues>> result) {
                mAllAdapter.setData(result.getData());
                mAllAdapter.notifyDataSetChanged();
            }
        });
    }

    private static class RecordAdapter extends BaseAdapter {

        Context mContext;
        List<ContentValues> mData;

        public RecordAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<ContentValues> data) {
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
            ContentValues data = mData.get(i);
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.record_row, viewGroup, false);
                ViewHolder holder = new ViewHolder();
                holder.id = (TextView) view.findViewById(R.id.id);
                holder.key = (TextView) view.findViewById(R.id.key);
                holder.value = (TextView) view.findViewById(R.id.value);
                view.setTag(holder);
            }
            ViewHolder vHolder = (ViewHolder) view.getTag();
            vHolder.id.setText(String.valueOf(i));
            vHolder.key.setText(data.getAsString(TableStruct.KEY));
            vHolder.value.setText(new String(data.getAsByteArray(TableStruct.VALUE)));
            return view;
        }
    }

    private static class ViewHolder {
        public TextView id;
        public TextView key;
        public TextView value;
    }
}
