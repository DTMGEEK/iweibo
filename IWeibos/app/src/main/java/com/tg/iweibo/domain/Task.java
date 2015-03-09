package com.tg.iweibo.domain;

import java.util.Map;

public class Task {
	   private int taskID;

	   private Map<String,Object> taskParams;
       //用户登录
       public static final int TASK_USER_LOGIN = 0;

       //获取用户详细信息
       public static final int TASK_GET_USER_INFO = 1;

       //获取用户首页博客
       public static final int TASK_GET_USER_HOMETIMEINLINE = 2;

       //获取用户头象图片
       public static final int TASK_GET_USER_PERSONAL= 3;

       //获取用户所有好友
       public static final int TASK_GET_USER_FRIEND = 4;

       //获取用户首页博客下一页
       public static final int TASK_GET_USER_HOMETIMEINLINE_NEXT = 6;

      //发表新微博
       public static final int TASK_NEW_WEIBO = 5;




        public Task(int taskID, Map<String, Object> taskParams) {
            this.taskID = taskID;
            this.taskParams = taskParams;
        }

        public int getTaskID() {
	        return taskID;
	    }

	    public void setTaskID(int taskID) {
	        this.taskID = taskID;
	    }

	    public Map<String, Object> getTaskParams() {
	        return taskParams;
	    }

	    public void setTaskParams(Map<String, Object> taskParams) {
	        this.taskParams = taskParams;
	    }
}
