package com.alesegdia.demux;

import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.StaminaComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.components.PickupItemComponent;

import java.util.LinkedList;
import java.util.List;

import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.assets.TilemapWrapper;
import com.alesegdia.demux.components.AIWalkComponent;
import com.alesegdia.demux.components.ActiveComponent;
import com.alesegdia.demux.components.AnimationComponent;
import com.alesegdia.demux.components.BulletComponent;
import com.alesegdia.demux.components.CountdownDestructionComponent;
import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.HealthComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PainComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.RoomLinkComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.alesegdia.demux.components.UpgradesComponent;
import com.alesegdia.demux.ecs.Engine;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.physics.CollisionLayers;
import com.alesegdia.demux.physics.Physics;
import com.alesegdia.demux.systems.AIWalkingSystem;
import com.alesegdia.demux.systems.AnimationSystem;
import com.alesegdia.demux.systems.AttackTriggeringSystem;
import com.alesegdia.demux.systems.CountdownDestructionSystem;
import com.alesegdia.demux.systems.DashingSystem;
import com.alesegdia.demux.systems.DrawingSystem;
import com.alesegdia.demux.systems.FlipSystem;
import com.alesegdia.demux.systems.GaugeSystem;
import com.alesegdia.demux.systems.HumanControllerSystem;
import com.alesegdia.demux.systems.MovementSystem;
import com.alesegdia.demux.systems.PainSystem;
import com.alesegdia.demux.systems.PickupSystem;
import com.alesegdia.demux.systems.ShootingSystem;
import com.alesegdia.demux.systems.SineMovementSystem;
import com.alesegdia.demux.systems.UpdatePhysicsSystem;
import com.alesegdia.troidgen.restriction.RestrictionSet;
import com.alesegdia.troidgen.room.Direction;
import com.alesegdia.troidgen.room.Link;
import com.alesegdia.troidgen.room.LinkInfo;
import com.alesegdia.troidgen.room.Room;
import com.alesegdia.troidgen.util.RNG;
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

	public String notification;
	public float notificationTTL = 0f;
	
	public class Notification
	{
		public Notification(String text, int ttl) {
			this.text = text;
			this.ttl = ttl;
		}
		public String text;
		public float ttl;
	}

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


		// TODO: ver por que no funciona el sine, ver orden en asroth
		
		engine.addSystem(new CountdownDestructionSystem());
		engine.addSystem(new PainSystem());
		engine.addSystem(new AIWalkingSystem());
		engine.addSystem(physics.physicsSystem);
		engine.addSystem(new UpdatePhysicsSystem());
		engine.addSystem(new SineMovementSystem());

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
	
	public void configAlive( Entity e, float hp )
	{
		e.addComponent(new HealthComponent(hp));
		e.addComponent(new PainComponent());
	}
	
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
		
		if( prd.uc != null )
		{
			player.addComponent(prd.uc);
		}
		else
		{
			player.addComponent(new UpgradesComponent());
		}
		
		configAlive(player, 100);

		engine.addEntity(player);
	}
	
	public Entity makeSlimeEnemy(float x, float y)
	{
		Entity slime = new Entity();
		
		PhysicsComponent pc = (PhysicsComponent) slime.addComponent(new PhysicsComponent());
		pc.body = physics.createEnemyBody(x, y);
		pc.body.setUserData(slime);
		pc.grounded = false;

		LinearVelocityComponent lvc = (LinearVelocityComponent) slime.addComponent(new LinearVelocityComponent());
		
		GraphicsComponent gc = (GraphicsComponent) slime.addComponent(new GraphicsComponent());
		System.out.println(gc);
		gc.drawElement = Gfx.slimeSheet.get(0);
		gc.sprite = new Sprite(gc.drawElement);
		gc.hasShadowEffect = true;

		TransformComponent tc = (TransformComponent) slime.addComponent(new TransformComponent());
		tc.position = pc.body.getPosition();
		
		AnimationComponent ac = (AnimationComponent) slime.addComponent(new AnimationComponent());
		ac.currentAnim = Gfx.slimeAnim;
		
		AIWalkComponent aiwc = (AIWalkComponent) slime.addComponent(new AIWalkComponent());
		aiwc.walkingLeft = RNG.rng.nextBoolean();

		configAlive(slime, 100);

		engine.addEntity(slime);

		return slime;
	}
	
	public boolean lvl1Opened()
	{
		UpgradesComponent uc = (UpgradesComponent) player.getComponent(UpgradesComponent.class);
		if( uc == null )
		{
			return false;
		}
		return uc.hasDash && uc.hasSJump;
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

		if( l.connectedRoom.rinfo.restriction.equals(new RestrictionSet(4, true, false, false, false)) && !lvl1Opened() )
		{
			ac.currentAnim = Gfx.entranceAnims[DirectionUtils.GetIndexForDirection(l.direction)];
		}
		else
		{	
			ac.currentAnim = Gfx.entranceOpenedAnims[DirectionUtils.GetIndexForDirection(l.direction)];
		}
		
		
		RoomLinkComponent rlc = (RoomLinkComponent) entrance.addComponent(new RoomLinkComponent());
		rlc.link = l;
		
		PhysicsComponent phc = (PhysicsComponent) entrance.addComponent(new PhysicsComponent());
		Vector2 pos = new Vector2(l.relCoord.x * GameConfig.BLOCK_X, l.relCoord.y * GameConfig.BLOCK_Y);
		
		Vector2 offset = DirectionUtils.GetOffsetForDirection(l.direction);
		pos.add(offset);
		
		pos.x *= GameConfig.METERS_TO_PIXELS / 2f;
		pos.y *= GameConfig.METERS_TO_PIXELS / 2f;
		
		phc.body = physics.createLinkBody(pos.x, pos.y, l.direction);
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
		
		
		return e;
	}
	
	public Entity makePickup( PickupEntry pe ) {
		PickupType pt = pe.pickupType;
		
		Entity e = new Entity();
		TransformComponent tc = (TransformComponent) e.addComponent(new TransformComponent());
		PhysicsComponent phc = (PhysicsComponent) e.addComponent(new PhysicsComponent());
		GraphicsComponent gc = (GraphicsComponent) e.addComponent(new GraphicsComponent());
		AnimationComponent ac = (AnimationComponent) e.addComponent(new AnimationComponent());
		phc.body = physics.createPickupBody(pe.position.x + 0.25f, pe.position.y + 0.25f, 2);
		PickupItemComponent pic = (PickupItemComponent) e.addComponent(new PickupItemComponent());
		pic.pickupType = pt;
		pic.pickupEntry = pe;
		phc.body.setUserData(e);
		switch( pt ) {
		case HEAL:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.healPickupAnim;
			break;
		case INC_STAMINA:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.incStaminaPickupAnim;
			break;
		case TRI_MOD:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.triModAnim;
			break;
		case INC_SP:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.incSpPickupAnim;
			break;
		case ABILITY_DASH:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.dashAbilityAnim;
			break;
		case ABILITY_SJUMP:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.superJumpAbilityAnim;
			break;
		case BI_MOD:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.biModPickupAnim;
			break;
		case DEMUX_MOD:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.demuxModAnim;
			break;
		case INC_GAUGE:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.incGaugeAnim;
			break;
		case SINE_MOD:
			gc.drawElement = Gfx.pickupsSheet.get(0);
			gc.sprite = new Sprite(gc.drawElement);
			ac.currentAnim = Gfx.sineModAnim;
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

			UpgradesComponent uc = (UpgradesComponent) player.getComponent(UpgradesComponent.class);
			if( !l.isConnected() || (l.connectedRoom.rinfo.restriction.equals(new RestrictionSet(4, true, false, false, false)) && (!uc.hasDash || !uc.hasSJump)) )
			{
				makeBlockEntrance(l);
			}
		}
	}

	public void notify(String string) {
		this.notification = string;
		this.notificationTTL = 3f;
	}

}
