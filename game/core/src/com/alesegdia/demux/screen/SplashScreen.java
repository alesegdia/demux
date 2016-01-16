package com.alesegdia.demux.screen;

import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class SplashScreen implements Screen {

	private GdxGame g;

	public SplashScreen( GdxGame g )
	{
		this.g = g;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE) )
		{
			g.setScreen(g.restartScreen);
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		g.menuCam.update();
		g.batch.setProjectionMatrix(g.menuCam.combined);

		g.batch.begin();
		g.batch.draw(Gfx.splashTexture, 0, 0);
		g.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
