package com.tg.iweibo.engine;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.tg.iweibo.R;
import com.tg.iweibo.activity.HomeActivity;
import com.tg.iweibo.activity.WeiboHomeFragment;
import com.tg.iweibo.domain.Task;
import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.weiboutil.JsonParser;
import com.tg.iweibo.weiboutil.SendRequest;
import org.apache.http.HttpEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainService extends Service implements Runnable  {

    private static final String TAG = "MainService";

    private StatusesAPI statusesApi = null;

    //任务队列
    private static List<Task> taskList = new ArrayList<Task>();

    //activity栈
    private static List<Activity> activityList = new ArrayList<Activity>();

    private static List<Fragment> fragmentList = new ArrayList<Fragment>();

    private List<String> user_homeList = new ArrayList<String>();

    //是否运行任务
    private boolean isRun = false;

    private  IWeiboActivity iActivity;
    //根据activity更新ui
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                //获得首页微博
                case Task.TASK_GET_USER_HOMETIMEINLINE:
                     iActivity = (IWeiboActivity) MainService.this.getActvityByName("HomeActivity");
                    iActivity.refresh(HomeActivity.REFRESH_WEIBO, msg.obj);
                break;

                case Task.TASK_GET_USER_PERSONAL:
                     iActivity = (IWeiboActivity) MainService.this.getActvityByName("HomeActivity");
                    iActivity.refresh(HomeActivity.REFRESH_PERSON, msg.obj);
                break;

            }
        }
    };



    //一创建就启动线程
    @Override
    public void onCreate() {
        this.isRun = true;
        new Thread(this).start();
    }


    public MainService() {
        //this.isRun = true;
    }


    /**
     * 添加任务到任务栈
     *
     * @param task 任务实体类
     */
    public void addTask(Task task) {
        MainService.this.taskList.add(task);
    }


    /**
     * 添加activity到activity栈
     *
     * @param activity activity实体
     */
    public void addActivity(Activity activity) {
        MainService.this.activityList.add(activity);
    }





    /**
     * 根据名字在activity栈中找到actvity实例
     *
     * @param activityName activity的名字
     * @return activity实例
     */
    private Activity getActvityByName(String activityName) {
        if (!(MainService.this.activityList.isEmpty())) {
            for (Activity activity : MainService.this.activityList) {
                if (null != activity) {
                    if (activity.getClass().getName().indexOf(activityName) >= 0) {
                        return activity;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 开启一个新的线程处理任务
     */
    @Override
    public void run() {
        while (this.isRun) {
            Task lastTask = null;
            synchronized (this.taskList) {//可以在别的地方也会直线该任务，所以进行线程同步
                if (!(MainService.this.taskList.isEmpty())) {
                    lastTask = this.taskList.get(0);
                    doTask(lastTask);
                }
                taskList.remove(lastTask);
            }
        }
    }


    /**
     * 处理任务方法，根据id判断要处理的是哪一个热舞
     *
     * @param task
     */
    private void doTask(Task task) {
        Message msg = MainService.this.handler.obtainMessage();
        msg.what = task.getTaskID();


        switch (task.getTaskID()) {

            //获取用户首页的微博信息
            case Task.TASK_GET_USER_HOMETIMEINLINE:

                try {
                    Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("access_token",token.getToken());
                    String requestPath = this.getResources().getString(R.string.requestPath);
                    HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);
                    List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
                    msg.obj = friendsTimeLine_list;
                   // byte[] b = this.readStream(entity.getContent());
                   // msg.obj = new String(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogManage.i(TAG, "获取用户首页微博信息");
            break;

            //获取用户个人微博
            case Task.TASK_GET_USER_PERSONAL:
                try{
                        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("access_token", token.getToken());
                        String requestPath = this.getResources().getString(R.string.requestPath_person);
                        HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);
                        List<WeiboInfos> userTimeLine__list = JsonParser.friendsTimeLine_To_Map(entity);
                        msg.obj = userTimeLine__list;
                    }catch(Exception e) {
                        e.printStackTrace();
                }
            break;






            default:
            break;
        }

        handler.sendMessage(msg);
        this.taskList.remove(task);//执行完任务销毁
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRun = false;
    }


    /**
     * 读取数据方法
     *
     * @param is
     * @return
     * @throws java.io.IOException
     */
    public static byte[] readStream(InputStream is) throws IOException {
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



    /*
       发送请求获得用户列表的方法
       public void test() throws Exception {

            Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
            StringBuilder temp = new StringBuilder("https://api.weibo.com/2/statuses/friends_timeline.json");
            temp.append("?");
            temp.append("access_token=");
            System.out.print(token.getToken());
            temp.append(token.getToken());

            HttpClient client = new DefaultHttpClient();
            //HttpGet get = new HttpGet("https://api.weibo.com/2/statuses/friends_timeline.json?access_token=2.00NJerHC0I61_14f931017bdU7GHlC");
            HttpGet get = new HttpGet(temp.toString());

            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == 200){
                byte [] b = readStream(response.getEntity().getContent());
                String str = new String(b);
                testA = str;
        }
    */




}
