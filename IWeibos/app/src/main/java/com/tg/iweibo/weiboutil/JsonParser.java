package com.tg.iweibo.weiboutil;

import android.text.TextUtils;

import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;
import com.tg.iweibo.domain.Comment;
import com.tg.iweibo.domain.UserInfo;
import com.tg.iweibo.domain.WeiboInfos;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-19
 * Time: 15:19
 */

public class JsonParser {


    /**
     * 读取数据方法
     * @param is
     * @return
     * @throws java.io.IOException
     */
    private static byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        is.close();
        bout.close();
        return bout.toByteArray();
    }


    //获取当前登录用户及其所关注用户的最新微博的JSON数据
    public static List<WeiboInfos> friendsTimeLine_To_Map(HttpEntity entity) throws IOException, JSONException {
        //取得json格式的数据

        List<WeiboInfos> weiboInfos = new ArrayList<WeiboInfos>();
        byte[] byteData = readStream(entity.getContent());
        String strData = new String(byteData);
        JSONObject jsonObject = new JSONObject(strData);
        JSONArray jsonArray = jsonObject.getJSONArray("statuses");
        for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonItem = jsonArray.getJSONObject(i);
            WeiboInfos infos = new WeiboInfos();
            infos.setCreated_at(jsonItem.getString("created_at"));
            infos.setId(jsonItem.getString("id"));
            infos.setText(jsonItem.getString("text"));
            infos.setSource(jsonItem.getString("source"));
            infos.setFavorited(jsonItem.getBoolean("favorited"));
            infos.setTruncated(jsonItem.getBoolean("truncated"));
            infos.setIn_reply_to_user_id(jsonItem.getString("in_reply_to_user_id"));
            infos.setIn_reply_to_screen_name(jsonItem.getString("in_reply_to_screen_name"));
            infos.setGeo(jsonItem.getString("geo"));
            infos.setMid(jsonItem.getString("mid"));
            infos.setReposts_count(jsonItem.getInt("reposts_count"));
            infos.setComments_count(jsonItem.getInt("comments_count"));

           if(!TextUtils.isEmpty(jsonItem.optString("thumbnail_pic"))){
                infos.setThumbnail_pic(jsonItem.optString("thumbnail_pic"));
            }
            if(!TextUtils.isEmpty(jsonItem.optString("bmiddle_pic"))){
                infos.setBmiddle_pic(jsonItem.optString("bmiddle_pic"));
            }
            if(!TextUtils.isEmpty(jsonItem.optString("original_pic"))){
                infos.setOriginal_pic(jsonItem.optString("original_pic"));
            }
            if(null != jsonItem.optJSONObject("retweeted_status")) {
                infos.setRetweeted_status(jsonItem.optJSONObject("retweeted_status"));
            }
            UserInfo userInfo = new UserInfo();
            JSONObject userInfoItem = jsonItem.getJSONObject("user");


            userInfo.setId(userInfoItem.getInt("id"));
            userInfo.setScreen_name(userInfoItem.getString("screen_name"));
            userInfo.setName(userInfoItem.getString("name"));
            userInfo.setProvince(userInfoItem.getString("province"));
            userInfo.setCity(userInfoItem.getString("city"));
            userInfo.setLocation(userInfoItem.getString("location"));
            userInfo.setDescription(userInfoItem.getString("description"));
            userInfo.setUrl(userInfoItem.getString("url"));
            userInfo.setProfile_image_url(userInfoItem.getString("profile_image_url"));
            userInfo.setDomain(userInfoItem.getString("domain"));
            userInfo.setGender(userInfoItem.getString("gender"));
            userInfo.setBi_followers_count(userInfoItem.getInt("followers_count"));
            userInfo.setFriends_count(userInfoItem.getInt("friends_count"));
            userInfo.setCreated_at(userInfoItem.getString("created_at"));
            userInfo.setFollowing(userInfoItem.getBoolean("following"));
            userInfo.setAllow_all_act_msg(userInfoItem.getBoolean("allow_all_act_msg"));
            userInfo.setGeo_enabled(userInfoItem.getBoolean("geo_enabled"));
            userInfo.setVerified(userInfoItem.getBoolean("verified"));
            userInfo.setAllow_all_comment(userInfoItem.getBoolean("allow_all_comment"));
            userInfo.setFollow_me(userInfoItem.getBoolean("follow_me"));
            userInfo.setRemark(userInfoItem.getString("remark"));
            userInfo.setAvatar_large(userInfoItem.getString("avatar_large"));
            userInfo.setVerified_reason(userInfoItem.getString("verified_reason"));
            userInfo.setOnline_status(userInfoItem.getInt("online_status"));
            userInfo.setBi_followers_count(userInfoItem.getInt("bi_followers_count"));
            
            
            infos.setUserInfo(userInfo);
            weiboInfos.add(infos);
        }

        return weiboInfos;
    }




    public  static List<Comment> comments_Timeline_To_List(HttpEntity entity) throws Exception{

        byte[] dataByte = readStream(entity.getContent());
        String dataStr = new String(dataByte);
        List<Comment> list = new ArrayList<Comment>();

        JSONObject jsonObject = new JSONObject(dataStr);
        JSONArray commentsArr = jsonObject.getJSONArray("comments");

        for (int i=0; i<commentsArr.length(); i++){
            JSONObject jsonObject = (JSONObject) commentsArr.get(i);
            Comment comment = new Comment();
            comment.setCreated_at(jsonObject.getString(""));
            comment.setId(jsonObject.getInt());
            comment.setText(jsonObject.getString());
            comment.setSource(jsonObject.getString());


            JSONObject jsonUser = jsonObject.getJSONObject("user");
            UserInfo userInfo = new UserInfo();
            userInfo.setId(jsonUser.getInt(""));
            userInfo.setScreen_name(jsonUser.getString(""));
            userInfo.setName(jsonUser.getString(""));
            userInfo.setProvince(jsonUser.getString(""));
            userInfo.setCity(jsonUser.getString(""));
            userInfo.setLocation(jsonUser.getString(""));
            userInfo.setDescription(jsonUser.getString(""));
            userInfo.setUrl(jsonUser.getString());
            userInfo.setProfile_image_url(jsonUser.getString(""));
            userInfo.setDomain(jsonUser.getString(""));
            userInfo.setGender(jsonUser.getString(""));
            userInfo.setFollowers_count(jsonUser.getInt(""));
            userInfo.setFriends_count(jsonUser.getInt(""));
            userInfo.setCreated_at(jsonUser.optString(""));

            userInfo.setFollowing(jsonUser.getBoolean(""));
            userInfo.setAllow_all_act_msg(jsonUser.getBoolean(""));
            userInfo.setGeo_enabled(jsonUser.getBoolean(""));
            userInfo.setVerified(jsonUser.getBoolean(""));
            userInfo.setAllow_all_comment(jsonUser.getBoolean(""));
            userInfo.setFollow_me(jsonUser.getBoolean(""));

            userInfo.setRemark(jsonUser.getString(""));
            userInfo.setAvatar_large(jsonUser.getString(""));
            userInfo.setVerified_reason(jsonUser.getString(""));
            userInfo.setOnline_status(jsonUser.getInt(""));
            userInfo.setBi_followers_count(jsonUser.getInt());


            comment.setUserInfo(userInfo);

            list.add(comment);
        }



        return list;



    }





}
