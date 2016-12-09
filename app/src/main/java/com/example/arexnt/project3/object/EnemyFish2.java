package com.example.arexnt.project3.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.arexnt.project3.R;

import java.util.Random;

/*中型敌机的类*/
public class EnemyFish2 extends EnemyFish {
    private static int currentCount = 0;     //	对象当前的数量
    private Bitmap middleFish;// 对象图片
    private Bitmap resizeBmp;
    private Bitmap resizeBmp2;
    public static int sumCount = 8;             //	对象总的数量

    public EnemyFish2(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.score = 100;        // 为对象设置分数
        this.size = 3;
    }

    //初始化数据
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        isAlive = true;
        bloodVolume = 15;
        blood = bloodVolume;
        Random ran = new Random();
        speed = ran.nextInt(14) + 15 * arg0;
        if (0 == currentCount % 2) {
            isFromLeft = true;
            object_x = -object_width * (currentCount + 1);
        } else {
            isFromLeft = false;
            object_x = screen_width + object_width * (currentCount + 1);
        }
        object_y = ran.nextInt((int) (screen_height - object_height));
        //object_x = ran.nextInt((int)(screen_width - object_width));
        //object_y = -object_height * (currentCount* + 1);
        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }
    }

    // 初始化图片资源
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        middleFish = BitmapFactory.decodeResource(resources, R.drawable.en1fish128);
        object_width = middleFish.getWidth() / 2;            //获得每一帧位图的宽
        object_height = middleFish.getHeight() / 2;        //获得每一帧位图的高
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        resizeBmp = Bitmap.createBitmap(middleFish, 0, 0, middleFish.getWidth(), middleFish.getHeight(), matrix, true);
        matrix.postScale(-1f, 1f);
        resizeBmp2 = Bitmap.createBitmap(middleFish, 0, 0, middleFish.getWidth(), middleFish.getHeight(), matrix, true);
    }

    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        if (isAlive) {
            if (isVisible) {
                canvas.save();
                canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
                if (isFromLeft)
                    canvas.drawBitmap(resizeBmp2, object_x, object_y, paint);
                else
                    canvas.drawBitmap(resizeBmp, object_x, object_y, paint);
                canvas.restore();
            }
            logic();
        }
    }

    // 释放资源
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if (!middleFish.isRecycled()) {
            middleFish.recycle();
        }
    }
}
