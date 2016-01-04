package com.alesegdia.demux.screen;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GameWorld;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.physics.MapBodyBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class TilemapScreen implements Screen {

	private GdxGame g;
	private TilemapWrapper currentMap;
	private GameWorld gw;

	public TilemapScreen( GdxGame g, TilemapWrapper startMap )
	{
		this.g = g;
		this.currentMap = startMap;
	}
	
	@Override
	public void show() {
		gw = new GameWorld(g.physics, g.batch, g.cam);
		gw.makePlayer(200, 40);
		Array<Body> bodies = MapBodyBuilder.buildShapes(this.currentMap.tilemap, GameConfig.METERS_TO_PIXELS, g.physics.world());
		for( Body b : bodies )
		{
			
		}
	}

	@Override
	public void render(float delta)
	{	
		
		g.cam.position.x = gw.playerPositionComponent.position.x;
		g.cam.position.y = gw.playerPositionComponent.position.y;

		gw.step();
		g.physics.step(delta);

		gw.setCam();
        g.cam.update();
        g.batch.setProjectionMatrix(g.cam.combined);
        
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		currentMap.render(g.cam);
		
		
		g.batch.begin();
		gw.render();
		g.batch.end();
		
		g.physics.render(g.cam);
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
