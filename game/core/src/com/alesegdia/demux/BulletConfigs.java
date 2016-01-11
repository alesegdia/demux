package com.alesegdia.demux;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.ShootComponent.BulletModel;
import com.alesegdia.demux.components.WeaponComponent.WeaponModel;
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

		standardEnemyBM.h = 5;
		standardEnemyBM.w = 5;
		standardEnemyBM.tr = Gfx.playerBulletTexture;
		standardEnemyBM.speed = 10;
		standardEnemyBM.destructionTime = 1f;
		standardEnemyBM.isPlayer = false;
		standardEnemyBM.power = 1;
		
		standardEnemyDirectionalBM.h = 5;
		standardEnemyDirectionalBM.w = 5;
		standardEnemyDirectionalBM.tr = Gfx.playerBulletTexture;
		standardEnemyDirectionalBM.speed = 10;
		standardEnemyDirectionalBM.destructionTime = 1f;
		standardEnemyDirectionalBM.isPlayer = false;
		standardEnemyDirectionalBM.power = 1;
		standardEnemyDirectionalBM.horizontal = false;
		
		createEntryInList(0,0, standardEnemyDirectionalBM, maskBEList);
		
		createEntryInList( -0.5f, -0.35f, standardEnemyBM, threeHeadedBEList );
		createEntryInList( -0.5f, 0.3f, standardEnemyBM, threeHeadedBEList );
		createEntryInList( -0.5f, 0.75f, standardEnemyBM, threeHeadedBEList );

		/* RUNNER SHOOT CONFIG */
		createEntryInList( 0, 0.3f, standardEnemyBM, runnerBEList );
		
		/* JUMPER SHOOT CONFIG */
		createEntryInList( 0, 0.5f, standardEnemyBM, jumperBEList );
		
		/* DEMON SHOOT CONFIG */
		createEntryInList( -0.3f, 0f, standardEnemyDirectionalBM, demonBEList );
		createEntryInList( 0.3f, 0.3f, standardEnemyDirectionalBM, demonBEList );
		
		/* PLAYER DEFAULT SHOOT CONFIG */
		playerDefaultBM.h = 5;
		playerDefaultBM.w = 5;
		playerDefaultBM.isPlayer = true;
		playerDefaultBM.speed = 7;
		playerDefaultBM.tr = Gfx.playerBulletTexture;
		playerDefaultBM.power = 2;
		playerDefaultBM.destructionTime = 1f;
		createEntryInList( -0.40f, 0.02f, playerDefaultBM, playerDefaultBEList );
		defaultGun.rate = 0.2f;
		defaultGun.bulletEntries = playerDefaultBEList;
		//defaultGun.tr = Gfx.defaultSymbol;
		
		
	}
	
	void createEntryInList( float oX, float oY, BulletModel bm, List<BulletEntry> belist ) {
		BulletEntry be = new BulletEntry();
		be.origin = new Vector2(oX, oY);
		be.bm = bm;
		belist.add(be);
	}
	
}
