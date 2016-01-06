package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.assets.Gfx;
import com.alesegdia.demux.components.AnimationComponent;
import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.PlayerComponent;
import com.alesegdia.demux.components.ShadowBufferEntry;
import com.alesegdia.demux.components.TransformComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class HumanControllerSystem extends EntitySystem {

	public HumanControllerSystem() {
		super(PlayerComponent.class, PhysicsComponent.class,
			  LinearVelocityComponent.class, AnimationComponent.class,
			  GraphicsComponent.class);
	}
	
	@Override
	public void process(Entity e) {
		PhysicsComponent phc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
		LinearVelocityComponent lvc = (LinearVelocityComponent) e.getComponent(LinearVelocityComponent.class);
		AnimationComponent ac = (AnimationComponent) e.getComponent(AnimationComponent.class);
		PlayerComponent plc = (PlayerComponent) e.getComponent(PlayerComponent.class);
		GraphicsComponent gc = (GraphicsComponent) e.getComponent(GraphicsComponent.class);
		
		if( phc.grounded ){
			if( Math.abs(phc.body.getLinearVelocity().x) > 0 ) {
				ac.currentAnim = Gfx.playerWalk;
			} else {
				ac.currentAnim = Gfx.playerStand;
			}
		} else {
			if( plc.jumping ) {
				if( phc.body.getLinearVelocity().y > 0.01 ) {
					ac.currentAnim = Gfx.playerJumpUp;
				} else {
					ac.currentAnim = Gfx.playerJumpDown;
				}
			}
		}
		
		int dx = 0; int dy = 0;
		float prevYlinear = phc.body.getLinearVelocity().y;
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx = -1;
		} else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx = 1;
		}

		
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
		{
			if( !phc.grounded )
			{
				plc.jumping = false;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.UP))
		{
			dy = 1;
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if( true ) { //phc.grounded ) {
				plc.jumping = true;
				lvc.doCap[1] = false;
				prevYlinear = 6;

			}
		}
		
		if( plc.jumping ) {
			lvc.doCap[1] = false;
		}

		lvc.linearVelocity.x = dx * 5 * lvc.speed.x;
		lvc.linearVelocity.y = prevYlinear;
		phc.body.setGravityScale(1);
		
		plc.isPressingDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
		
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
	}
	
}
