package com.alesegdia.demux;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.screen.MapGameplayScreen;
import com.alesegdia.demux.screen.RestartGameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxGame extends Game {
	public SpriteBatch batch;
	public ShapeRenderer srend;
	public OrthographicCamera cam;
	
	public MapGameplayScreen tilemapScreen;
	public Physics physics;
	public RestartGameScreen restartScreen;

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

        Tmx.Initialize();
        Gfx.Initialize();
        
		physics = new Physics();

        tilemapScreen = new MapGameplayScreen(this);
        restartScreen = new RestartGameScreen(this);
        setScreen(restartScreen);
	}

	@Override
	public void render () {
		super.render();
	}

}
