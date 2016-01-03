package com.alesegdia.demux.screen;

import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class TilemapScreen implements Screen {

	private GdxGame g;
	private TilemapWrapper currentMap;

	public TilemapScreen( GdxGame g, TilemapWrapper startMap )
	{
		this.g = g;
		this.currentMap = startMap;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta)
	{	
		handleInput(delta);
		
        g.cam.update();
        g.batch.setProjectionMatrix(g.cam.combined);
        
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		currentMap.render(g.cam);
	}

	private void handleInput( float delta ) {
		if( Gdx.input.isKeyPressed(Input.Keys.A) ) g.cam.position.x -= delta;
		if( Gdx.input.isKeyPressed(Input.Keys.D) ) g.cam.position.x += delta;
		if( Gdx.input.isKeyPressed(Input.Keys.S) ) g.cam.position.y -= delta;
		if( Gdx.input.isKeyPressed(Input.Keys.W) ) g.cam.position.y += delta;
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
