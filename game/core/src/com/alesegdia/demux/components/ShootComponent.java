package com.alesegdia.demux.components;

import java.util.List;

import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShootComponent extends Component {
	
	public static enum ShootType {
		DEFAULT
	}

	public static class BulletModel {
		public ShootType shootType;
		public boolean isPlayer = false;
		public float destructionTime;
		public int power;
		public int speed;
		public TextureRegion tr;
		public float w;
		public float h;
		public Vector2 dir;
		public boolean horizontal = true;
		public boolean sinegun = false;
		public boolean trespassingEnabled = false;
	}
	
	public int soundIndex = 1;
	
	public static class BulletEntry {
		public BulletModel bm;
		public Vector2 origin;
	}
	
	//public BulletModel bulletModel;
	
	public boolean horizontal = true;
	public ShootType shootType = ShootType.DEFAULT; 
	
	public List<BulletEntry> bulletConfigs;
	
}
