package com.tg.iweibo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-14
 * Time: 23:50
 * UserInfo的数据库帮助类
 */

public class UserInfoDbHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "userinfo.db";
    private static final int VERSION = 1;
    private static final String TABLENAME = "userinfo";

    public UserInfoDbHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if exists "+this.TABLENAME+"( _id integer primary key autoincrement, userName TEXT," +
                    " accountType TEXT, token TEXT, uid TEXT, isDefault TEXT, userIcon BLOB)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "+this.TABLENAME;
        db.execSQL(sql);
        this.onCreate(db);
    }
}
