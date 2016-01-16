package com.alesegdia.demux;

import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.components.PickupItemComponent;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.components.ActiveComponent;
import com.alesegdia.demux.components.AnimationComponent;
import com.alesegdia.demux.components.BulletComponent;
import com.alesegdia.demux.components.CountdownDestructionComponent;
import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.RoomLinkComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.alesegdia.demux.ecs.Engine;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.physics.CollisionLayers;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.systems.AnimationSystem;
import com.alesegdia.demux.systems.AttackTriggeringSystem;
import com.alesegdia.demux.systems.CountdownDestructionSystem;
import com.alesegdia.demux.systems.DashingSystem;
import com.alesegdia.demux.systems.DrawingSystem;
import com.alesegdia.demux.systems.FlipSystem;
import com.alesegdia.demux.systems.GaugeSystem;
import com.alesegdia.demux.systems.HumanControllerSystem;
import com.alesegdia.demux.systems.MovementSystem;
import com.alesegdia.demux.systems.PickupSystem;
import com.alesegdia.demux.systems.ShootingSystem;
import com.alesegdia.demux.systems.UpdatePhysicsSystem;
import com.alesegdia.troidgen.room.Direction;
import com.alesegdia.troidgen.room.Link;
import com.alesegdia.troidgen.room.LinkInfo;
import com.alesegdia.troidgen.room.Room;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
	BulletConfigs bulletCfgs;

	private CameraScroller scroll;

	public GameWorld( Physics physics, SpriteBatch batch, Camera cam ) {
		this.physics = physics;
		this.cam = cam;
		engine = new Engine();
		engine.addSystem(new HumanControllerSystem());
		engine.addSystem(new AttackTriggeringSystem());
		engine.addSystem(new GaugeSystem());
		engine.addSystem(new ShootingSystem());

		engine.addSystem(new AnimationSystem());
		engine.addSystem(new DashingSystem());
		engine.addSystem(new MovementSystem());

		engine.addSystem(new CountdownDestructionSystem());
		engine.addSystem(physics.physicsSystem);
		engine.addSystem(new UpdatePhysicsSystem());

		engine.addSystem(new PickupSystem());
		
		engine.addSystem(new FlipSystem());
		engine.addSystem(new DrawingSystem(batch), true);
		

		bulletCfgs = new BulletConfigs();

		instance = this;
	}
	
	public void clear()
	{
		engine.Clear();
	}

	public void resetScroller( TiledMap tm )
	{
		scroll = new CameraScroller( this.cam, tm, (TransformComponent) player.getComponent(TransformComponent.class) );
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
			player.addComponent(new PlayerComponent());
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
		
		player.addComponent(new DashComponent());
		
		if( prd.ac != null && prd.sc != null && prd.wc != null )
		{
			player.addComponent(prd.ac);
			player.addComponent(prd.sc);
			player.addComponent(prd.wc);
		}
		else
		{
			AttackComponent atc = (AttackComponent) player.addComponent(new AttackComponent());
			ShootComponent sc = (ShootComponent) player.addComponent(new ShootComponent());
			WeaponComponent wep = (WeaponComponent) player.addComponent(new WeaponComponent());
			
			atc.attackCooldown = 2f;
		
			for( int i = 0; i < wep.weaponModel.length; i++ )
			{
				wep.weaponModel[i] = new WeaponStats(i, i, i, i).makeModel(); //new WeaponComponent.WeaponModel(BulletConfigs.defaultGun);
			}
	
			// change weapon
			atc.attackCooldown = wep.weaponModel[0].rate;
			sc.bulletConfigs = wep.weaponModel[0].bulletEntries;
		}
		
		if( prd.gc != null )
		{
			player.addComponent(prd.gc);
		}
		else
		{
			GaugeComponent gac = (GaugeComponent) player.addComponent(new GaugeComponent());
			gac.maxGauge = 300;
			gac.currentGauge = 300;
		}
		
		player.addComponent(new PickupEffectComponent());
		
		if( prd.stc != null )
		{
			player.addComponent(prd.stc);
		}
		else
		{
			player.addComponent(new StaminaComponent());
		}

		engine.addEntity(player);
	}
	
	public void makeEntrance(Link l)
	{
		Entity entrance = new Entity();
		
		TransformComponent tc = (TransformComponent) entrance.addComponent(new TransformComponent());
		tc.position.set( l.relCoord.x * 8, l.relCoord.y * 8 );
		tc.position.add( DirectionUtils.GetOffsetForDirection(l.direction) );
		
		GraphicsComponent gc = (GraphicsComponent) entrance.addComponent(new GraphicsComponent());
		gc.drawElement = Gfx.entranceSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		
		AnimationComponent ac = (AnimationComponent) entrance.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.entranceAnims[DirectionUtils.GetIndexForDirection(l.direction)];
		
		RoomLinkComponent rlc = (RoomLinkComponent) entrance.addComponent(new RoomLinkComponent());
		rlc.link = l;
		
		PhysicsComponent phc = (PhysicsComponent) entrance.addComponent(new PhysicsComponent());
		Vector2 pos = new Vector2(l.relCoord.x * GameConfig.BLOCK_X, l.relCoord.y * GameConfig.BLOCK_Y);
		
		Vector2 offset = DirectionUtils.GetOffsetForDirection(l.direction);
		pos.add(offset);
		
		pos.x *= GameConfig.METERS_TO_PIXELS / 2f;
		pos.y *= GameConfig.METERS_TO_PIXELS / 2f;
		
		phc.body = physics.createLinkBody(pos.x, pos.y);
		phc.body.setUserData(entrance);
		
		switch(l.direction)
		{
		case DOWN:
			gc.offset.y = 0.25f;
			break;
		case LEFT:
			gc.offset.x = 0.25f;
			break;
		case RIGHT:
			gc.offset.x = -0.25f;
			break;
		case TOP:
			gc.offset.y = -0.25f;
			break;
		}
		
		engine.addEntity(entrance);
	}
	
	private void makeBlockEntrance(Link l) {
		Entity entrance = new Entity();
		
		TransformComponent tc = (TransformComponent) entrance.addComponent(new TransformComponent());
		tc.position.set( l.relCoord.x * 8, l.relCoord.y * 8 );
		tc.position.add( DirectionUtils.GetOffsetForDirection(l.direction) );
		
		PhysicsComponent phc = (PhysicsComponent) entrance.addComponent(new PhysicsComponent());
		Vector2 pos = new Vector2(l.relCoord.x * GameConfig.BLOCK_X, l.relCoord.y * GameConfig.BLOCK_Y);
		
		Vector2 offset = DirectionUtils.GetOffsetForDirection(l.direction);
		pos.add(offset);
		pos.x *= GameConfig.METERS_TO_PIXELS / 2f;
		pos.y *= GameConfig.METERS_TO_PIXELS / 2f;		
		
		Vector2 sz = DirectionUtils.GetSizeForDirection(l.direction);
		phc.body = physics.createBlockedLinkBody(pos.x, pos.y, sz.x, sz.y);
		phc.body.setUserData(entrance);
		
		engine.addEntity(entrance);		
	}
	
	public Entity makeBullet( float x, float y, float w, float h, Vector2 dir, boolean player, TextureRegion tr,
			float destructionTime, float power, boolean trespassingEnabled ) {
		Entity e = new Entity();

		BulletComponent bc = (BulletComponent) e.addComponent(new BulletComponent());
		bc.power = power;
		bc.trespassingEnabled = trespassingEnabled;
		
		GraphicsComponent gc = (GraphicsComponent) e.addComponent(new GraphicsComponent());
		gc.drawElement = tr;
		gc.sprite = new Sprite(gc.drawElement);
		gc.allowFlip = false;
		
		TransformComponent posc = (TransformComponent) e.addComponent(new TransformComponent());
		posc.position = new Vector2(x,y);
		posc.offset.x = 0;
		posc.offset.y = 0;
		PhysicsComponent phc = (PhysicsComponent) e.addComponent(new PhysicsComponent());
		short cat, mask, group;
		if( player ) {
			cat = CollisionLayers.CATEGORY_PLBULLETS;
			mask = CollisionLayers.MASK_PLBULLETS;
			group = CollisionLayers.GROUP_PLBULLETS;
		} else {
			cat = CollisionLayers.CATEGORY_ENBULLETS;
			mask = CollisionLayers.MASK_ENBULLETS;
			group = CollisionLayers.GROUP_ENBULLETS;
		}
		phc.body = physics.createBulletBody(x, y, w/4, h/4, cat, mask, group);
		phc.body.setTransform(x, y, (float) Math.toRadians(dir.angle()));
		phc.body.setUserData(e);
		
		phc.body.setLinearVelocity(dir);
		
		LinearVelocityComponent lvc = (LinearVelocityComponent) e.addComponent(new LinearVelocityComponent());		
		lvc.speed.set(0.5f,0);
		lvc.linearVelocity.set(dir);

		CountdownDestructionComponent cdc = (CountdownDestructionComponent) e.addComponent(new CountdownDestructionComponent());
		cdc.timeToLive = destructionTime;
		
		engine.addEntity(e);
		
		return e;
	}
	
	public Entity makePickup( float x, float y, PickupType pt ) {
		Entity e = new Entity();
		TransformComponent tc = (TransformComponent) e.addComponent(new TransformComponent());
		PhysicsComponent phc = (PhysicsComponent) e.addComponent(new PhysicsComponent());
		GraphicsComponent gc = (GraphicsComponent) e.addComponent(new GraphicsComponent());
		AnimationComponent ac = (AnimationComponent) e.addComponent(new AnimationComponent());
		phc.body = physics.createPickupBody(x, y, 2);
		PickupItemComponent pic = (PickupItemComponent) e.addComponent(new PickupItemComponent());
		pic.pickupType = pt;
		phc.body.setUserData(e);
		switch( pt ) {
		case HEAL:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.healPickupAnim;
			break;
		case INC_STAMINA:
			break;
		case TRI_MOD:
			break;
		case INC_SP:
			break;
		default:
			break;
		}
		return engine.addEntity(e);
	}
	
	public Entity makeHorizontalBullet( float x, float y, float w, float h, float speed, boolean player, TextureRegion tr, boolean flipX, float dt, float power, boolean trespassingEnabled ) {
		Entity e = makeBullet(x, y, w, h, new Vector2(speed * (flipX ? -1 : 1), 0), player, tr, dt, power, trespassingEnabled);
		return e;
	}

	public void setCam() {
		cam.position.x = playerPositionComponent.position.x;
		cam.position.y = playerPositionComponent.position.y;
		this.scroll.step();
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
