package com.tg.iweibo.domain;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-07
 * Time: 15:28
 */

public class Comment {
    //评论创建时间
    private String created_at;
    //评论的ID
    private int id;
    //评论的内容
    private String text;
    //评论的来源
    private String source;
    //评论作者的用户信息字段
    private UserInfo userInfo;
    //评论的MID
    private String mid;
    //字符串型的评论ID
    private String idstr;
    //评论的微博信息字段
    private Status status;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
