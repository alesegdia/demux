package com.alesegdia.demux;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.components.ActiveComponent;
import com.alesegdia.demux.components.AnimationComponent;
import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.RoomLinkComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.alesegdia.demux.ecs.Engine;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.systems.AnimationSystem;
import com.alesegdia.demux.systems.DashingSystem;
import com.alesegdia.demux.systems.DrawingSystem;
import com.alesegdia.demux.systems.FlipSystem;
import com.alesegdia.demux.systems.HumanControllerSystem;
import com.alesegdia.demux.systems.MovementSystem;
import com.alesegdia.demux.systems.UpdatePhysicsSystem;
import com.alesegdia.troidgen.room.Direction;
import com.alesegdia.troidgen.room.Link;
import com.alesegdia.troidgen.room.LinkInfo;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameWorld {
	
	public static GameWorld instance;

	Engine engine;
	private Physics physics;
	private Camera cam;
	
	public int getNumEntities() {
		return engine.getNumEntities();
	}

	Entity player;

	public GameWorld( Physics physics, SpriteBatch batch, Camera cam ) {
		this.physics = physics;
		this.cam = cam;
		engine = new Engine();
		engine.addSystem(new HumanControllerSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new UpdatePhysicsSystem());
		engine.addSystem(new DashingSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new FlipSystem());
		engine.addSystem(new DrawingSystem(batch), true);
		engine.addSystem(physics.physicsSystem);
	}
	
	public void clear()
	{
		engine.Clear();
	}
	
	public static TransformComponent playerPositionComponent;
	
	public void makePlayer(int x, int y, PlayerRespawnData prd) {
		player = new Entity();
		
		PhysicsComponent pc = (PhysicsComponent) player.addComponent(new PhysicsComponent());
		pc.body = physics.createPlayerBody(x, y);
		pc.body.setUserData(player);
		pc.grounded = false;
		if( prd.prevlvc != null )
		{
			pc.body.setLinearVelocity(prd.prevlvc);
		}
		
		GraphicsComponent gc = (GraphicsComponent) player.addComponent(new GraphicsComponent());
		System.out.println(gc);
		gc.drawElement = Gfx.playerSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		gc.hasShadowEffect = true;

		playerPositionComponent = (TransformComponent) player.addComponent(new TransformComponent());
		playerPositionComponent.position = pc.body.getPosition();
		
		AnimationComponent ac = (AnimationComponent) player.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.playerWalk;

		if( prd.plc != null )
		{
			prd.plc.gotoRoom = null;
			prd.plc.linkEntity = null;
			prd.plc.platform = null;
			player.addComponent(prd.plc);
		}
		else
		{
			PlayerComponent plyc = (PlayerComponent) player.addComponent(new PlayerComponent());
		}
		
		LinearVelocityComponent lvc = (LinearVelocityComponent) player.addComponent(new LinearVelocityComponent());		
		lvc.speed.set(0.5f,0.25f);
		lvc.cap.y = 10;
		lvc.doCap[1] = false;
		if( prd.prevlvc != null )
		{
			lvc.linearVelocity.set(prd.prevlvc);
		}
		
		ActiveComponent actc = (ActiveComponent) player.addComponent(new ActiveComponent());
		actc.isActive = true;
		
		DashComponent dc = (DashComponent) player.addComponent(new DashComponent());
		
		
		engine.addEntity(player);
	}
	
	public void makeEntrance(Link l)
	{
		Entity entrance = new Entity();
		
		TransformComponent tc = (TransformComponent) entrance.addComponent(new TransformComponent());
		tc.position.set( l.relCoord.x * 8, l.relCoord.y * 8 );
		tc.position.add( getOffsetForDirection(l.direction) );
		
		GraphicsComponent gc = (GraphicsComponent) entrance.addComponent(new GraphicsComponent());
		gc.drawElement = Gfx.entranceSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		
		AnimationComponent ac = (AnimationComponent) entrance.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.entranceAnims[getIndexForDirection(l.direction)];
		
		RoomLinkComponent rlc = (RoomLinkComponent) entrance.addComponent(new RoomLinkComponent());
		rlc.link = l;
		
		PhysicsComponent phc = (PhysicsComponent) entrance.addComponent(new PhysicsComponent());
		Vector2 pos = new Vector2(l.relCoord.x * 16 * 8, l.relCoord.y * 16 * 8);
		
		Vector2 offset = getOffsetForDirection(l.direction);
		offset.x *= 16;
		offset.y *= 16;
		
		pos.add(offset);
		
		phc.body = physics.createLinkBody(pos.x, pos.y);
		phc.body.setUserData(entrance);
		
		engine.addEntity(entrance);
	}
	
	private void makeBlockEntrance(Link l) {
		Entity entrance = new Entity();
		
		TransformComponent tc = (TransformComponent) entrance.addComponent(new TransformComponent());
		tc.position.set( l.relCoord.x * 8, l.relCoord.y * 8 );
		tc.position.add( getOffsetForDirection(l.direction) );
		
		PhysicsComponent phc = (PhysicsComponent) entrance.addComponent(new PhysicsComponent());
		Vector2 pos = new Vector2(l.relCoord.x * GameConfig.BLOCK_X * 8, l.relCoord.y * GameConfig.BLOCK_Y * 8);
		
		Vector2 offset = getOffsetForDirection2(l.direction);
		offset.x *= GameConfig.BLOCK_X;
		offset.y *= GameConfig.BLOCK_Y;
		
		pos.add(offset);
		
		Vector2 sz = getSizeForDirection2(l.direction);
		phc.body = physics.createBlockedLinkBody(pos.x, pos.y, sz.x, sz.y);
		phc.body.setUserData(entrance);
		
		engine.addEntity(entrance);		
	}



	public Vector2 getOffsetForDirection(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = GameConfig.BLOCK_X / 4f;
			offset.y = GameConfig.BLOCK_Y / 2f - 0.5f;
			break;
		case DOWN:
			offset.x = GameConfig.BLOCK_X / 4f;
			offset.y = 0.5f;
			break;			
		case LEFT:
			offset.x = 0.5f;
			offset.y = 1f;
			break;
		case RIGHT:
			offset.x = GameConfig.BLOCK_X / 2f - 0.5f;
			offset.y = 1;
		}
		return offset;
	}

	public Vector2 getOffsetForDirection2(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = GameConfig.BLOCK_X / 4f;
			offset.y = GameConfig.BLOCK_Y / 2f - 0.25f;
			break;
		case DOWN:
			offset.x = GameConfig.BLOCK_X / 4f;
			offset.y = 0.25f;
			break;			
		case LEFT:
			offset.x = 0.25f;
			offset.y = 1;
			break;
		case RIGHT:
			offset.x = GameConfig.BLOCK_X / 2f - 0.25f;
			offset.y = 1;
		}
		return offset;
	}

	public Vector2 getSizeForDirection2(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = 0.25f;
			offset.y = 0.25f;
			break;
		case DOWN:
			offset.x = 0.50f;
			offset.y = 0.255f;
			break;			
		case LEFT:
			offset.x = 0.25f;
			offset.y = 0.25f;
			break;
		case RIGHT:
			offset.x = 0.25f;
			offset.y = 0.25f;
		}
		return offset;
	}

	private int getIndexForDirection(Direction dir) {
		switch(dir)
		{
		case TOP: return 2;
		case DOWN: return 3;
		case LEFT: return 1;
		case RIGHT: return 0;
		}
		return -1;
	}

	public void setCam() {
		cam.position.x = playerPositionComponent.position.x;
		cam.position.y = playerPositionComponent.position.y;
	}
	
	public void step() {
		engine.step();
	}
	
	public void render() {
		engine.render();
	}

	public Entity addToEngine(Entity e) {
		return engine.addEntity(e);
	}

	public Entity getPlayer() {
		return player;
	}

	public boolean isPlayerDead() {
		return player.isDead;
	}

	public void buildMapEntities(Room r) {
		for( Link l : r.links )
		{
			if( l.isConnected() )
			{
				makeEntrance(l);
			}
			else
			{
				makeBlockEntrance(l);
			}
		}
	}

}
