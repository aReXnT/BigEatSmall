package com.example.arexnt.project3.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.arexnt.project3.factory.GameObjectFactory;
import com.example.arexnt.project3.interfaces.IMyFish;
import com.example.arexnt.project3.R;
import com.example.arexnt.project3.view.MainView;

/*玩家飞机的类*/
public class MyFish extends GameObject implements IMyFish{
    private float middle_x;			 // 飞机的中心坐标
    private float middle_y;
    private Bitmap myfish;			 // 飞机飞行时的图片
    private Bitmap resizeBmp;
    private MainView mainView;
    private GameObjectFactory factory;
    private Matrix matrix = null;
    private int size;

    public void setSize(int size) {
        this.size = size;
        object_width = object_width*1.4f; // 获得每一帧位图的宽
        object_height = object_height*1.4f; 	// 获得每一帧位图的高
        matrix.postScale(1.4f,1.4f); //长和宽放大缩小的比例
        resizeBmp = Bitmap.createBitmap(myfish,0,0,myfish.getWidth(),myfish.getHeight(),matrix,true);
    }

    public int getSize() {

        return size;
    }

    public MyFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.speed = 16;
        this.size = 2;
        initBitmap();
        factory = new GameObjectFactory();
    }
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }
    // 设置屏幕宽度和高度
    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        object_x = screen_width/2 - object_width/2;
        object_y = screen_height/2 - object_height/2;
        middle_x = object_x + object_width/2;
        middle_y = object_y + object_height/2;
    }
    // 初始化图片资源的
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        myfish = BitmapFactory.decodeResource(resources, R.drawable.me2);
        object_width = myfish.getWidth()/4; // 获得每一帧位图的宽
        object_height = myfish.getHeight()/4; 	// 获得每一帧位图的高
        matrix = new Matrix();
        matrix.postScale(0.25f,0.25f); //长和宽放大缩小的比例
        resizeBmp = Bitmap.createBitmap(myfish,0,0,myfish.getWidth(),myfish.getHeight(),matrix,true);
    }
    // 对象的绘图方法
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        if(isAlive){
            canvas.save();
            canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
            canvas.drawBitmap(resizeBmp, object_x, object_y, paint);
            canvas.restore();
        }
    }
    // 释放资源的方法
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if(!myfish.isRecycled()){
            myfish.recycle();
        }
    }
    @Override
    public float getMiddle_x() {
        return middle_x;
    }
    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width/2;
    }
    @Override
    public float getMiddle_y() {
        return middle_y;
    }
    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height/2;
    }
}
