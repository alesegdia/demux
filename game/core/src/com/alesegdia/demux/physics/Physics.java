package com.alesegdia.demux.physics;

import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.systems.PhysicsSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Physics {

	World world;
	Box2DDebugRenderer debugRenderer;
	private float accumulator = 0;
	private static final float TIME_STEP = 1/60.f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	
	
	public PhysicsSystem physicsSystem;
	
	public Physics() {
		world = new World(new Vector2(0, -10f), true);
		physicsSystem = new PhysicsSystem();
		world.setContactListener(this.physicsSystem);
		debugRenderer = new Box2DDebugRenderer();
	}
	
	public void step(float deltaTime) {

	    accumulator += deltaTime;
	    while (accumulator >= TIME_STEP) {
	        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	        accumulator -= TIME_STEP;
	    }
	    //world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}
	
	public void render(Camera cam) {
		debugRenderer.render(world, cam.combined);
	}
	
	Body createBody(float x, float y, boolean dynamic) {
		BodyDef bodyDef = new BodyDef();
		if( dynamic ) {
			bodyDef.type = BodyType.DynamicBody;
		} else {
			bodyDef.type = BodyType.StaticBody;
		}
		bodyDef.position.set(x, y);
		Body b = world.createBody(bodyDef);
		b.setFixedRotation(true);
		return b;
	}
	
	public Fixture createFixture(Body body, Shape shape, short catbits, short maskbits, short group, float density, float friction, float restitution ) {
		return createFixture(body, shape, catbits, maskbits, group, density, friction, restitution, false);
	}
	
	public Fixture createFixture(Body body, Shape shape, short catbits, short maskbits, short group, float density, float friction,
			float restitution, boolean isSensor ) {
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		fd.filter.categoryBits = catbits;
		fd.filter.maskBits = maskbits;
		fd.filter.groupIndex = group;
		fd.isSensor = isSensor;
		return body.createFixture(fd);
	}
	
	public Body createCircleBody(float x, float y, float radius, short catbits, short maskbits, short group, boolean dynamic) {
		Body b = createBody(x * GameConfig.PIXELS_TO_METERS, y* GameConfig.PIXELS_TO_METERS,dynamic);
		CircleShape cs = new CircleShape();
		cs.setRadius(radius * GameConfig.PIXELS_TO_METERS);
		createFixture(b, cs, catbits, maskbits, group, 1f, 0f, 0f);
		cs.dispose();
		return b;
	}
	
	public Body createRectBody(float x, float y, float w, float h, short cat, short mask, short group, boolean dyn) {
		return createRectBody(x,y,w,h,cat,mask,group,dyn,false);		
	}
	
	public Body createRectBody(float x, float y, float w, float h, short catbits, short maskbits, short group, boolean dynamic, boolean isSensor) {
		Body b = createBody(x,y,dynamic);
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(w, h);
		createFixture(b, ps, catbits, maskbits, group, 0, 0, 0, isSensor);
		ps.dispose();
		return b;
	}
	
	public Body createEnemyBody(float x, float y) {
		return createCircleBody(x, y, 10, CollisionLayers.CATEGORY_ENEMYPHYSIC, CollisionLayers.MASK_ENEMYPHYSIC, CollisionLayers.GROUP_ENEMYPHYSIC, true);
	}

	public Body createPlayerBody(float x, float y) {
		Body b = createBody(x * GameConfig.PIXELS_TO_METERS, y* GameConfig.PIXELS_TO_METERS, true);
		//CircleShape cs = new CircleShape();
		//cs.setRadius(7.41f * GameConfig.PIXELS_TO_METERS);
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(0.14f, 0.3f);
		createFixture(b, cs, CollisionLayers.CATEGORY_PLAYERPHYSIC, CollisionLayers.MASK_PLAYERPHYSIC, CollisionLayers.GROUP_PLAYERPHYSIC, 1f, 0f, 0f);
		createFixture(b, cs, CollisionLayers.CATEGORY_PLAYERLOGIC, CollisionLayers.MASK_PLAYERLOGIC, CollisionLayers.GROUP_PLAYERPHYSIC, 1f, 0f, 0f);
		cs.dispose();
		return b;

	}
	
	public Body createBulletBody( float x, float y, float w, float h, short cat, short mask, short group ) {
		return createDirectionalBullet(x,y,w,h,0,cat,mask,group);
	}
	
	public Body createDirectionalBullet( float x, float y, float w, float h, float angle, short cat, short mask, short group ) {
		Body b = createBody(x, y, true);
		b.setBullet(true);
		CircleShape cs = new CircleShape();
		cs.setRadius(w * GameConfig.PIXELS_TO_METERS);
		/*
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(w * GameConfig.PIXELS_TO_METERS, h * GameConfig.PIXELS_TO_METERS);
		*/
		createFixture(b, cs, cat, mask, group, 0, 0, 0, true);
		b.setGravityScale(0);
		cs.dispose();
		return b;
	}
	
	public Body createPlayerBulletBody( float x, float y ) {
		return createBulletBody(x, y, 5, 5, CollisionLayers.CATEGORY_PLBULLETS, CollisionLayers.MASK_PLBULLETS, CollisionLayers.GROUP_PLBULLETS);
	}

	public Body createGroundExplosionBody( float x, float y ) {
		Body b = createBody(x, y, true);
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(5 * GameConfig.PIXELS_TO_METERS, 5 * GameConfig.PIXELS_TO_METERS);
		createFixture(b, ps, CollisionLayers.CATEGORY_PLBULLETS, CollisionLayers.MASK_PLBULLETS, CollisionLayers.MASK_PLBULLETS, 0, 0, 0, true);
		b.setGravityScale(0);
		ps.dispose();
		return b;
	}
	
	public Body createEnemyBody2( float x, float y, float w, float h ) {
		Body b = createBody(x, y, true);
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(w * GameConfig.PIXELS_TO_METERS, h * GameConfig.PIXELS_TO_METERS);
		createFixture(b, ps, CollisionLayers.CATEGORY_ENEMYPHYSIC, CollisionLayers.MASK_ENEMYPHYSIC, CollisionLayers.GROUP_ENEMYPHYSIC, 1f, 0, 0, false);
		createFixture(b, ps, CollisionLayers.CATEGORY_ENEMYLOGIC, CollisionLayers.MASK_ENEMYLOGIC, CollisionLayers.GROUP_ENEMYPHYSIC, 0.01f, 0, 0, true);
		ps.dispose();
		return b;
	}

	public Body createPickupBody(float x, float y, float s) {
		Body b = createBody(x, y, true);
		b.setGravityScale(0);
		CircleShape cs = new CircleShape();
		cs.setRadius(s * GameConfig.PIXELS_TO_METERS);
		createFixture(b, cs, CollisionLayers.CATEGORY_PICKUP, CollisionLayers.MASK_PICKUP, CollisionLayers.GROUP_PICKUP, 0, 0, 0, true);
		return b;
	}

	public Body createShopBody(float x, float y, float w, float h) {
		Body b = createRectBody(x, y, w * GameConfig.PIXELS_TO_METERS, h * GameConfig.PIXELS_TO_METERS,
				CollisionLayers.CATEGORY_SHOP, CollisionLayers.MASK_SHOP, CollisionLayers.GROUP_SHOP, false, true);
		b.setGravityScale(0);
		return b;
	}

	public Body createPortalBody(float x, float y, int w, int h) {
		Body b = createRectBody(x, y, w * GameConfig.PIXELS_TO_METERS, h * GameConfig.PIXELS_TO_METERS,
				CollisionLayers.CATEGORY_PORTAL, CollisionLayers.MASK_PORTAL, CollisionLayers.GROUP_PORTAL, false, true);
		b.setGravityScale(0);
		return b;
	}

	public void Dispose() {
		Array<Body> bodies = new Array<Body>();
		this.world.getBodies(bodies);
		for( Body b : bodies ) {
			b.getWorld().destroyBody(b);
		}
	}


}
