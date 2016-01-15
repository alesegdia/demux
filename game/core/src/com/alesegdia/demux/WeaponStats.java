package com.alesegdia.demux;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.ShootComponent.BulletModel;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.badlogic.gdx.math.Vector2;

public class WeaponStats {

	public int powerLimit = 4;
	public int power;

	public int rateLimit = 4;
	public int rate;

	public int ttlLimit = 4;
	public int ttl;

	public int speedLimit = 4;
	public int speed;
	
	// triple gun, sine gun, 5-gun
	public int modifier;
	
	public WeaponStats(int p, int r, int t, int s) {
		power = p;
		rate = r;
		ttl = t;
		speed = s;
	}

	public WeaponModel makeModel()
	{
		WeaponModel wm = new WeaponModel();
		wm.rate = Interpolators.Instance().rate.get((10-rate) * 0.1f);

		// just for a single bullet by now
		BulletEntry be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		
		be.bm.destructionTime = Interpolators.Instance().ttl.get(ttl * 0.1f);
		be.bm.h = 5;
		be.bm.w = 5;
		be.bm.tr = Gfx.playerBulletTexture;
		be.bm.horizontal = true;
		be.bm.isPlayer = true;
		be.bm.trespassingEnabled = false;
		be.bm.speed = Interpolators.Instance().speed.get(speed * 0.1f);
		
		List<BulletEntry> bes = new LinkedList<BulletEntry>();
		bes.add(be);
		
		wm.ws = this;
		
		wm.bulletEntries = bes;

		return wm;
	}
	
}
