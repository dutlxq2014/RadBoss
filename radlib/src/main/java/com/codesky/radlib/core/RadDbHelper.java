package com.codesky.radlib.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


import com.codesky.radlib.support.ERadTab;
import com.codesky.radlib.support.RadDBConst;
import com.codesky.radlib.support.RadDbUtil;
import com.codesky.radlib.support.RadLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Sqlite storage helper.
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadDbHelper extends SQLiteOpenHelper {

    public static final String TAG = RadDbHelper.class.getSimpleName();

    /** 数据库文件 */
    public static final String DATABASE_NAME = "rad.db";

    /** 数据库版本 */
    public static final int DATABASE_VERSION = 1;

    public static final String MAIN_TABLE_NAME = ERadTab.MAIN.table();
    public static final String BUFF_TABLE_NAME = ERadTab.BUFF.table();
    private static final ColumnDef[] MAIN_TABLE_COLUMN;
    private static final ColumnDef[] BUFF_TABLE_COLUMN;
    private Context mAppContext;

    static {
        // 主记录表
        MAIN_TABLE_COLUMN = new ColumnDef[] {
                // _id 默认
                new ColumnDef(RadDBConst.RECORD_KEY, "text", "\"\""),
                new ColumnDef(RadDBConst.RECORD_DATA, "BLOB", "\"\""),
                new ColumnDef(RadDBConst.RECORD_DS, "char(16)", "\"\""),
                new ColumnDef(RadDBConst.RECORD_SIZE, "long", "0")
        };
        BUFF_TABLE_COLUMN = MAIN_TABLE_COLUMN;
    }

    /**
     * 数据库表列的定义
     */
    private static class ColumnDef {

        public String columnName;
        public String type;
        public String defValue;

        public ColumnDef(String columnName, String type, String defValue) {
            this.columnName = columnName;
            this.type = type;
            this.defValue = defValue;
        }
        public String toString() {
            return String.format("%s %s not null default %s", columnName, type, defValue);
        }
    }

    public RadDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public RadDbHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory) {
        this(context, dbName, factory, DATABASE_VERSION);
    }

    public RadDbHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        super(context, dbName, factory, dbVersion);
        mAppContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1) {
            // Do something like add columns
        }

        //alterTable(db, MAIN_TABLE_NAME, new ColumnDef[]{new ColumnDef("newColumn","char(8)","\"\"")});
        RadLog.i(TAG, "Update database version from " + oldVersion + " to " + newVersion);
    }

    public void createTables(SQLiteDatabase db) {
        createTableIfNotExists(db, MAIN_TABLE_NAME, MAIN_TABLE_COLUMN);
        createTableIfNotExists(db, BUFF_TABLE_NAME, BUFF_TABLE_COLUMN);
    }

    public void ensureTableExists(ERadTab table) {
        switch (table) {
            case MAIN:
                createTableIfNotExists(getWritableDatabase(), table.table(), MAIN_TABLE_COLUMN);
                break;
            case BUFF:
                createTableIfNotExists(getWritableDatabase(), table.table(), BUFF_TABLE_COLUMN);
                break;
            default:
                // ignored
        }
    }

    private void createTableIfNotExists(SQLiteDatabase db, String tableName, ColumnDef[] columnDefs, String... primaryKey) {
        Cursor cursor = null;
        try {
            StringBuilder sql = new StringBuilder(32);
            sql.append("select count(1) from sqlite_master where type='table' and name='").append(tableName).append("';");
            cursor = getWritableDatabase().rawQuery(sql.toString(), null);
            if (cursor.moveToNext()) {
                int cnt = cursor.getInt(0);
                if (cnt > 0) {
                    return;
                }
            }
            String createTableSql = buildCreateTableString(tableName, columnDefs, primaryKey);
            db.execSQL(createTableSql);
            RadLog.i(TAG, "Create table : " + tableName);
        } catch (Exception e) {
            RadLog.e(TAG, "create table exception: " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(cursor);
        }
    }

    /**
     * 结构化建表
     * @param tableName 表名
     * @param columns 没列数据格式
     * @param primaryKeys 主键
     * @return 构造表的sql语句
     */
    private String buildCreateTableString(String tableName, ColumnDef[] columns, String... primaryKeys) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(tableName);

        if (primaryKeys != null && primaryKeys.length > 0) {
            stringBuilder.append(" ( ");
        } else {
            stringBuilder.append(" (_id integer primary key autoincrement, ");
        }

        for (int j = 0; j < columns.length; j++) {
            stringBuilder.append(columns[j]);

            if ((j + 1) < columns.length) {
                stringBuilder.append(", ");
            }
        }

        if (primaryKeys != null && primaryKeys.length > 0) {
            stringBuilder.append(", primary key(");

            for (int i = 0; i < primaryKeys.length; i++) {
                stringBuilder.append(primaryKeys[i]);

                if ((i + 1) < primaryKeys.length) {
                    stringBuilder.append(", ");
                }
            }

            stringBuilder.append(")");
        }

        stringBuilder.append(");");

        return stringBuilder.toString();
    }

    /**
     * 修改表结构
     */
    private void alterTable(SQLiteDatabase db, String tableName, ColumnDef[] addColumns) {

        if (TextUtils.isEmpty(tableName) || addColumns==null || addColumns.length==0)
            return;

        StringBuilder sb = new StringBuilder();
        Set<String> set = getColumnSet(db, tableName);
        for (int i=0; i<addColumns.length; ++i) {
            ColumnDef column = addColumns[i];
            if (!set.contains(column.columnName)) {
                sb.setLength(0);
                RadLog.i(TAG, sb.append("alter table add column ").append(addColumns[i]).append(" to ").append(tableName).toString());
                sb.setLength(0);
                db.execSQL(sb.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ").append(column).append(";").toString());
            }
        }
    }

    private Set<String> getColumnSet(SQLiteDatabase db, String tableName) {
        HashSet<String> hashSet = new HashSet<String>();
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, null, null, null, null, null, null);
            if (cursor != null) {
                for(int i=0; i<cursor.getColumnCount(); ++i) {
                    hashSet.add(cursor.getColumnName(i));
                }
            }
        } catch (Exception e) {
            RadLog.e(TAG, "getColumnSet exception: " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(cursor);
        }
        return hashSet;
    }

    /**
     * 数据库文件丢失的情况下从Assets目录拷贝数据库文件
     */
    private void cloneDbFileFromAssetsIfAbsent() {

        // 判断数据库文件是否存在
        File file = mAppContext.getDatabasePath(DATABASE_NAME);
        if (file.exists()) {
            return;
        }

        // 尝试创建数据库目录
        String path = file.getPath();
        String dirStr = path.substring(0, path.lastIndexOf(File.separatorChar));
        File dir = new File(dirStr);
        if (!dir.exists() && dir.mkdirs()) {
            RadLog.i(TAG, "database directory not exists, successfully create one");
        } else {
            RadLog.i(TAG, "database directory already exists");
        }

        // 复制数据库文件
        FileOutputStream fos = null;
        InputStream fis = null;
        try {
            fos = new FileOutputStream(path);
            fis = mAppContext.getAssets().open(DATABASE_NAME, AssetManager.ACCESS_STREAMING);
            byte[] buf = new byte[1024];
            int count;
            while ((count = fis.read(buf)) > 0) {
                fos.write(buf, 0, count);
            }
            fos.flush();
        } catch (Exception e) {
            RadLog.e(TAG, "Clone database file exception : " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(fos);
            RadDbUtil.closeQuietly(fis);
        }
    }

}
