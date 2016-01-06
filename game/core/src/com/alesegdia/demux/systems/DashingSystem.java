package com.alesegdia.demux.systems;

import com.alesegdia.demux.components.DashComponent;
import com.alesegdia.demux.components.LinearVelocityComponent;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.badlogic.gdx.Gdx;

public class DashingSystem extends EntitySystem {

	public DashingSystem() {
		super(LinearVelocityComponent.class, DashComponent.class);
	}
	
	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Entity e) {
		DashComponent dc = (DashComponent) e.getComponent(DashComponent.class);
		if( dc.dashTimer > 0 )
		{
			LinearVelocityComponent lvc = (LinearVelocityComponent) e.getComponent(LinearVelocityComponent.class);
			lvc.linearVelocity.set(dc.dashIntensity, lvc.linearVelocity.y);
			dc.dashTimer -= Gdx.graphics.getDeltaTime();
		}
	}

}
