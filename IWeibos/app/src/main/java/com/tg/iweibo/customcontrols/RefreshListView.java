package com.tg.iweibo.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.tg.iweibo.R;
import com.tg.iweibo.utils.LogManage;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-23
 * Time: 15:43
 */

public class RefreshListView extends ListView implements OnScrollListener {

    private static final  String TAG = "RefreshListView";
    //顶部布局文件的实体
    private View headerView = null;

    //顶部布局文件的高度
    private int headerHeight = 0;

    //判断可见的第一个item是不是第0个
    private int firstVisibleItem;

    //纪录是不是从第0个item开始滚动
    private  boolean reMark;

    //滚动的状态，滚动或者没有滚动
    private int scrollState;

    //开始的y的高度
    private int startY;

    //纪录手指在屏幕运动的状态
    private  int state = 0;

    //手指在屏幕运动状态
    //正常状态
    private static final int NORM = 0;
    //下拉状态
    private static final int PULL = 1;
    //松开状态
    private static final int RELEASE = 2;
    //刷新状态
    private static final int RELEASEREFRESH = 3;

    private IinterfaceListener listener;


    //底部布局实体
    private View footerView = null;
    //是否在加载
    private boolean isLoading = false;
    //最后一个看见item的编号
    private int lastVisibleItem = 0;
    //所有的item总算
    private int totalItemCount = 0;



   public  RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    //初始化方法
    public void init(Context context){
        this.headerView = LayoutInflater.from(context).inflate(R.layout.customlistview_headerlayout,null);
        this.footerView = LayoutInflater.from(context).inflate(R.layout.customlistview_footer_layout,null);
        this.footerView.setVisibility(View.GONE);
        this.measureView(headerView);
        this.headerHeight = this.headerView.getMeasuredHeight();
        LogManage.i(TAG,String.valueOf(headerHeight));
        this.topPadding(-headerHeight);
        this.addFooterView(footerView);
        RefreshListView.this.addHeaderView(headerView);
        //设置滚动监听事件
        this.setOnScrollListener(this);

    }

    //设置顶部的padding 用于显示或者隐藏顶部布局
    private void topPadding(int topPadding) {
        this.headerView.setPadding(this.headerView.getPaddingLeft(),topPadding
                            ,this.headerView.getPaddingRight(),this.headerView.getPaddingBottom());
        this.headerView.invalidate();
    }


    /**
     * 告诉父控件 子控件的宽度和高度
     * @param view 子控件
     */
    private void measureView(View view){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(null == params){
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                      ViewGroup.LayoutParams.WRAP_CONTENT);
        }
          /*
            * 计算MeasureSpec然后传递到特定的子视图，此方法用来计算一个合适子视图的尺寸大小（宽度或者高度)，
            * 此方法的目的在于结合我们从子视图的LayoutParams所给出的MeasureSpec信息来获取最合适的结果。
            * 比如，如果这个View知道自己的大小尺寸（因为它本身的MeasureSpec的model为Exactly,）并且子视图的大小恰好跟父窗口一样大，父窗口必须用给定的大小去layout子视图
            * 参数：spec 父窗口传递给子视图的大小和模式
            * padding 父窗口的边距，也就是xml中的android:padding
            * childDimension 子视图想要绘制的准确大小，但最终不一定绘制此值
            *
            *
            * 因为listView不限制高度。
            * child有多高，listView就给它多高的空间。
            * 但是listView是限制宽度的，所以需要getChildMeasureSpec
            */
        int width = ViewGroup.getChildMeasureSpec(0,0,params.width);
        LogManage.i(TAG,String.valueOf(width));
        //计算高度
        int height ;
        int tempHeight = params.height;
        if(tempHeight > 0){
            height = MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);
        }else{
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        view.measure(width,height);
    }


    //
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
            this.scrollState = scrollState;
                //已经是最后一条 并且滚动已经停止
            if(lastVisibleItem == totalItemCount && scrollState == this.SCROLL_STATE_IDLE){
                if(!this.isLoading){
                    this.isLoading = true;
                    this.footerView.setVisibility(View.VISIBLE);
                    //加载数据
                    listener.onLoad();
                }
            }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.firstVisibleItem = firstVisibleItem;

                //下拉
                this.lastVisibleItem = firstVisibleItem + visibleItemCount;
                this.totalItemCount = totalItemCount;
    }


    /**
     * 触控事件监听
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){

            //手指按下
            case MotionEvent.ACTION_DOWN:
               if(this.firstVisibleItem == 0){
                   this.startY = (int) ev.getY();
                   this.reMark = true;
               }
            break;

            //手指移动
            case MotionEvent.ACTION_MOVE:
                this.toMove(ev);
            break;

            //手指放开
            //在放手抬起的时候要进行状态的判断，如果是在松开手了RELEASE 那么状态就改为刷新RELEASEREFRESH
            case MotionEvent.ACTION_UP:
                  if(state == RELEASE){
                      state = RELEASEREFRESH;
                      this.refreshByState();
                       listener.IRefresh();
                  }else if(state == PULL){
                        state = NORM;
                        reMark = false;
                        refreshByState();
                  }
            break;
        };

        return super.onTouchEvent(ev);
    }



    //手指移动移动过程中的状态改变
    private void toMove(MotionEvent ev) {
        if(!this.reMark){      //不是从顶端下拉就返回
            return;
        }

        int tempY = (int) ev.getY();

        //下拉的距离
        int space = tempY - this.startY;

        //顶部布局的toppadding，用来显示顶部布局
        int topPadding = space - this.headerHeight;

        switch (this.state){
            case NORM:      //正常状态下
               if(space > 0){       //根据下拉的距离判断
                   this.state = this.PULL;
                   refreshByState();
               }
            break;


            case PULL:      //在下拉状态根据距离判断 是否已经变为刷新状态
                 this.topPadding(topPadding);
                //如果高度大于一定的高度并且是处于滚动状态，状态就变成了可以刷新的状态
                if((space > this.headerHeight + 30)&&(this.scrollState==SCROLL_STATE_TOUCH_SCROLL)){
                    this.state = RELEASE;
                    refreshByState();
                }
            break;

            case RELEASE:   //释放状态 判断距离是否要刷新，还是用户已经回弹
                this.topPadding(topPadding);
                if(space < this.headerHeight+30){
                    this.state = this.PULL;
                    refreshByState();
                }else if(space <= 0 ){
                    this.state = this.NORM;
                    this.reMark = false;
                    refreshByState();
                }
            break;

        }
    }


    //根据状态刷新UI
    private void refreshByState(){
        TextView customlistview_header_tip_tv = (TextView) this.headerView.findViewById(R.id.customlistview_header_tip_tv);
        TextView customlistview_header_lasttimeupdate_tv = (TextView) this.headerView.findViewById(R.id.customlistview_header_lasttimeupdate_tv);
        ImageView customlistview_header_arrow_iv = (ImageView) this.headerView.findViewById(R.id.customlistview_header_arrow_iv);
        ProgressBar customlistview_header_progress_pb = (ProgressBar) this.findViewById(R.id.customlistview_header_progress_pb);

        RotateAnimation rotatAnim = new RotateAnimation(0,180,RotateAnimation.RELATIVE_TO_SELF,0.5f
                                        ,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotatAnim.setDuration(500);
        rotatAnim.setFillAfter(true);//设置动画结束后保持状态


        RotateAnimation rotatAnim1 = new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5f
                                              ,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotatAnim1.setDuration(500);
        rotatAnim1.setFillAfter(true);

        switch (this.state){
            case NORM:
                this.topPadding(-this.headerHeight);
                customlistview_header_arrow_iv.clearAnimation();
            break;

            case PULL:
                customlistview_header_arrow_iv.setVisibility(View.VISIBLE);
                customlistview_header_arrow_iv.clearAnimation();
                customlistview_header_arrow_iv.setAnimation(rotatAnim1);
                customlistview_header_progress_pb.setVisibility(View.GONE);
                customlistview_header_tip_tv.setText(this.getContext().getResources()
                        .getString(R.string.customlistview_header_tip_text));
            break;


            case RELEASE:
                customlistview_header_arrow_iv.setVisibility(View.VISIBLE);
                customlistview_header_arrow_iv.clearAnimation();
                customlistview_header_arrow_iv.setAnimation(rotatAnim);
                customlistview_header_progress_pb.setVisibility(View.GONE);
                customlistview_header_tip_tv.setText(this.getContext().getResources()
                        .getString(R.string.customlistview_header_tip_retext));
            break;


            case RELEASEREFRESH:
                this.topPadding(50);
                customlistview_header_arrow_iv.setVisibility(View.GONE);
                customlistview_header_arrow_iv.clearAnimation();
                customlistview_header_progress_pb.setVisibility(View.VISIBLE);
                customlistview_header_tip_tv.setText(this.getContext().getResources()
                        .getString(R.string.customlistview_header_tip_reingtext));
            break;

        }
    }


    /**
     * 刷新完成方法
     */
    public void refreshComplete(){
        this.state = NORM;
        this.reMark = false;
        this.refreshByState();
        TextView customlistview_header_lasttimeupdate_tv = (TextView) this.headerView
                                .findViewById(R.id.customlistview_header_lasttimeupdate_tv);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        String dateStr = sdf.format(new Date(System.currentTimeMillis()));
        customlistview_header_lasttimeupdate_tv.setText(dateStr);
    }



    public void setInterface(IinterfaceListener listener){
            this.listener = listener;
    }


    public interface IinterfaceListener{
        public void IRefresh();

        public void onLoad();
    }



}
