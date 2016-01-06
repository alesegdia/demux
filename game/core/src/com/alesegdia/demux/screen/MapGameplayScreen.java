package com.alesegdia.demux.screen;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GameWorld;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.physics.MapBodyBuilder;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class MapGameplayScreen implements Screen {

	private GdxGame g;
	private TilemapWrapper currentMap;
	private GameWorld gw;
	private Room currentRoom;
	private boolean physicsDebug = false;

	public MapGameplayScreen( GdxGame g )
	{
		this.g = g;
		gw = new GameWorld( g.physics, g.batch, g.cam );
	}
	
	public void reset( Room startRoom )
	{
		reset(startRoom, null, null);
	}
	
	public void reset( Room startRoom, Vector2 spawnPos, LinearVelocityComponent plc )
	{
		System.out.println(startRoom.rinfo.id);
		this.currentMap = Tmx.GetMap(startRoom.rinfo.id);
		this.currentRoom = startRoom;
		
		gw.clear();
		g.physics.clear();

		if( spawnPos == null )
		{
			gw.makePlayer(200, 40, plc);
		}
		else
		{
			gw.makePlayer((int)spawnPos.x, (int)spawnPos.y, plc);
		}
		
		Array<Body> bodies = MapBodyBuilder.buildShapes(this.currentMap.tilemap, GameConfig.METERS_TO_PIXELS, g.physics.world());

		gw.buildMapEntities(startRoom);
		
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta)
	{	
		
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
		
		if( this.physicsDebug )
		{
			g.physics.render(g.cam);			
		}
		
		PlayerComponent plc = (PlayerComponent) gw.getPlayer().getComponent(PlayerComponent.class);
		if( plc.gotoRoom != null )
		{
			Vector2 pos = new Vector2(plc.gotoRoom.connectedLink.relCoord.x * 16 * 8, plc.gotoRoom.connectedLink.relCoord.y * 16 * 8);
			Vector2 offset = gw.getOffsetForDirection(plc.gotoRoom.connectedLink.direction);
			offset.x *= 16;
			offset.y *= 16;

			
			switch(plc.gotoRoom.connectedLink.direction)
			{
			case TOP:
				offset.y -= 8;
				break;
			case DOWN:
				offset.y += 8;
				break;
			case RIGHT:
				offset.x -= 8;
				break;
			case LEFT:
				offset.x += 8;
				break;
			}
			pos.add(offset);

			this.reset(plc.gotoRoom.connectedRoom, pos, (LinearVelocityComponent) gw.getPlayer().getComponent(LinearVelocityComponent.class));			
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		g.srend.setAutoShapeType(true);
		g.srend.begin(ShapeType.Filled);
		
		for( Room r : g.restartScreen.roomLayout )
		{
			if( r == this.currentRoom )
			{
				g.srend.setColor(1,0,0,0.5f);
			}
			else
			{
				g.srend.setColor(1,1,1,0.5f);
			}
			g.srend.rect(150 + r.position.x * 16, 150 + r.position.y * 16, r.size.x * 16, r.size.y * 16);
		}
		

		g.srend.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

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
