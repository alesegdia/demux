package com.alesegdia.demux;

import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera cam;
	
	public GameScreen gameScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(12, 12 * (h / w));
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        Tmx.Initialize();
        
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}

}
