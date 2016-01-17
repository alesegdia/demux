package com.alesegdia.demux.screen;

import com.alesegdia.demux.DirectionUtils;
import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.GameWorld;
import com.alesegdia.demux.GameWorld.Notification;
import com.alesegdia.demux.GdxGame;
import com.alesegdia.demux.PickupEntry;
import com.alesegdia.demux.PickupType;
import com.alesegdia.demux.PlayerRespawnData;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.assets.Tmx;
import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.UpgradesComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.map.MapObjectPositionCollector;
import com.alesegdia.demux.map.MapPickupCollector;
import com.alesegdia.demux.map.PickupLocations;
import com.alesegdia.demux.physics.CollisionLayers;
import com.alesegdia.demux.physics.MapBodyBuilder;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class MapGameplayScreen implements Screen {

	private GdxGame g;
	private TilemapWrapper currentMap;
	public GameWorld gw;
	Room currentRoom;
	private boolean physicsDebug = false;

	public MapGameplayScreen( GdxGame g )
	{
		this.g = g;
		gw = new GameWorld( g.physics, g.batch, g.cam );
	}
	
	public void reset( Room startRoom )
	{
		reset(startRoom, new PlayerRespawnData());
	}
	
	public void reset( Room startRoom, PlayerRespawnData prd )
	{
		this.currentMap = Tmx.GetMap(startRoom.rinfo.id);
		this.currentRoom = startRoom;
		
		gw.clear();
		g.physics.Dispose();

		if( prd.spawnPos == null )
		{
			gw.makePlayer(32, 40, prd);
		}
		else
		{
			gw.makePlayer((int)prd.spawnPos.x, (int)prd.spawnPos.y, prd );
		}
		
		Array<Body> collision_bodies = MapBodyBuilder.buildShapes(this.currentMap.tilemap, "collision", GameConfig.METERS_TO_PIXELS, g.physics.world(),
				CollisionLayers.CATEGORY_MAP, CollisionLayers.MASK_MAP, CollisionLayers.GROUP_MAP );

		Array<Body> enemylimits_bodies = MapBodyBuilder.buildShapes(this.currentMap.tilemap, "enemy-limits", GameConfig.METERS_TO_PIXELS, g.physics.world(),
				CollisionLayers.CATEGORY_ENEMYLIMIT, CollisionLayers.MASK_ENEMYLIMIT, CollisionLayers.GROUP_ENEMYLIMIT );
		
		Array<Vector2> spawners = MapObjectPositionCollector.Collect(this.currentMap.tilemap, "enemy-spawn-easy", GameConfig.METERS_TO_PIXELS);
		for( Vector2 v : spawners )
		{
			gw.makeSlimeEnemy(v.x, v.y);
		}

		gw.buildMapEntities(startRoom);
		
		gw.resetScroller(this.currentMap.tilemap);
		
		startRoom.isVisited = true;
		
		//PickupLocations pls = MapPickupCollector.collect(Tmx.GetMap(currentMap.rinfo.id).tilemap, GameConfig.METERS_TO_PIXELS);
		
		for( PickupEntry pe : g.restartScreen.pickupMap.get(currentRoom) )
		{
			if( !pe.collected )
			{
				gw.makePickup(pe);
			}
		}
		
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta)
	{	
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.F9) )
		{
			this.physicsDebug = !this.physicsDebug;
		}
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
			Vector2 pos = new Vector2(plc.gotoRoom.connectedLink.relCoord.x * GameConfig.BLOCK_X, plc.gotoRoom.connectedLink.relCoord.y * GameConfig.BLOCK_Y);
			Vector2 offset = DirectionUtils.GetOffsetForDirection(plc.gotoRoom.connectedLink.direction);
			switch(plc.gotoRoom.connectedLink.direction)
			{
			case TOP:
				offset.y -= 1f;
				break;
			case DOWN:
				offset.y += 1f;
				break;
			case RIGHT:
				offset.x -= 0.8f;
				offset.y -= 0.2f;
				break;
			case LEFT:
				offset.x += 0.8f;
				offset.y -= 0.2f;
				break;
			}
			pos.add(offset);
			pos.x *= GameConfig.METERS_TO_PIXELS / 2f;
			pos.y *= GameConfig.METERS_TO_PIXELS / 2f;		

			PlayerRespawnData prd = new PlayerRespawnData();
			prd.plc = plc;
			prd.prevlvc = ((LinearVelocityComponent) gw.getPlayer().getComponent(LinearVelocityComponent.class)).linearVelocity;
			prd.spawnPos = pos;
			
			prd.ac = (AttackComponent) gw.getPlayer().getComponent(AttackComponent.class);
			prd.sc = (ShootComponent) gw.getPlayer().getComponent(ShootComponent.class);
			prd.wc = (WeaponComponent) gw.getPlayer().getComponent(WeaponComponent.class);
			prd.gc = (GaugeComponent) gw.getPlayer().getComponent(GaugeComponent.class);
			prd.stc = (StaminaComponent) gw.getPlayer().getComponent(StaminaComponent.class);
			prd.uc = (UpgradesComponent) gw.getPlayer().getComponent(UpgradesComponent.class);
			
			this.reset(plc.gotoRoom.connectedRoom, prd);			
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) )
		{
			System.out.println("going to menu screen");
			g.setScreen(g.menuScreen);
		}
		
		g.textCam.update();
		g.batch.setProjectionMatrix(g.textCam.combined);
		g.batch.begin();
		GaugeComponent gac = (GaugeComponent) gw.getPlayer().getComponent(GaugeComponent.class);
		StaminaComponent stc = (StaminaComponent) gw.getPlayer().getComponent(StaminaComponent.class);
		g.font.draw(g.batch, gac.gauge() + " " + stc.stamina(), 0, 20);
		
		
		gw.notificationTTL -= Gdx.graphics.getDeltaTime();
		if( gw.notificationTTL > 0 )
		{
			g.font.draw(g.batch, gw.notification, 10, 580);
		}
		
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
