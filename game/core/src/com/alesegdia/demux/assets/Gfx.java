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
	public static Animation playerDash;
	public static TextureRegion playerBulletTexture;
	
	public static Texture menuTexture;
	public static Sprite menuSprite;

	public static void Initialize()
	{
		playerSheet = new Spritesheet("tiacascoanim2.png", 3, 3);
		playerStand = new Animation(0.2f, playerSheet.get(0));
		playerJumpUp = new Animation(0.2f, playerSheet.get(1));
		playerJumpDown = new Animation(0.2f, playerSheet.get(1));
		playerDash = new Animation(0.2f, playerSheet.get(6));
		playerWalk = new Animation(0.1f, playerSheet.getRange(2, 4));
		playerWalk.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		entranceSheet = new Spritesheet("entrance-sheet.png", 1, 4);		
		entranceAnims = new Animation[4];
		entranceAnims[0] = new Animation(0.2f, entranceSheet.get(0));
		entranceAnims[1] = new Animation(0.2f, entranceSheet.get(1));
		entranceAnims[2] = new Animation(0.2f, entranceSheet.get(2));
		entranceAnims[3] = new Animation(0.2f, entranceSheet.get(3));
		
		Texture pt = new Texture(Gdx.files.internal("bala.png"));
		playerBulletTexture = new TextureRegion();
		playerBulletTexture.setRegion(pt);

		menuTexture = new Texture(Gdx.files.internal("menu.png"));
		
		menuSprite = new Sprite();
		menuSprite.setTexture(menuTexture);
		menuSprite.setRegion(new TextureRegion(menuTexture));
		menuSprite.setScale(10);
		menuSprite.setPosition(0, 0);
	}
	
}
