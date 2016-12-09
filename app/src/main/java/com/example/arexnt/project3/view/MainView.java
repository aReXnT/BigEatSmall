package com.example.arexnt.project3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.arexnt.project3.R;
import com.example.arexnt.project3.constant.ConstantUtil;
import com.example.arexnt.project3.factory.GameObjectFactory;
import com.example.arexnt.project3.object.EnemyFish;
import com.example.arexnt.project3.object.EnemyFish1;
import com.example.arexnt.project3.object.EnemyFish2;
import com.example.arexnt.project3.object.EnemyFish3;
import com.example.arexnt.project3.object.EnemyFish4;
import com.example.arexnt.project3.object.EnemyFish5;
import com.example.arexnt.project3.object.GameObject;
import com.example.arexnt.project3.object.MyFish;
import com.example.arexnt.project3.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;



/*游戏进行的主界面*/
public class MainView extends BaseView{
    private int missileCount; 		// 导弹的数量
	private int middlePlaneScore;	// 中型敌机的积分	//根据分数确定什么时候进行敌机初始化，初始化后分数归零，每次得分该分数会跟着增加
	private int bigPlaneScore;		// 大型敌机的积分
	private int bossPlaneScore;		// boss型敌机的积分
    private int missileScore;		// 导弹的积分
	private int sumScore;			// 游戏总得分
	private int speedTime;			// 游戏速度的倍数

    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint3 = new Paint();

    private int angle;
	private float play_bt_w;
	private float play_bt_h;
	private boolean isPlay;			// 标记游戏运行状态
	private boolean isTouchPlane;	// 判断玩家是否按下屏幕

	private Bitmap playButton; 		// 开始/暂停游戏的按钮图片
	private MyFish myFish;		// 玩家的飞机

	private List<EnemyFish> enemyFishes;
	private GameObjectFactory factory;
	private int goalScore = 100;
    private float touch_x;
    private float touch_y;
    private float missile_bt_y;


    public MainView(Context context, GameSoundPool sounds) {
		super(context, sounds);
		// TODO Auto-generated constructor stub
		isPlay = true;
		speedTime = 1;
		angle = 0;
		factory = new GameObjectFactory();						  //工厂类
		enemyFishes = new ArrayList<EnemyFish>();
		myFish = (MyFish) factory.createMyPlane(getResources());//生产玩家的飞机
		myFish.setMainView(this);
		for(int i = 0; i < EnemyFish1.sumCount; i++){
			//生产小型敌机
			EnemyFish1 smallPlane = (EnemyFish1) factory.createSmallPlane(getResources());
			enemyFishes.add(smallPlane);
		}
		for(int i = 0; i < EnemyFish2.sumCount; i++){
			//生产中型敌机
			EnemyFish2 middlePlane = (EnemyFish2) factory.createMiddlePlane(getResources());
			enemyFishes.add(middlePlane);
		}
		for(int i = 0; i < EnemyFish3.sumCount; i++){
			//生产大型敌机
			EnemyFish3 bigPlane = (EnemyFish3) factory.createBigPlane(getResources());
			enemyFishes.add(bigPlane);
		}
		//生产BOSS敌机
        for(int i = 0; i < EnemyFish4.sumCount; i++) {
            EnemyFish4 bossFish = (EnemyFish4) factory.createBossPlane(getResources());
            enemyFishes.add(bossFish);
        }
        //生存骷髅鱼
        for(int i = 0; i < EnemyFish5.sumCount; i++) {
            EnemyFish5 deathfish = (EnemyFish5) factory.createDeathPlane(getResources());
            enemyFishes.add(deathfish);
        }

		thread = new Thread(this);
        // 设置常亮
        this.setKeepScreenOn(true);
	}
	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); // 初始化图片资源
		for(GameObject obj: enemyFishes){
			obj.setScreenWH(screen_width,screen_height);
		}

		myFish.setScreenWH(screen_width,screen_height);
		myFish.setAlive(true);
		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// 响应触屏事件的方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			isTouchPlane = false;
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			float y = event.getY();
			if(x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h){
				if(isPlay){
					isPlay = false;
				}		
				else{
					isPlay = true;	
					synchronized(thread){
						thread.notify();
					}
				}
				return true;
			}
            else{
                touch_x = myFish.getMiddle_x() - x;
                touch_y = myFish.getMiddle_y() - y;
                isTouchPlane = true;
                return true;
            }
		}
		//响应手指在屏幕移动的事件
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//判断触摸点是否为玩家的飞机
			if(isTouchPlane){
				float x = event.getX();
				float y = event.getY();
				if(x + touch_x > myFish.getMiddle_x() + 20){
					if(myFish.getMiddle_x() + myFish.getSpeed() <= screen_width){
						myFish.setMiddle_x(myFish.getMiddle_x() + myFish.getSpeed());
					}					
				}
				else if(x + touch_x < myFish.getMiddle_x() - 20){
					if(myFish.getMiddle_x() - myFish.getSpeed() >= 0){
						myFish.setMiddle_x(myFish.getMiddle_x() - myFish.getSpeed());
					}				
				}
				if(y + touch_y > myFish.getMiddle_y() + 20){
					if(myFish.getMiddle_y() + myFish.getSpeed() <= screen_height){
						myFish.setMiddle_y(myFish.getMiddle_y() + myFish.getSpeed());
					}		
				}
				else if(y + touch_y < myFish.getMiddle_y() - 20){
					if(myFish.getMiddle_y() - myFish.getSpeed() >= 0){
						myFish.setMiddle_y(myFish.getMiddle_y() - myFish.getSpeed());
					}
				}
				return true;
			}	
		}
		return false;
	}
	// 初始化图片资源方法
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		playButton = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight()/2;
 	}
	//初始化游戏对象
	public void initObject(){
		for(EnemyFish obj: enemyFishes){
			//初始化小型敌机	
			if(obj instanceof EnemyFish1){
				if(!obj.isAlive() && middlePlaneScore <= 1000){
					obj.initial(speedTime,0,0);
					break;
				}	
			}
			//初始化中型敌机
			else if(obj instanceof EnemyFish2){
				//if(middlePlaneScore >= 0){
					if(!obj.isAlive() && bigPlaneScore <= 6400){
						obj.initial(speedTime,0,0);
						break;
					}	
				//}
			}
			//初始化大型敌机
			else if(obj instanceof EnemyFish3){
				if(bigPlaneScore >= 500){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}
				}
			}
			//初始化骷髅鱼
			else if(obj instanceof EnemyFish5){
				if(!obj.isAlive()){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}
				}
			}
			//初始化BOSS敌机
			else{
				if(bossPlaneScore >= 2400){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}
				}
			}

		}
		//提升等级
		if(sumScore >= speedTime*5000 && speedTime < 6){
			speedTime++;	
		}
	}
	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		for(GameObject obj: enemyFishes){
			obj.release();
		}
		myFish.release();

		if(!playButton.isRecycled()){
			playButton.recycle();
		}

	}
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.WHITE); // 绘制背景色
			canvas.save();
			//波浪背景
			int height = getHeight();
			int width = getWidth();
			paint1 = new Paint();
			paint2 = new Paint();
			paint3 = new Paint();
			paint1.setColor(Color.rgb(205, 243, 246));
			paint2.setAlpha(200);
			paint2.setColor(Color.rgb(150, 206, 231));
			paint3.setAlpha(150);
			paint3.setColor(Color.rgb(89, 186, 231));
			double lineX = 0;
			double lineY1 = 0;
			double lineY2 = 0;
			double lineY3 = 0;
			for (int i = 0; i < width; i++) {
				lineX = i;
				if (myFish.isAlive()) {
					lineY1 = 5 * Math.sin((i + angle) * Math.PI / 180);
					lineY2 = 10 * Math.sin((i + angle) * Math.PI / 180) + 20;
					lineY3 = 20 * Math.sin((i + angle) * Math.PI / 270) + 50;
				} else {
					lineY1 = 0;
					lineY2 = 20;
					lineY3 = 50;
				}
				canvas.drawLine((int) lineX, (int) (lineY1 + height / 10),
						(int) lineX + 1, (int) (lineY2 + height / 10), paint1);
				canvas.drawLine((int) lineX, (int) (lineY2 + height / 10),
						(int) lineX + 1, (int) (lineY3 + height / 10), paint2);
				canvas.drawLine((int) lineX, (int) (lineY3 + height / 10),
						(int) lineX + 1, height, paint3);
			}

			canvas.restore();


			//绘制按钮
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
			if(isPlay){
				canvas.drawBitmap(playButton, 10, 10, paint);			 
			}
			else{
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();
			//绘制敌机
			for(EnemyFish obj: enemyFishes){
				if(obj.isAlive()){
					obj.drawSelf(canvas);					
					//检测敌机是否与玩家的飞机碰撞					
					if(obj.isCanCollide() && myFish.isAlive()){
						if(obj.isCollide(myFish)){
							if(obj.getSize() >= myFish.getSize())
							    myFish.setAlive(false);
                            else{
								sounds.playSound(2, 0);
                                obj.setAlive(false);
                                addGameScore(obj.getScore());
                                if(myFish.getSize() < 8) {
                                    if (sumScore >= goalScore) {
                                        myFish.setSize(myFish.getSize() + 2);
                                        goalScore *= 8;
                                    }
                                }

                            }
						}
					}
				}	
			}
			if(!myFish.isAlive()){
				threadFlag = false;
				sounds.playSound(3, 0);			//飞机炸毁的音效
			}
			myFish.drawSelf(canvas);	//绘制玩家的飞机


			//绘制积分文字
			paint.setTextSize(40);
			paint.setColor(Color.BLACK);
			canvas.drawText("积分:"+ String.valueOf(sumScore), 30 + play_bt_w, screen_height - 40, paint);		//绘制文字

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	// 增加游戏分数的方法 
	public void addGameScore(int score){
		middlePlaneScore += score;	// 中型敌机的积分
		bigPlaneScore += score;		// 大型敌机的积分
		bossPlaneScore += score;	// boss型敌机的积分
        if(missileScore > 3000){
            missileScore = 0;
        }
        missileScore += 100;		// 导弹的积分
		sumScore += score;			// 游戏总得分
	}
	// 播放音效
	public void playSound(int key){
		sounds.playSound(key, 0);
	}
	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		sounds.playSound(4,10);
		while (threadFlag) {	
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();

            angle++;
            if (angle == 360) {
                angle = 0;
            }


			long endTime = System.currentTimeMillis();
			if(!isPlay){
				synchronized (thread) {  
				    try {  
				    	thread.wait();  
				    } catch (InterruptedException e) {
				        e.printStackTrace();  
				    }  
				}  		
			}	
			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Message message = new Message();
		message.what = 	ConstantUtil.TO_END_VIEW;
		message.arg1 = Integer.valueOf(sumScore);
		mainActivity.getHandler().sendMessage(message);
	}


}
