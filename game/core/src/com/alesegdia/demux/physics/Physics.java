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
	
	public Body createPlayerBody(float x, float y) {
		Body b = createBody(x * GameConfig.PIXELS_TO_METERS, y* GameConfig.PIXELS_TO_METERS, true);
		//CircleShape cs = new CircleShape();
		//cs.setRadius(7.41f * GameConfig.PIXELS_TO_METERS);
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(0.14f, 0.35f);
		createFixture(b, cs, CollisionLayers.CATEGORY_PLAYERPHYSIC, CollisionLayers.MASK_PLAYERPHYSIC, CollisionLayers.GROUP_PLAYERPHYSIC, 1f, 0f, 0f);
		createFixture(b, cs, CollisionLayers.CATEGORY_PLAYERLOGIC, CollisionLayers.MASK_PLAYERLOGIC, CollisionLayers.GROUP_PLAYERPHYSIC, 1f, 0f, 0f);
		cs.dispose();
		return b;

	}

	public Body createLinkBody(float x, float y) {
		Body b = createBody(x * GameConfig.PIXELS_TO_METERS, y* GameConfig.PIXELS_TO_METERS, false);
		//CircleShape cs = new CircleShape();
		//cs.setRadius(7.41f * GameConfig.PIXELS_TO_METERS);
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(0.1f, 0.1f);
		createFixture(b, cs, CollisionLayers.CATEGORY_LINK, CollisionLayers.MASK_LINK, CollisionLayers.GROUP_LINK, 1f, 0f, 0f, true);
		cs.dispose();
		return b;

	}

	public Body createBlockedLinkBody(float x, float y, float sx, float sy) {
		Body b = createBody(x * GameConfig.PIXELS_TO_METERS, y* GameConfig.PIXELS_TO_METERS, false);
		//CircleShape cs = new CircleShape();
		//cs.setRadius(7.41f * GameConfig.PIXELS_TO_METERS);
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(sx, sy);
		createFixture(b, cs, CollisionLayers.CATEGORY_MAP, CollisionLayers.MASK_MAP, CollisionLayers.GROUP_MAP, 1f, 0f, 0f);
		cs.dispose();
		return b;
	}

	public void Dispose() {
		Array<Body> bodies = new Array<Body>();
		this.world.getBodies(bodies);
		for( Body b : bodies ) {
			b.getWorld().destroyBody(b);
		}
	}

	public World world() {
		return this.world;
	}


}
