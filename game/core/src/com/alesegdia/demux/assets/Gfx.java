package com.alesegdia.demux.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gfx {

	public static Animation playerWalk;
	public static Animation playerStand;
	public static Animation playerJumpUp;
	public static Animation playerJumpDown;
	public static Spritesheet playerSheet;
	
	public static Spritesheet entranceSheet;
	public static Animation[] entranceAnims;
	public static Animation[] entranceOpenedAnims;
	public static Animation playerDash;
	public static TextureRegion playerBulletTexture;
	
	public static Texture menuTexture;
	
	public static Spritesheet pickupsSheet;
	public static Animation healPickupAnim;
	public static Animation incSpPickupAnim;
	public static Animation incGaugeAnim;
	public static Animation dashAbilityAnim;
	public static Animation incStaminaPickupAnim;
	public static Animation triModAnim;
	public static Animation sineModAnim;
	public static Animation demuxModAnim;
	public static Animation superJumpAbilityAnim;
	public static Animation biModPickupAnim;
	public static Texture splashTexture;
	
	public static Spritesheet slimeSheet;
	public static Animation slimeAnim;
	
	public static Spritesheet jumperSheet;
	public static Animation jumperAnim;
	
	public static void Initialize()
	{
		playerSheet = new Spritesheet("tiacascoanim2.png", 3, 3);
		playerStand = new Animation(0.2f, playerSheet.get(0));
		playerJumpUp = new Animation(0.2f, playerSheet.get(1));
		playerJumpDown = new Animation(0.2f, playerSheet.get(1));
		playerDash = new Animation(0.2f, playerSheet.get(6));
		playerWalk = new Animation(0.1f, playerSheet.getRange(2, 4));
		playerWalk.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		entranceSheet = new Spritesheet("entrance-sheet.png", 2, 4);
		entranceAnims = new Animation[4];
		entranceAnims[0] = new Animation(0.2f, entranceSheet.get(0));
		entranceAnims[1] = new Animation(0.2f, entranceSheet.get(1));
		entranceAnims[2] = new Animation(0.2f, entranceSheet.get(2));
		entranceAnims[3] = new Animation(0.2f, entranceSheet.get(3));
		
		entranceOpenedAnims = new Animation[4];
		entranceOpenedAnims[0] = new Animation(0.2f, entranceSheet.get(4));
		entranceOpenedAnims[1] = new Animation(0.2f, entranceSheet.get(5));
		entranceOpenedAnims[2] = new Animation(0.2f, entranceSheet.get(6));
		entranceOpenedAnims[3] = new Animation(0.2f, entranceSheet.get(7));
		
		Texture pt = new Texture(Gdx.files.internal("bala.png"));
		playerBulletTexture = new TextureRegion();
		playerBulletTexture.setRegion(pt);
		
		pickupsSheet = new Spritesheet("pickups-sheet.png", 4, 3);
		
		healPickupAnim = new Animation(0.2f, pickupsSheet.get(0));
		incSpPickupAnim = new Animation(0.2f, pickupsSheet.get(1));
		dashAbilityAnim = new Animation(0.2f, pickupsSheet.get(2));
		triModAnim = new Animation(0.2f, pickupsSheet.get(3));
		sineModAnim = new Animation(0.2f, pickupsSheet.get(4));
		demuxModAnim = new Animation(0.2f, pickupsSheet.get(5));
		superJumpAbilityAnim = new Animation(0.2f, pickupsSheet.get(6));
		incStaminaPickupAnim = new Animation(0.2f, pickupsSheet.get(7));
		biModPickupAnim = new Animation(0.2f, pickupsSheet.get(8));
		incGaugeAnim = new Animation(0.2f, pickupsSheet.get(9));

		menuTexture = new Texture(Gdx.files.internal("menu.png"));
		splashTexture = new Texture(Gdx.files.internal("splash.png"));
		
		slimeSheet = new Spritesheet("enemy-slime-sheet.png", 1, 2);
		slimeAnim = new Animation(0.2f, slimeSheet.getRange(0, 1));
		
		jumperSheet = new Spritesheet("enemy-jumper-sheet.png", 1, 4);
		jumperAnim = new Animation(0.2f, jumperSheet.getRange(2, 3));
		
	}
	
}
