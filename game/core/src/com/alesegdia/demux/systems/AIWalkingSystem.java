package com.alesegdia.demux.systems;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.alesegdia.demux.components.AIWalkComponent;
import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.PhysicsComponent;


public class AIWalkingSystem extends EntitySystem {

	public AIWalkingSystem()
	{
		super(AIWalkComponent.class);
	}
	
	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Entity e) {
		AIWalkComponent aiwc = (AIWalkComponent) e.getComponent(AIWalkComponent.class);
		GraphicsComponent gc = (GraphicsComponent) e.getComponent(GraphicsComponent.class);
		PhysicsComponent phc = (PhysicsComponent) e.getComponent(PhysicsComponent.class);
		phc.body.setLinearVelocity(aiwc.walkingSpeed * (aiwc.walkingLeft ? -1 : 1), 0); //phc.body.getLinearVelocity().y);
	}

}
