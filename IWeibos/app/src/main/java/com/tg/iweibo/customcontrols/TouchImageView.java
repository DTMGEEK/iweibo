package com.tg.iweibo.customcontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-03
 * Time: 15:16
 */

public class TouchImageView extends ImageView {


    private PointF down = new PointF();

    private PointF mid = new PointF();

    private float oldDist = 1f;

    private Matrix matrix = new Matrix();

    private Matrix preMatrix = new Matrix();

    private Matrix saveMatrix = new Matrix();


    private static final int NONE = 0;
    private static final int DRAG = 0;
    private static final int ZOOM = 0;
    private int mode = NONE;

      //纪录图片是否已经放大
     private boolean isBig = false;

    private  int widthScreen; //屏幕宽度

    private int heightScreen; //屏幕高度

    private int touchImageWidth; //改变后的图片宽度

    private int touchImageHeight; //改变后的图片高度

    private float defaultScale;//默认缩放比例

    private long lastClickTime = 0;

    private Bitmap touchImg = null; //缩放后的图片实例

    private static final int DOUBLE_CLICK_TIME_SPACE = 300; //双击屏幕的时间间隔
    private static final int DOUBLE_POINT_DISTANCE = 10;    //双击点的最短距离
    private static final float MAX_SCALE = 3.0f;     //最大缩放比例

    private static final int DRAG_LEFT = 0 ;
    private static final int DRAG_RIGHT = 1 ;
    private static final int DRAG_TOP = 2 ;
    private static final int DRAG_DOWN = 3 ;


    public TouchImageView(Context context) {
        super(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //初始化方法
    public void initImageView(int screenWidth,int screenHeight){
        this.widthScreen = screenWidth;
        this.heightScreen = screenHeight;

        touchImg = ((BitmapDrawable)this.getDrawable()).getBitmap();

        touchImageWidth = touchImg.getWidth();//图片宽度
        touchImageHeight = touchImg.getHeight();//图片高度

        float scaleX = (float)widthScreen/touchImageHeight;
        float scaleY = (float)heightScreen/touchImageHeight;
        defaultScale = scaleX < scaleY ? scaleX:scaleY;

        float subX = (widthScreen - touchImageWidth * defaultScale)/2;
        float subY = (heightScreen - touchImageHeight * defaultScale)/2;

        this.setScaleType(ScaleType.MATRIX);
        preMatrix.reset();
        preMatrix.postScale(defaultScale,defaultScale);
        preMatrix.postTranslate(subX,subY);
        matrix.set(preMatrix);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(null != touchImg){
            canvas.save();
            canvas.drawBitmap(touchImg,matrix,null);
            canvas.restore();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                this.mode = DRAG;
                down.x = event.getX();
                down.y = event.getY();
                saveMatrix.set(matrix);

                if(event.getEventTime() - lastClickTime < this.DOUBLE_CLICK_TIME_SPACE){
                    changeSize(event.getX(),event.getY());
                }
                lastClickTime = event.getEventTime();
            break;

            case MotionEvent.ACTION_POINTER_DOWN:
                this.oldDist = spacing(event);
                if(this.oldDist > this.DOUBLE_POINT_DISTANCE){
                    mode = ZOOM;
                    saveMatrix.set(matrix);
                    midPoint(mid,event);
                }
            break;


            case MotionEvent.ACTION_MOVE:
                if(mode == ZOOM) {   //缩放状态
                    float newDist = spacing(event);
                    float scale = newDist / oldDist;
                    if(scale > 1.01|| scale < 0.99) {
                        preMatrix.set(saveMatrix);
                        preMatrix.postScale(scale, scale, mid.x, mid.y);// 缩放
                        if(canZoom()) {
                            matrix.set(preMatrix);
                            invalidate();
                        }
                    }
                } else if(mode == DRAG) {  //拖动状态
                if(1.0f < distance(event, down)) {
                    preMatrix.set(saveMatrix);

                    preMatrix.postTranslate(event.getX() - down.x, 0);
                    if(event.getX() > down.x) {
                        if(canDrag(DRAG_RIGHT)) {
                            saveMatrix.set(preMatrix);
                        } else{
                            preMatrix.set(saveMatrix);
                        }
                    } else{
                        if(canDrag(DRAG_LEFT)) {
                            saveMatrix.set(preMatrix);
                        } else{
                            preMatrix.set(saveMatrix);
                        }
                    }
                    preMatrix.postTranslate(0, event.getY() - down.y);
                    if(event.getY() > down.y) {
                        if(canDrag(DRAG_DOWN)) {
                            saveMatrix.set(preMatrix);
                        } else{
                            preMatrix.set(saveMatrix);
                        }
                    } else{
                        if(canDrag(DRAG_TOP)) {
                            saveMatrix.set(preMatrix);
                        } else{
                            preMatrix.set(saveMatrix);
                        }
                    }

                    matrix.set(preMatrix);
                    invalidate();
                    down.x = event.getX();
                    down.y = event.getY();
                    saveMatrix.set(matrix);
                }
            }
            break;


            case MotionEvent.ACTION_UP:
                mode = NONE;
                springback();
            break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
            break;
        }
        return true;
    }




    private void springback() {
        preMatrix.set(matrix);
        float[] x = new float[4];
        float[] y = new float[4];
        getFourPoint(x,y);
        if(x[1] - x[0] > widthScreen){
             if(x[0] > 0){
                 preMatrix.postTranslate(-x[0], 0);
                 matrix.set(preMatrix);
                 invalidate();
             }else if(x[1] < widthScreen){
                 preMatrix.postTranslate(widthScreen - x[1],0);
                 matrix.set(preMatrix);
                 invalidate();
             }
        }else if(x[1] - x[0] < widthScreen - 1f){
            preMatrix.postTranslate(widthScreen -(x[1] - x[0]/2 - x[0]),0);
            matrix.set(preMatrix);
            invalidate();
        }


        if(y[2] - y[0] > heightScreen){
            if(y[0] > 0){
                preMatrix.postTranslate(0,-y[0]);
                matrix.set(preMatrix);
                invalidate();
            }else if(y[2] < heightScreen){
                preMatrix.postTranslate(0,heightScreen - y[2]);
                matrix.set(preMatrix);
                invalidate();
            }
        }else if(y[2] - y[0] < heightScreen - 1f){
            preMatrix.postTranslate(0,(heightScreen - (y[2] - y[0]))/2 - y[0]);
            matrix.set(preMatrix);
            invalidate();
        }
    }



    private void getFourPoint(float[] x, float[] y) {
        float[] f = new float[9];
        preMatrix.getValues(f);

        // 图片4个顶点的坐标
        x[0] = f[Matrix.MSCALE_X] * 0+ f[Matrix.MSKEW_X] * 0
                + f[Matrix.MTRANS_X];
        y[0] = f[Matrix.MSKEW_Y] * 0+ f[Matrix.MSCALE_Y] * 0
                + f[Matrix.MTRANS_Y];
        x[1] = f[Matrix.MSCALE_X] * touchImg.getWidth() + f[Matrix.MSKEW_X] * 0
                + f[Matrix.MTRANS_X];
        y[1] = f[Matrix.MSKEW_Y] * touchImg.getWidth() + f[Matrix.MSCALE_Y] * 0
                + f[Matrix.MTRANS_Y];
        x[2] = f[Matrix.MSCALE_X] * 0+ f[Matrix.MSKEW_X]
                * touchImg.getHeight() + f[Matrix.MTRANS_X];
        y[2] = f[Matrix.MSKEW_Y] * 0+ f[Matrix.MSCALE_Y]
                * touchImg.getHeight() + f[Matrix.MTRANS_Y];
        x[3] = f[Matrix.MSCALE_X] * touchImg.getWidth() + f[Matrix.MSKEW_X]
                * touchImg.getHeight() + f[Matrix.MTRANS_X];
        y[3] = f[Matrix.MSKEW_Y] * touchImg.getWidth() + f[Matrix.MSCALE_Y]
                * touchImg.getHeight() + f[Matrix.MTRANS_Y];

    }


    private boolean canDrag(final int direction){
        float[] x = new float[4];
        float[] y = new float[4];

        getFourPoint(x,y);

        //出界判断
        if((x[0] > 0 || x[2] >0 || x[1] < widthScreen || x[3] < widthScreen)
                && (y[0] > 0 || y[1] > 0 || y[2] < heightScreen || y[3] < heightScreen)){
            return false;
        }

        if(DRAG_LEFT == direction){
            //左移出界判断
            if(x[1] < widthScreen || x[3] < widthScreen){
                return  false;
            }
        }else if(DRAG_RIGHT == direction){
            //右移出界判断
            if(x[0] > 0 || x[2] > 0){
                return  false;
            }
        }else if(DRAG_TOP == direction){
            //上移出界判断
            if(y[2] < heightScreen || y[3] < heightScreen){
                return false;
            }
        }else if(DRAG_DOWN == direction){
            //下移出界判断
            if(y[0] > 0 || y[1] > 0){
                return  false;
            }
        }else{
            return false;
        }
        return true;
    }




    private boolean canZoom(){
        float[] x = new float[4];
        float[] y = new float[4];

        getFourPoint(x,y);

        //图片现在的宽度
        double width = Math.sqrt((x[0] - x[1]) * (x[0] - x[1]) + (y[0] - y[1]) * (y[0]-y[1]));

        double height = Math.sqrt((x[0] - x[2]) * (x[0] - x[2]) + (y[0] - y[2]) * (y[0] - y[2]));


        //缩放比率判断
        if(width < touchImageWidth * defaultScale -1 || width > touchImageWidth * MAX_SCALE + 1){
            return  false;
        }

        //出界判断
        if(width < widthScreen && height < heightScreen){
            return  false;
        }

        return true;
    }


    //触碰两点间距离
    private static  float spacing(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        if(x < 0){
            x = -x;
        }
        float y = event.getY(0) - event.getY(1);
        if(y < 0){
            y = -y;
        }
        return FloatMath.sqrt(x*x + y*y);
    }


    //取手势中心点
    private static void midPoint(PointF point,MotionEvent event){
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x/2,y/2);
    }


    //取两点间的距离
    private static float distance(MotionEvent point2,PointF point1){
        float x = point1.x - point2.getX();
        if(x < 0){
            x = -x;
        }
        float y = point1.y - point2.getY();
        if(y < 0){
            y = -y;
        }
        return FloatMath.sqrt(x*x + y*y);
    }



    private void changeSize(float x,float y){
        if(isBig){
            float subX = (widthScreen - touchImageWidth * defaultScale) / 2;
            float subY = (heightScreen - touchImageHeight * defaultScale) / 2;
            preMatrix.reset();
            preMatrix.postScale(defaultScale,defaultScale);
            preMatrix.postTranslate(subX,subY);
            matrix.set(preMatrix);
            invalidate();

            isBig = false;
        }else {
            float transX = (widthScreen - touchImageWidth * MAX_SCALE) / 2;
            float transY = (heightScreen - touchImageHeight * MAX_SCALE) / 2;
            preMatrix.reset();
            preMatrix.postScale(MAX_SCALE,MAX_SCALE);
            preMatrix.postTranslate(transX,transY);
            matrix.set(preMatrix);
            invalidate();

            isBig = false;
        }
    }




}
