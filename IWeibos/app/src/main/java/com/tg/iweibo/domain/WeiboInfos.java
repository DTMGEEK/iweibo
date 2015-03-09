package com.tg.iweibo.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-28
 * Time: 09:40
 */

public class WeiboInfos implements Parcelable {


    private String created_at;
    private String id;
    private String text;
    private String source;
    private boolean favorited;
    private boolean truncated;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;
    private String geo;
    private String mid;
    private int reposts_count;
    private int comments_count;

    //缩略图片地址，没有时不返回此字段
    private String thumbnail_pic ;
   //中等尺寸图片地址，没有时不返回此字段
    private String bmiddle_pic;
   //	原始图片地址，没有时不返回此字段
    private String original_pic;

    //被转发的原微博信息字段，当该微博为转发微博时返回
    private Object retweeted_status;


    public Object getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Object retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    private UserInfo userInfo;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }





    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
