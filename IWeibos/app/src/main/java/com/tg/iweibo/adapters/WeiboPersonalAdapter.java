package com.tg.iweibo.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tg.iweibo.R;
import com.tg.iweibo.activity.ScaleImgActivity;
import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.imagecache.SimpleImageLoader;
import com.tg.iweibo.weibocontentrplace.SimpleWeiboContentLoader;

import java.util.List;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-27
 * Time: 15:45
 */

public class WeiboPersonalAdapter extends BaseAdapter {


    private List<WeiboInfos> weiboPersonalDateList;

    private Context context;

    private Dialog imgDialog = null;

    Button weibo_img_dig_bn = null;

    ImageView weibo_img_dig_iv = null;

    ProgressBar weibo_img_dig_pb = null;

    //原始图片地址，没有时不返回此字段
    private String original_pic = null;

    private Intent scaleImgIntent = null;

    public WeiboPersonalAdapter(Context context, List weiboPersonalDateList){
        this.context = context;
        this.weiboPersonalDateList = weiboPersonalDateList;

        scaleImgIntent = new Intent(context, ScaleImgActivity.class);


       /* //在这里实例化，可以减少每次点击都实例化一次，达到了重用的目的
        this.imgDialog = new Dialog(context,R.layout.weibo_img_dig);
        View view = LayoutInflater.from(context).inflate(R.layout.weibo_img_dig,null);
        weibo_img_dig_iv = (ImageView) view.findViewById(R.id.weibo_img_dig_iv);
        weibo_img_dig_pb = (ProgressBar) view.findViewById(R.id.weibo_img_dig_pb);
        weibo_img_dig_bn = (Button) view.findViewById(R.id.weibo_img_dig_bn);
        this.imgDialog.setContentView(view);*/
    }


    @Override
    public int getCount()
    {
        return weiboPersonalDateList.size();
    }


    @Override
    public Object getItem(int position)
    {
        return weiboPersonalDateList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

           View view = createViewFromResource(position,convertView);
        return view;
    }


    private class Holder{
         ImageView itme_blog_iv_head;
         TextView item_blog_tv_uname;
         ImageView item_blog_iv_V;
         TextView item_blog_tv_time;
         TextView item_blog_tv_content;
         ImageView item_blog_iv_pic;
         LinearLayout item_blog_ll_sublayout;
         TextView item_blog_tv_subcontent;
         ImageView item_blog_iv_content_subpic;
         TextView item_blog_tv_from;
         TextView item_blog_tv_redirect;
         TextView item_blog_tv_comment;
    }



    //判断convertView是否可以重复利用
    private View  createViewFromResource(int position,View convertView){
        View view = null;

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_blog,null);
        }else{
            view = convertView;
        }

        bindView(position,view);
        return view;
    }


    //为view布局上的控件赋值
    private void bindView(int position, View view) {

        WeiboInfos weiboinfo = weiboPersonalDateList.get(position);
        Holder holder = null;

        if(null == holder) {
            holder = new Holder();
            holder.itme_blog_iv_head = (ImageView) view.findViewById(R.id.itme_blog_iv_head);
            holder.item_blog_tv_uname = (TextView) view.findViewById(R.id.item_blog_tv_uname);
            holder.item_blog_iv_V = (ImageView) view.findViewById(R.id.item_blog_iv_V);
            holder.item_blog_tv_content = (TextView) view.findViewById(R.id.item_blog_tv_content);
            holder.item_blog_iv_pic = (ImageView) view.findViewById(R.id.item_blog_iv_pic);
            holder.item_blog_ll_sublayout = (LinearLayout) view.findViewById(R.id.item_blog_ll_sublayout);
            holder.item_blog_tv_subcontent = (TextView) view.findViewById(R.id.item_blog_tv_subcontent);
            holder.item_blog_iv_content_subpic = (ImageView) view.findViewById(R.id.item_blog_iv_content_subpic);
            holder.item_blog_tv_from = (TextView) view.findViewById(R.id.item_blog_tv_from);
            holder.item_blog_tv_redirect = (TextView) view.findViewById(R.id.item_blog_tv_redirect);
            holder.item_blog_tv_comment = (TextView) view.findViewById(R.id.item_blog_tv_comment);
            view.setTag(holder);
        }else{
             holder = (Holder) view.getTag();
        }

        //使用图片缓存，没有就开启线程下载，并保存到文件中
        SimpleImageLoader.showImage(weiboinfo.getUserInfo().getProfile_image_url().toString(),holder.itme_blog_iv_head);

         //holder.itme_blog_iv_head.setImageResource(weiboinfo.get);
        holder.item_blog_tv_uname.setText(weiboinfo.getUserInfo().getScreen_name());
        if(weiboinfo.getUserInfo().isVerified()){ //判断用户是否的加V认证用户
            // holder.item_blog_iv_V.setImageResource();
            holder.item_blog_iv_V.setVisibility(View.VISIBLE); //显示v认证图片
        }


        //holder.item_blog_tv_content.setText(weiboinfo.getText());
        SimpleWeiboContentLoader.replaceContent(weiboinfo.getText(),holder.item_blog_tv_content);

        //判断微博中是否含有图片
        if(null != weiboinfo.getOriginal_pic()){
            holder.item_blog_iv_pic.setVisibility(View.VISIBLE);

            this.original_pic = weiboinfo.getOriginal_pic();

            //使用图片缓存，没有就开启线程下载，并保存到文件中
            SimpleImageLoader.showImage(weiboinfo.getThumbnail_pic().toString(),holder.item_blog_iv_pic);
            // holder.item_blog_iv_pic.setImageResource();

            //为图片设置点击事件
            holder.item_blog_iv_pic.setOnClickListener(new ImageOnClickListener());
        }

        // 判断是否是转发
        if(null != weiboinfo.getRetweeted_status()){
            holder.item_blog_tv_subcontent.setVisibility(View.VISIBLE);
            //holder.item_blog_tv_subcontent.setText(weiboinfo.get);
        }

        //holder.item_blog_iv_content_subpic.setImageResource();
        holder.item_blog_tv_from.setText(Html.fromHtml(weiboinfo.getSource().toString()));
        holder.item_blog_tv_redirect.setText(String.valueOf(weiboinfo.getReposts_count()));
        holder.item_blog_tv_comment.setText(String.valueOf(weiboinfo.getComments_count()));
    }





    //设置点击事件的内部类
    private  class ImageOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
           
            //imgDialog.show();
            String bimMap_url = original_pic;
            scaleImgIntent.putExtra("bimMap_url",bimMap_url);
            context.startActivity(scaleImgIntent);

        }
    }


}
