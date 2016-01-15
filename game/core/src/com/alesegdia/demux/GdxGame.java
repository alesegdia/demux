package com.alesegdia.demux;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.screen.MapGameplayScreen;
import com.alesegdia.demux.screen.MenuScreen;
import com.alesegdia.demux.screen.RestartGameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxGame extends Game {
	
	public SpriteBatch batch;
	
	public ShapeRenderer srend;
	public OrthographicCamera cam;
	public OrthographicCamera menuCam;
	public OrthographicCamera textCam;
	
	public MapGameplayScreen tilemapScreen;
	public RestartGameScreen restartScreen;
	public MenuScreen menuScreen;
	
	public Physics physics;
	
	public BitmapFont font;
	public BitmapFont fontBig;
	public BitmapFont fontRlyBig;

	@Override
	public void create () {
		batch = new SpriteBatch();
		srend = new ShapeRenderer();
		
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(12, 12 * (h / w));
        cam.setToOrtho(false, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        menuCam = new OrthographicCamera();
        menuCam.setToOrtho(false, GameConfig.VIEWPORT_WIDTH * GameConfig.METERS_TO_PIXELS, GameConfig.VIEWPORT_HEIGHT * GameConfig.METERS_TO_PIXELS);
        menuCam.position.set(menuCam.viewportWidth / 2f, menuCam.viewportHeight / 2f, 0);
        menuCam.update();
        
        textCam = new OrthographicCamera();
        textCam.setToOrtho(false, 800, 600);
        textCam.position.set(400, 300, 0);
        textCam.update();
        
        Tmx.Initialize();
        Gfx.Initialize();
        
		physics = new Physics();

        tilemapScreen = new MapGameplayScreen(this);
        restartScreen = new RestartGameScreen(this);
        menuScreen = new MenuScreen(this);
        setScreen(restartScreen);
        
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("visitor1.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		font = generator.generateFont(parameter); // font size 12 pixels
		fontBig = generator.generateFont(parameter); // font size 12 pixels
		fontRlyBig = generator.generateFont(parameter); // font size 12 pixels
		
		generator.dispose();

	}

	@Override
	public void render () {
		super.render();
	}

}
