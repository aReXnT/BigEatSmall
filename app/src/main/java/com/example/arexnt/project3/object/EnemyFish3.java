package com.example.arexnt.project3.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.arexnt.project3.R;

import java.util.Random;

/*大型敌机的类*/
public class EnemyFish3 extends EnemyFish {
    private static int currentCount = 0;     //	对象当前的数量
    public static int sumCount = 6;             //	对象总的数量
    private Bitmap bigFish; // 对象图片
    private Bitmap resizeBmp;
    private Bitmap resizeBmp2;

    public EnemyFish3(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.score = 500;        // 为对象设置分数
        this.size = 5;
    }

    //初始化数据
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        isAlive = true;
        bloodVolume = 30;
        blood = bloodVolume;
        Random ran = new Random();
        speed = ran.nextInt(18) + 10 * arg0;
        if (0 == currentCount % 2) {
            isFromLeft = true;
            object_x = -object_width * (currentCount + 1);
        } else {
            isFromLeft = false;
            object_x = screen_width + object_width * (currentCount/2 + 1);
        }
        object_y = ran.nextInt((int) (screen_height - object_height));
        //object_x = ran.nextInt((int)(screen_width - object_width));
        //object_y = -object_height * (currentCount*2 + 1);
        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }
    }

    // 初始化图片资源
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        bigFish = BitmapFactory.decodeResource(resources, R.drawable.en2fish128);
        object_width = bigFish.getWidth() / 4 * 3;            //获得每一帧位图的宽
        object_height = bigFish.getHeight() / 4 * 3;        //获得每一帧位图的高
        Matrix matrix = new Matrix();
        matrix.postScale(0.75f, 0.75f); //长和宽放大缩小的比例
        resizeBmp = Bitmap.createBitmap(bigFish, 0, 0, bigFish.getWidth(), bigFish.getHeight(), matrix, true);
        matrix.postScale(-1f, 1f);
        resizeBmp2 = Bitmap.createBitmap(bigFish, 0, 0, bigFish.getWidth(), bigFish.getHeight(), matrix, true);
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
        if (!bigFish.isRecycled()) {
            bigFish.recycle();
        }
    }
}
