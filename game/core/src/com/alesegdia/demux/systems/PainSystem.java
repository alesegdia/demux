package com.alesegdia.demux.systems;

import com.alesegdia.demux.components.HealthComponent;
import com.alesegdia.demux.components.PainComponent;
import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;

public class PainSystem extends EntitySystem {

	public PainSystem() {
		super(PainComponent.class, HealthComponent.class);
	}
	
	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(Entity e) {
		PainComponent pc = (PainComponent) e.getComponent(PainComponent.class);
		HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class);
		hc.currentHP -= pc.lastFrameDamage;
		pc.lastFrameDamage = 0;
		if( hc.currentHP <= 0 )
		{
			e.isDead = true;
		}
	}

}
