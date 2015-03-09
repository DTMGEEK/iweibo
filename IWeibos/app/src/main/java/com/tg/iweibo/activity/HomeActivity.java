package com.tg.iweibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.tg.iweibo.R;
import com.tg.iweibo.domain.Task;
import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.engine.MainService;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.utils.NetUtil;
import java.util.ArrayList;
import java.util.List;


//微博首页类
public class HomeActivity extends FragmentActivity implements IWeiboActivity {

    private static final String TAG = "HomeActivity";

    private TextView homelayout_username_tv = null;

    private ImageView user_title_layout_sendweibo_iv = null;

    public static final int REFRESH_WEIBO = 1;

    public static final int REFRESH_ICON = 2;

    public static final int REFRESH_PERSON = 3;

    public List<WeiboInfos> weiboinfos;

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost = null;


    //定义一个布局
    private LayoutInflater inflater = null;


    private Class[] fragmentArray = {WeiboHomeFragment.class,PersonalFragment.class
            ,ReviewFragment.class,CollectionFragment.class};

    private int[] mImgageViewArray = {R.drawable.zhuyean1,R.drawable.gerenan1
            ,R.drawable.pinglunan1,R.drawable.shoucang1};

    private String[] mTextviewArray = {"首页","个人","评论","收藏"};

    private MainService  mainService = null;

    private WeiboHomeFragment f = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_WEIBO:
                    LogManage.i(TAG,"REFRESH_WEIBO回调");
                break;


            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home_layout);
        this.homelayout_username_tv = (TextView) this.findViewById(R.id.homelayout_username_tv);
        user_title_layout_sendweibo_iv = (ImageView) this.findViewById(R.id.user_title_layout_sendweibo_iv);
        user_title_layout_sendweibo_iv.setOnClickListener(this);
        this.init();
        this.initView();

    }



    @Override
    public void init() {
        //加载当前用户首页信息
        if(NetUtil.checkNet(HomeActivity.this)) {

            mainService = new MainService();
            String mainservice_packagename = this.getResources().getString(R.string.mainservice_packagename);
            Intent MainServiceIntent = new Intent(mainservice_packagename);
            HomeActivity.this.startService(MainServiceIntent);
            Task task_hometime = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,null);

            mainService.addActivity(HomeActivity.this);

            mainService.addTask(task_hometime);
        }else{
            NetUtil.isNeedSetNetWork(HomeActivity.this);
        }
    }


    @Override
    public void refresh(Object... params) {

        ArrayList<WeiboInfos> list = (ArrayList<WeiboInfos>) params[1];
        LogManage.i(TAG,String.valueOf(list.size()));

        switch(((Integer)params[0]).intValue()){

                //刷新微博
                case HomeActivity.REFRESH_WEIBO:
                    LogManage.i(this.TAG,"更新微博");
                    weiboinfos = (ArrayList<WeiboInfos>) params[1];
                     //f = (WeiboHomeFragment) getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
                     f = (WeiboHomeFragment) getSupportFragmentManager().findFragmentByTag("首页");
                     f.setInfo(weiboinfos);
                 break;

            }
    }



    //初始化控件方法
    public void initView(){
        //实例化布局对象
        this.inflater = LayoutInflater.from(HomeActivity.this);

        //实例化TabHost对象，得到TabHost
        this.mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        this.mTabHost.setup(this,this.getSupportFragmentManager(),R.id.homelayout_realcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            //将Tab按钮添加进Tab选项卡中
            this.mTabHost.addTab(tabSpec,fragmentArray[i],null);

        }
    }


    /**
     * 给Tab按钮设置图标
     */
    private View getTabItemView(int index) {
        View view = this.inflater.inflate(R.layout.tab_item_view,null);
        ImageView iv = (ImageView) view.findViewById(R.id.tab_item_view_iv);
        iv.setImageResource(mImgageViewArray[index]);

        TextView tv = (TextView) view.findViewById(R.id.tab_item_view_tv);
        tv.setText(mTextviewArray[index]);
        return view;
    }


   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把触控事件交给 手势检测器处理
        LogManage.i(TAG,"onTouchEvent被激活");
        return this.detector.onTouchEvent(event);
    }*/

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.user_title_layout_sendweibo_iv:
                    Intent sendWeiboIntent = new Intent(this,SendWeiboActivity.class);
                    this.startActivity(sendWeiboIntent);
                    overridePendingTransition(R.anim.base_silde_right_in,R.anim.base_silde_remain);
                break;
            }
    }


    //当返回按钮被按下的时候
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.abc_fade_out);
    }





}



