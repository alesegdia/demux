package com.alesegdia.demux.components;

import java.util.List;

import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.ecs.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShootComponent extends Component {
	
	public static enum ShootType {
		DEFAULT
	}

	public static class BulletModel {
		public BulletModel(BulletModel bm) {
			this.shootType = bm.shootType;
			this.isPlayer = bm.isPlayer;
			this.destructionTime = bm.destructionTime;
			this.power = bm.power;
			this.speed = bm.speed;
			this.tr = bm.tr;
			this.w = bm.w;
			this.h = bm.h;

			this.horizontal = bm.horizontal;
			this.sinegun = bm.sinegun;
			this.trespassingEnabled = bm.trespassingEnabled;

		}
		public BulletModel() {
			// TODO Auto-generated constructor stub
		}
		public ShootType shootType;
		public boolean isPlayer = false;
		public float destructionTime;
		public float power;
		public float speed;
		public TextureRegion tr;
		public float w;
		public float h;
		
		/**
		 * To be set when created a bullet from this
		 */
		public Vector2 dir;
		public boolean horizontal = true;
		public boolean sinegun = false;
		public boolean trespassingEnabled = false;
	}
	
	public int soundIndex = 1;
	
	public static class BulletEntry {
		public BulletEntry()
		{
			
		}
		
		public BulletEntry(float oX, float oY, BulletModel bm2) {
			this.origin = new Vector2(oX, oY);
			this.bm = new BulletModel(bm2);
		}
		public BulletEntry(BulletEntry be) {
			this.origin = new Vector2(be.origin);
			this.bm = new BulletModel(be.bm);
		}
		public BulletModel bm;
		public Vector2 origin;
	}
	
	//public BulletModel bulletModel;
	
	public boolean horizontal = true;
	public ShootType shootType = ShootType.DEFAULT; 
	
	public List<BulletEntry> bulletConfigs;
	
}
