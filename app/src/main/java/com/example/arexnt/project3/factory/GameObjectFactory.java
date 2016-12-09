package com.example.arexnt.project3.factory;

import android.content.res.Resources;

import com.example.arexnt.project3.object.EnemyFish1;
import com.example.arexnt.project3.object.EnemyFish2;
import com.example.arexnt.project3.object.EnemyFish3;
import com.example.arexnt.project3.object.EnemyFish4;
import com.example.arexnt.project3.object.EnemyFish5;
import com.example.arexnt.project3.object.GameObject;
import com.example.arexnt.project3.object.MyFish;

/*游戏对象的工厂类*/
public class GameObjectFactory {
	//创建小型敌机的方法
	public GameObject createSmallPlane(Resources resources){
		return new EnemyFish1(resources);
	}
	//创建中型敌机的方法
	public GameObject createMiddlePlane(Resources resources){
		return new EnemyFish2(resources);
	}
	//创建大型敌机的方法
	public GameObject createBigPlane(Resources resources){
		return new EnemyFish3(resources);
	}
	//创建BOSS敌机的方法
	public GameObject createBossPlane(Resources resources){
		return new EnemyFish4(resources);
	}
	//创建骷髅鱼的方法
	public GameObject createDeathPlane(Resources resources){
		return new EnemyFish5(resources);
	}
	//创建玩家飞机的方法
	public GameObject createMyPlane(Resources resources){
		return new MyFish(resources);
	}

}
