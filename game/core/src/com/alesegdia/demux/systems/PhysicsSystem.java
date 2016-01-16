package com.alesegdia.demux.systems;

import java.util.ArrayList;
import java.util.List;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.physics.CollisionLayers;
import com.alesegdia.demux.components.PickupEffectComponent;
import com.alesegdia.demux.systems.PhysicsSystem.ICollisionCallback;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.RoomLinkComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class PhysicsSystem extends EntitySystem implements ContactListener {

	abstract class ICollisionCallback {
		public abstract void startCollision(Contact contact, Body b1, Body b2, Vector2 normal);
		public void postsolveCollision(Contact contact, Body b1, Body b2, Vector2 normal, ContactImpulse impulse) {
			// TODO Auto-generated method stub
			
		}
		public abstract void endCollision(Contact c, Body b1, Body b2);
		public short B1_CATEGORY;
		public short B2_CATEGORY;
		public void setCategories( short c1, short c2 ) {
			this.B1_CATEGORY = c1; this.B2_CATEGORY = c2;
		}
		public void presolveCollision(Contact contact, Body b1, Body b2, Vector2 normal) {
			// TODO Auto-generated method stub
			
		}
	}

	List<ICollisionCallback> callbacks = new ArrayList<ICollisionCallback>();
	
	public PhysicsSystem() {
		super(PhysicsComponent.class);
		
		callbacks.add(new ICollisionCallback(){
			{ setCategories( CollisionLayers.CATEGORY_PLAYERPHYSIC, CollisionLayers.CATEGORY_MAP ); }
			
			// TODO: use also 3 raycasts to check for grounded
			@Override
			public void startCollision(Contact c, Body player, Body map, Vector2 normal) {
				Entity e = (Entity) player.getUserData();
				PlayerComponent plc = (PlayerComponent) e.getComponent(PlayerComponent.class);
				if( Math.abs(-1 - normal.y) < 0.1 ) {
					PhysicsComponent pc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
					plc.jumping = false;
					pc.grounded = true;
					plc.platform = map;
					plc.superJump = false;
				}
			}

			@Override
			public void endCollision(Contact c, Body player, Body map) {
				Entity e = (Entity) player.getUserData();
				PhysicsComponent pc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
				PlayerComponent plc = (PlayerComponent) e.getComponent(PlayerComponent.class);
				if( plc.platform == map ) //pc.body.getLinearVelocity().y != 0 )
				{
					plc.platform = null;
					pc.grounded = false;
					plc.jumping = true;
				}
			}
		});

		callbacks.add(new ICollisionCallback(){
			{ setCategories( CollisionLayers.CATEGORY_PLAYERLOGIC, CollisionLayers.CATEGORY_LINK ); }
			
			// TODO: use also 3 raycasts to check for grounded
			@Override
			public void startCollision(Contact c, Body player, Body link, Vector2 normal) {
				Entity playerEntity = (Entity) player.getUserData();
				Entity linkEntity = (Entity) link.getUserData();
				PlayerComponent plc = (PlayerComponent) playerEntity.getComponent(PlayerComponent.class);
				RoomLinkComponent lc = (RoomLinkComponent) linkEntity.getComponent(RoomLinkComponent.class);
				
				plc.gotoRoom = lc.link;
				plc.linkEntity = linkEntity;
			}

			@Override
			public void endCollision(Contact c, Body player, Body link) {
			}
		});
		
		callbacks.add(new ICollisionCallback() {
			{ setCategories( CollisionLayers.CATEGORY_PICKUP, CollisionLayers.CATEGORY_PLAYERLOGIC ); }
			
			@Override
			public void startCollision(Contact c, Body pickupB, Body playerB, Vector2 normal) {
				Entity pickup = (Entity) pickupB.getUserData();
				Entity player = (Entity) playerB.getUserData();
				PickupEffectComponent pec = (PickupEffectComponent) player.getComponent(PickupEffectComponent.class);
				pec.pickupsCollectedLastFrame.add(pickup);
			}

			@Override
			public void endCollision(Contact c, Body b1, Body b2) {
				// TODO Auto-generated method stub
				
			}
			
		});


	}

	
	
	@Override
	public void beginContact(Contact contact) {
		short cb1 = contact.getFixtureA().getFilterData().categoryBits;
		short cb2 = contact.getFixtureB().getFilterData().categoryBits;
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();
		Vector2 normal = contact.getWorldManifold().getNormal();

		for( ICollisionCallback icb : callbacks ) {
			
			if( HandleStartCollision(contact, cb1, cb2, b1, b2, icb, normal) ) {
				break;
			}
		}
	}

	private boolean HandleStartCollision(Contact contact, short cb1, short cb2, Body b1, Body b2, ICollisionCallback icb, Vector2 normal) {
		if( CheckCollision(cb1, cb2, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.startCollision(contact, b1, b2, normal);
			return true;
		} else if( CheckCollision(cb2, cb1, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			normal.x = -normal.x;
			normal.y = -normal.y;
			icb.startCollision(contact, b2, b1, normal);
			return true;
		}
		return false;
	}

	private boolean HandlePresolveCollision(Contact contact, short cb1, short cb2, Body b1, Body b2, ICollisionCallback icb, Vector2 normal) {
		if( CheckCollision(cb1, cb2, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.presolveCollision(contact, b1, b2, normal);
			return true;
		} else if( CheckCollision(cb2, cb1, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.presolveCollision(contact, b2, b1, normal);
			return true;
		}
		return false;
	}

	private boolean HandlePostSolveCollision(Contact contact, short cb1, short cb2, Body b1, Body b2, ICollisionCallback icb, Vector2 normal, ContactImpulse impulse) {
		if( CheckCollision(cb1, cb2, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.postsolveCollision(contact, b1, b2, normal, impulse);
			return true;
		} else if( CheckCollision(cb2, cb1, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.postsolveCollision(contact, b2, b1, normal, impulse);
			return true;
		}
		return false;
	}

	private boolean CheckCollision(short cb1, short cb2, short c1, short c2) {
		return cb1 == c1 && cb2 == c2;
	}

	@Override
	public void endContact(Contact contact) {
		short cb1 = contact.getFixtureA().getFilterData().categoryBits;
		short cb2 = contact.getFixtureB().getFilterData().categoryBits;
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();

		for( ICollisionCallback icb : callbacks ) {
			if( HandleEndCollision(contact, cb1, cb2, b1, b2, icb) ) {
				break;
			}
		}		
	}

	private boolean HandleEndCollision(Contact contact, short cb1, short cb2, Body b1, Body b2, ICollisionCallback icb) {
		if( CheckCollision(cb1, cb2, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.endCollision(contact, b1, b2);
			return true;
		} else if( CheckCollision(cb2, cb1, icb.B1_CATEGORY, icb.B2_CATEGORY) ) {
			icb.endCollision(contact, b2, b1);
			return true;
		}
		return false;
	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		short cb1 = contact.getFixtureA().getFilterData().categoryBits;
		short cb2 = contact.getFixtureB().getFilterData().categoryBits;
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();

		for( ICollisionCallback icb : callbacks ) {
			if( HandlePresolveCollision(contact, cb1, cb2, b1, b2, icb, contact.getWorldManifold().getNormal() ) ) {
				break;
			}
		}		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		short cb1 = contact.getFixtureA().getFilterData().categoryBits;
		short cb2 = contact.getFixtureB().getFilterData().categoryBits;
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();

		for( ICollisionCallback icb : callbacks ) {
			if( HandlePostSolveCollision(contact, cb1, cb2, b1, b2, icb, contact.getWorldManifold().getNormal(), impulse ) ) {
				break;
			}
		}		
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
		PhysicsComponent phc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
		phc.body.getWorld().destroyBody(phc.body);
	}


	@Override
	public void process(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
