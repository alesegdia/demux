package com.alesegdia.demux.systems;

import com.alesegdia.demux.components.AttackComponent;
import com.alesegdia.demux.components.TransformComponent;
import com.alesegdia.demux.components.ShootComponent;
import com.alesegdia.demux.components.ActiveComponent;
import com.alesegdia.demux.components.ShootComponent.BulletEntry;
import com.alesegdia.demux.components.ShootComponent.BulletModel;
import com.alesegdia.demux.components.SineMovementComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ShootingSystem extends EntitySystem {

	public ShootingSystem() {
		super(ShootComponent.class, TransformComponent.class, GraphicsComponent.class,
				AttackComponent.class, ActiveComponent.class);
	}
	
	@Override
	public void notifyEntityRemoved(Entity e) {
		
	}

	@Override
	public void process(Entity e) {
		ShootComponent sc = (ShootComponent) e.getComponent(ShootComponent.class);
		TransformComponent pc = (TransformComponent) e.getComponent(TransformComponent.class);
		AttackComponent ac = (AttackComponent) e.getComponent(AttackComponent.class);
		GraphicsComponent gc = (GraphicsComponent) e.getComponent(GraphicsComponent.class);
		ActiveComponent actc = (ActiveComponent) e.getComponent(ActiveComponent.class);
		
		if( ac.doAttack && actc.isActive ) {
			ac.attackedLastFrame = true;
			boolean flip = false;
			if( ac.forceFace != 0 ) {
				flip = (ac.forceFace == 1 ? true : false);
			} else {
				flip = !gc.flipX;
			}
			for( BulletEntry be : sc.bulletConfigs ) {
				// 	( float x, float y, float w, float h, float speed, boolean player, TextureRegion tr, boolean flipX ) {
				BulletModel bm = be.bm;//sc.bulletModel;
				Entity b;
				if( bm.horizontal ) {
					b = GameWorld.instance.makeHorizontalBullet(
								pc.position.x + be.origin.x * (flip?1:-1),
								pc.position.y + be.origin.y,
								bm.w, bm.h, bm.speed, bm.isPlayer, bm.tr, flip, bm.destructionTime, bm.power,
								bm.trespassingEnabled);
				} else {
					Vector2 dir;
					if( !bm.isPlayer ) {
						TransformComponent plpc = GameWorld.playerPositionComponent;
						dir = new Vector2(plpc.position).sub(pc.position).nor().scl(bm.speed);
					} else {
						dir = new Vector2(bm.dir);
						dir.nor();
						dir.scl(bm.speed);
						if( flip ) {
							dir.x *= -1;
						}
					}
					b = GameWorld.instance.makeBullet(
							pc.position.x + be.origin.x * (flip?1:-1),
							pc.position.y + be.origin.y,
							bm.w, bm.h, dir, bm.isPlayer, bm.tr, bm.destructionTime, bm.power, bm.trespassingEnabled);
					b.name = "lebala";
				}
				if( bm.sinegun ) {
					System.out.println("SINEGUN!");
					SineMovementComponent smc = (SineMovementComponent) b.addComponent(new SineMovementComponent());
					smc.baseY = pc.position.y + be.origin.y;
				}
				
				GameWorld.instance.addToEngine(b);

			}
		}
	}

}
