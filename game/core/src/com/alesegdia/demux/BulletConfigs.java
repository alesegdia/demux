package com.alesegdia.demux;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.ShootComponent.BulletModel;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
import com.alesegdia.demux.interp.Lerper;
import com.badlogic.gdx.math.Vector2;

public class BulletConfigs {

	public List<BulletEntry> threeHeadedBEList = new ArrayList<BulletEntry>();
	public List<BulletEntry> runnerBEList = new ArrayList<BulletEntry>();
	public List<BulletEntry> jumperBEList = new ArrayList<BulletEntry>();
	public List<BulletEntry> demonBEList = new ArrayList<BulletEntry>();
	
	
	BulletModel standardEnemyBM = new BulletModel();
	BulletModel standardEnemyDirectionalBM = new BulletModel();
	
	
	public static List<BulletEntry> playerDefaultBEList = new ArrayList<BulletEntry>();
	BulletModel playerDefaultBM = new BulletModel();
	
	public static List<BulletEntry> playerTriplasmaBEList = new ArrayList<BulletEntry>();
	BulletModel playerTriplasmaBM_T = new BulletModel();
	BulletModel playerTriplasmaBM_B = new BulletModel();
	BulletModel playerTriplasmaBM_M = new BulletModel();

	public static List<BulletEntry> playerFireballBEList = new ArrayList<BulletEntry>();
	BulletModel playerFireballBM = new BulletModel();
	
	public static List<BulletEntry> playerSinegunBEList = new ArrayList<BulletEntry>();
	BulletModel playerSinegunBM = new BulletModel();
	
	public static List<BulletEntry> playerSacredpunchBEList = new ArrayList<BulletEntry>();
	BulletModel playerSacredpunchBM = new BulletModel();
	
	public static List<BulletEntry> playerTroquegunBEList = new ArrayList<BulletEntry>();
	BulletModel playerTroquegunBM = new BulletModel();
	public List<BulletEntry> maskBEList = new ArrayList<BulletEntry>();
	
	public static WeaponModel defaultGun = new WeaponModel();
	public static WeaponModel fireball = new WeaponModel();
	public static WeaponModel triplasma = new WeaponModel();
	public static WeaponModel sineGun = new WeaponModel();
	public static WeaponModel sacredPunch = new WeaponModel();
	public static WeaponModel troqueGun = new WeaponModel();
	
	
	public BulletConfigs() {

		/* PLAYER DEFAULT SHOOT CONFIG */
		playerDefaultBM.h = 5;
		playerDefaultBM.w = 5;
		playerDefaultBM.isPlayer = true;

		playerDefaultBM.speed = Interpolators.Instance().speed.get(0.3f);
		playerDefaultBM.tr = Gfx.playerBulletTexture;
		playerDefaultBM.power = Interpolators.Instance().power.get(0.2f);
		
		playerDefaultBM.destructionTime = Interpolators.Instance().ttl.get(0.3f);

		createEntryInList( -.40f, .02f, playerDefaultBM, playerDefaultBEList );
		
		defaultGun.rate = Interpolators.Instance().rate.get(0.2f);
		defaultGun.bulletEntries = playerDefaultBEList;
		//defaultGun.tr = Gfx.defaultSymbol;

	}
	
	void createEntryInList( float oX, float oY, BulletModel bm, List<BulletEntry> belist ) {
		belist.add(new BulletEntry(oX, oY, bm));
	}
	
}
