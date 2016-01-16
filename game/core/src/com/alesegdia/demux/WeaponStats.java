package com.alesegdia.demux;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.ShootComponent.BulletModel;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.badlogic.gdx.math.Vector2;

public class WeaponStats {

	public int powerLimit = 10;
	public int power;

	public int rateLimit = 10;
	public int rate;

	public int ttlLimit = 10;
	public int ttl;

	public int speedLimit = 10;
	public int speed;
	
	// triple gun, sine gun, 5-gun
	public int modifier = 2;
	
	public WeaponStats(int p, int r, int t, int s) {
		power = p;
		rate = r;
		ttl = t;
		speed = s;
	}
	
	private List<BulletEntry> makeDefaultType()
	{
		// just for a single bullet by now
		BulletEntry be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		
		provideStats(be);
		
		List<BulletEntry> bes = new LinkedList<BulletEntry>();
		bes.add(be);
		return bes;		
	}
	
	private List<BulletEntry> makeBinaryType()
	{
		List<BulletEntry> bes = new LinkedList<BulletEntry>();
		
		// just for a single bullet by now
		BulletEntry be;
		
		be = new BulletEntry();
		be.origin = new Vector2(-.40f, -.1f);
		be.bm = new BulletModel();
		provideStats(be);
		bes.add(be);

		be = new BulletEntry();
		be.origin = new Vector2(-.40f, .1f);
		be.bm = new BulletModel();
		provideStats(be);
		bes.add(be);

		return bes;
	}
	
	private List<BulletEntry> makeTriviumType()
	{
		List<BulletEntry> bes = new LinkedList<BulletEntry>();
		
		// just for a single bullet by now
		BulletEntry be;
		
		be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		provideStats(be);
		be.bm.dir = new Vector2(1,0.6f);
		be.bm.horizontal = false;
		bes.add(be);

		be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		provideStats(be);
		bes.add(be);

		be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		provideStats(be);
		be.bm.dir = new Vector2(1,-0.6f);
		be.bm.horizontal = false;
		bes.add(be);

		return bes;
	}
	
	private List<BulletEntry> makeWavegunType()
	{
		List<BulletEntry> bes = new LinkedList<BulletEntry>();
		
		// just for a single bullet by now
		BulletEntry be;
		be = new BulletEntry();
		be.origin = new Vector2(-.40f, .02f);
		be.bm = new BulletModel();
		provideStats(be);
		be.bm.sinegun = true;
		bes.add(be);
		
		return bes;
		
	}
	
	private List<BulletEntry> makeDemuxType()
	{
		return null;
		
	}
	
	private void provideStats(BulletEntry be)
	{
		be.bm.destructionTime = Interpolators.Instance().ttl.get(ttl * 0.1f);
		be.bm.h = 5;
		be.bm.w = 5;
		be.bm.tr = Gfx.playerBulletTexture;
		be.bm.horizontal = true;
		be.bm.isPlayer = true;
		be.bm.trespassingEnabled = false;
		be.bm.speed = Interpolators.Instance().speed.get(speed * 0.1f);
	}

	public WeaponModel makeModel()
	{
		WeaponModel wm = new WeaponModel();
		wm.rate = Interpolators.Instance().rate.get((10-rate) * 0.1f);

		wm.ws = this;
		
		switch( modifier )
		{
		case 0: wm.bulletEntries = makeDefaultType(); break;
		case 1: wm.bulletEntries = makeBinaryType(); break;
		case 2: wm.bulletEntries = makeTriviumType(); break;
		case 3: wm.bulletEntries = makeWavegunType(); break;
		case 4: wm.bulletEntries = makeDemuxType(); break;
		}

		return wm;
	}

	public float computeShootCost() {
		System.out.println("computin");
		return power + rate + speed + ttl;
	}
	
}
