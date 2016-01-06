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
		engine.addSystem(new MovementSystem());
		engine.addSystem(new FlipSystem());
		engine.addSystem(new DrawingSystem(batch), true);
		engine.addSystem(physics.physicsSystem);
	}
	
	public static TransformComponent playerPositionComponent;
	
	public void makePlayer(int x, int y) {
		player = new Entity();
		
		PhysicsComponent pc = (PhysicsComponent) player.addComponent(new PhysicsComponent());
		pc.body = physics.createPlayerBody(x, y);
		pc.body.setUserData(player);
		
		GraphicsComponent gc = (GraphicsComponent) player.addComponent(new GraphicsComponent());
		System.out.println(gc);
		gc.drawElement = Gfx.playerSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		
		playerPositionComponent = (TransformComponent) player.addComponent(new TransformComponent());
		playerPositionComponent.position = pc.body.getPosition();
		
		AnimationComponent ac = (AnimationComponent) player.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.playerWalk;

		PlayerComponent plc = (PlayerComponent) player.addComponent(new PlayerComponent());

		LinearVelocityComponent lvc = (LinearVelocityComponent) player.addComponent(new LinearVelocityComponent());		
		lvc.speed.set(0.5f,0.25f);
		lvc.cap.y = 2;
		lvc.doCap[1] = true;
		
		ActiveComponent actc = (ActiveComponent) player.addComponent(new ActiveComponent());
		actc.isActive = true;
		
		engine.addEntity(player);
	}
	
	public void makeEntrance(float x, float y, Direction dir)
	{
		Entity entrance = new Entity();
		
		TransformComponent tc = (TransformComponent) entrance.addComponent(new TransformComponent());
		tc.position.set( x * 8, y * 8 );
		tc.position.add( getOffsetForDirection(dir) );
		
		GraphicsComponent gc = (GraphicsComponent) entrance.addComponent(new GraphicsComponent());
		gc.drawElement = Gfx.entranceSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		
		AnimationComponent ac = (AnimationComponent) entrance.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.entranceAnims[getIndexForDirection(dir)];
		
		engine.addEntity(entrance);
	}
	
	private Vector2 getOffsetForDirection(Direction dir) {
		Vector2 offset = new Vector2(0,0);
		switch(dir)
		{
		case TOP:
			offset.x = 4;
			offset.y = 7.5f;
			break;
		case LEFT:
			offset.x = 0.5f;
			offset.y = 1;
			break;
		case RIGHT:
			offset.x = 7.5f;
			offset.y = 1;
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

	public void buildMapEntities(TilemapWrapper currentMap) {
		Room r = currentMap.createRoom();
		for( Link l : r.links )
		{
			if( l.isConnected() )
			{
				makeEntrance(l.relCoord.x, l.relCoord.y, l.direction);
			}
		}
	}

}
