package com.example.arexnt.project3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.arexnt.project3.constant.ConstantUtil;
import com.example.arexnt.project3.eatfish.MainActivity;
import com.example.arexnt.project3.R;
import com.example.arexnt.project3.sounds.GameSoundPool;

/*游戏结束时显示的界面*/
public class EndView extends BaseView{
	private int score;
	private float button_x;
	private float button_x2;
	private float button_y;
	private float strwid;
	private float strhei;
	private boolean isBtChange;				// 按钮图片改变的标记
	private boolean isBtChange2;
	private String startGame = "重新挑战";	// 按钮的文字
	private String exitGame = "退出游戏";
	private Bitmap startButton;				// 开始按钮图片
	private Bitmap startButtonPress;		// 开始按钮按下图片
	private Bitmap exitButton;				// 退出按钮图片
	private Bitmap exitButtonPress;			// 退出按钮按下图片
	private Bitmap resizeStart;
	private Bitmap resizeStartPress;
	private Bitmap resizeExit;
	private Bitmap resizeExitPress;
	private Bitmap background;				// 背景图片
	private Rect rect;						// 绘制文字的区域
	private MainActivity mainActivity;
	private int angle = 0;
	private int height;
	private int width;
	private Paint paint1 = new Paint();
	private Paint paint2 = new Paint();
	private Paint paint3 = new Paint();
	public EndView(Context context, GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		this.mainActivity = (MainActivity)context;
		rect = new Rect();
		thread = new Thread(this);
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
		initBitmap(); 
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
		if(event.getAction() == MotionEvent.ACTION_DOWN && event.getPointerCount() == 1){
			float x = event.getX();
			float y = event.getY();
			//判断第一个按钮是否被按下
			if(x > button_x && x < button_x + resizeStart.getWidth()
					&& y > button_y && y < button_y + resizeStart.getHeight())
			{
				sounds.playSound(1, 0);
				isBtChange = true;
				drawSelf();
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.TO_MAIN_VIEW);
			}
			//判断第二个按钮是否被按下
			else if(x > button_x2 && x < button_x2 + resizeExit.getWidth()
					&& y > button_y && y < button_y + resizeStart.getHeight())
			{
				sounds.playSound(1, 0);
				isBtChange2 = true;
				drawSelf();
				threadFlag = false;
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.END_GAME);
			}
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			float x = event.getX();
			float y = event.getY();
			if(x > button_x && x < button_x + resizeStart.getWidth()
					&& y > button_y && y < button_y + resizeStart.getHeight())
			{
				isBtChange = true;
			}
			else{
				isBtChange = false;
			}
			if(x > button_x && x < button_x + resizeExit.getWidth()
					&& y > button_y && y < button_y + resizeExit.getHeight())
			{
				isBtChange2 = true;
			}
			else{
				isBtChange2 = false;
			}
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			isBtChange = false;
			isBtChange2 = false;
			return true;
		}
		return false;
	}
	// 初始化图片资源方法
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub

		startButton = BitmapFactory.decodeResource(getResources(),R.drawable.start_button);
		startButtonPress = BitmapFactory.decodeResource(getResources(),R.drawable.start_button_press);
		exitButton = BitmapFactory.decodeResource(getResources(),R.drawable.end_button);
		exitButtonPress = BitmapFactory.decodeResource(getResources(),R.drawable.end_button_press);
		Matrix matrix = new Matrix();
		matrix.postScale(1f,1f); //长和宽放大缩小的比例
		resizeStart = Bitmap.createBitmap(startButton,0,0,startButton.getWidth(),startButton.getHeight(),matrix,true);
		resizeStartPress = Bitmap.createBitmap(startButtonPress,0,0,startButtonPress.getWidth(),startButtonPress.getHeight(),matrix,true);
		resizeExit = Bitmap.createBitmap(exitButton,0,0,exitButton.getWidth(),exitButton.getHeight(),matrix,true);
		resizeExitPress = Bitmap.createBitmap(exitButtonPress,0,0,exitButtonPress.getWidth(),exitButtonPress.getHeight(),matrix,true);

		button_x = screen_width / 2 - resizeStart.getWidth()*3/2 ;
		button_x2 = button_x +resizeStart.getWidth()*2;
		button_y = screen_height / 2 + resizeStart.getHeight();
		// 返回包围整个字符串的最小的一个Rect区域
		paint.setTextSize(40);   
		paint.getTextBounds(startGame, 0, startGame.length(), rect);
		strwid = rect.width();
		strhei = rect.height();
	}
	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if (!startButton.isRecycled()) {
			startButton.recycle();
		}
		if (!startButtonPress.isRecycled()) {
			startButtonPress.recycle();
		}
		if(!exitButton.isRecycled()){
			exitButton.recycle();
		}
		if(!exitButtonPress.isRecycled()){
			exitButtonPress.recycle();
		}

	}
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.WHITE); 						// 绘制背景色
			canvas.save();

			//波浪背景
			height = getHeight();
			width = getWidth();
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
				if (threadFlag) {
					lineY1 = Math.sin((i + angle) * Math.PI / 180);
					lineY2 = 10 * Math.sin((i + angle) * Math.PI / 180) + 20;
					lineY3 = 20 * Math.sin((i + angle) * Math.PI / 180) + 50;
				} else {
					lineY1 = 0;
					lineY2 = 20;
					lineY3 = 50;
				}
				canvas.drawLine((int) lineX, (int) (lineY1 + height / 2 ),
						(int) lineX + 1, (int) (lineY2 + height ), paint1);
				canvas.drawLine((int) lineX, (int) (lineY2 + height / 2),
						(int) lineX + 1, (int) (lineY3 + height / 2), paint2);
				canvas.drawLine((int) lineX, (int) (lineY3 + height / 2),
						(int) lineX + 1, height, paint3);
			}
			canvas.restore();
			//当手指滑过按钮时变换图片
			canvas.save();
			if (isBtChange) {
				canvas.drawBitmap(resizeStartPress, button_x, button_y, paint);
			} 
			else {
				canvas.drawBitmap(resizeStart, button_x, button_y, paint);
			}
			if (isBtChange2) {
				canvas.drawBitmap(resizeExitPress, button_x2, button_y, paint);
			}
			else {
				canvas.drawBitmap(resizeExit, button_x2, button_y, paint);
			}		
			paint.setTextSize(100);
			paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
			//返回包围整个字符串的最小的一个Rect区域
			//paint.getTextBounds(startGame, 0, startGame.length(), rect);
			//canvas.drawText(startGame, screen_width/2 - strwid/2, button_y + resizeStart.getHeight()/2 + strhei/2, paint);
			//canvas.drawText(exitGame, screen_width/2 - strwid/2, button_y + resizeStart.getHeight()/2 + strhei/2, paint);
			//paint.setTextSize(60);
			float textlong = paint.measureText("总分:"+ String.valueOf(score));
			canvas.drawText("总分:"+ String.valueOf(score), screen_width/2 - textlong/2, screen_height/2 - 100, paint);
			canvas.restore();
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {
			long startTime = System.currentTimeMillis();
			drawSelf();
			long endTime = System.currentTimeMillis();
			try {
				if (endTime - startTime < 400)
					Thread.sleep(400 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
	public void setScore(int score) {
		this.score = score;
	}
}
