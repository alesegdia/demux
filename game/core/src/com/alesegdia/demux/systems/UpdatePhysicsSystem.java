package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.GameConfig;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.TransformComponent;

public class UpdatePhysicsSystem extends EntitySystem {

	public UpdatePhysicsSystem() {
		super(TransformComponent.class, PhysicsComponent.class);
	}
	
	@Override
	public void process(Entity e) {
		PhysicsComponent pc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
		TransformComponent posc = (TransformComponent) e.getComponent(TransformComponent.class);
		posc.position.set(pc.body.getPosition());
		posc.position.x = (Math.round(posc.position.x * GameConfig.METERS_TO_PIXELS) / GameConfig.METERS_TO_PIXELS + posc.offset.x);
		posc.position.y = (Math.round(posc.position.y * GameConfig.METERS_TO_PIXELS) / GameConfig.METERS_TO_PIXELS + posc.offset.y);
		posc.angle = (float) Math.toDegrees(pc.body.getAngle());
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
}
