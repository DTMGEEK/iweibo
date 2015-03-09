package com.tg.iweibo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tg.iweibo.db.UserInfoDbHelper;
import com.tg.iweibo.domain.UserInfo;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-15
 * Time: 00:25
 */

public class UserInfoDao {

   /* private UserInfoDbHelper helper = null;
    private SQLiteDatabase db = null;

    public UserInfoDao(Context context){

        this.helper = new UserInfoDbHelper(context);
        this.db = this.helper.getWritableDatabase();

    }

    //判断数据库是否打开，没有就打开数据库
    public void isOpenDB(){
        if(null == this.db){
            this.db = this.helper.getWritableDatabase();
        }
    }


    *//**
     * 添加新用户信息
     * @param userInfo 保存用户信息的实体
     *//*
    public void addUserInfo(UserInfo userInfo){
        this.isOpenDB();
        try{
            if(this.db.isOpen()) {
                String sql = "insert into userinfo(userName,accountType,token,uid,isDefault,userIcon) values(?,?,?,?,?,?)";
                Object[] args = new Object[]{userInfo.getUserName(),userInfo.getAccountType(),userInfo.getToken(),
                        userInfo.getUid(),userInfo.getIsDefault(),userInfo.getUserIcon()};
                this.db.beginTransaction();
                this.db.execSQL(sql, args);
                this.db.setTransactionSuccessful();
            }
        }catch(Exception e){
            e.printStackTrace();
            this.db.endTransaction();
        }finally {
            this.db.endTransaction();
            this.db.close();
        }
    }


    *//**
     * 删除用户信息
     * @param _id
     *//*
    public void deleteUserInfo(int _id){
        this.isOpenDB();
        if(this.db.isOpen()) {
            String sql = "delete from userinfo wherer _id = ?";
            if (this.db.isOpen()) {
                this.db.execSQL(sql, new Object[]{_id});
                this.db.close();
            }
        }
    }


    *//**
     * 更新用户信息
     * @param userInfo 保存用户信息的实体
     *//*
    public void updateUserInfo(UserInfo userInfo){
        this.isOpenDB();
        try {
            if (this.db.isOpen()) {
                String sql = "update userinfo set userName = ?,accountType = ?,token = ?,uid = ?," +
                        "isDefault = ?,userIcon = ? where  _id = ?";
                Object[] args = new Object[]{userInfo.getUserName(),userInfo.getAccountType(),userInfo.getToken(),
                        userInfo.getUid(),userInfo.getIsDefault(),userInfo.getUserIcon(),userInfo.get_id()};
                this.db.execSQL(sql,args);

            }
        }catch(Exception e){
            e.printStackTrace();
            this.db.endTransaction();
        }finally {
            this.db.endTransaction();
            this.db.close();
        }
    }


    *//**
     * 根据用户id获得用户的信息
     * @param userId  用户id
     * @return 保存了用户信息的实体
     *//*
    public UserInfo getUserInfoByUserId(String userId){
           this.isOpenDB();
           UserInfo userInfo = null;
           if(this.db.isOpen()){
               String sql = "select _id, userName, accountType, token, isDefault where uid = ?";
               String[] args = new String[]{userId};
               Cursor cursor = this.db.rawQuery(sql,args);
               while(cursor.moveToNext()){
                   userInfo = new UserInfo();
                   userInfo.set_id(cursor.getLong(cursor.getColumnIndex("_id")));
                   userInfo.setAccountType(cursor.getString(cursor.getColumnIndex("accountType")));
                   userInfo.setIsDefault(cursor.getString(cursor.getColumnIndex("isDefault")));
                   userInfo.setToken(cursor.getString(cursor.getColumnIndex("token")));
                   userInfo.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
               }
               cursor.close();
               this.db.close();
           }
        return userInfo;
    }




*/

}
