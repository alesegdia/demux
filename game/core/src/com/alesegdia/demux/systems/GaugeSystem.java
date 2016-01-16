package com.alesegdia.demux.systems;

import com.alesegdia.demux.components.GraphicsComponent;
import com.alesegdia.demux.components.PhysicsComponent;
import com.alesegdia.demux.components.WeaponComponent;
import com.alesegdia.demux.components.GaugeComponent;
import com.alesegdia.demux.components.AttackComponent;

import com.alesegdia.demux.ecs.Entity;
import com.alesegdia.demux.ecs.EntitySystem;
import com.badlogic.gdx.Gdx;

public class GaugeSystem extends EntitySystem {

	public GaugeSystem() {
		super(GaugeComponent.class, AttackComponent.class);
	}
	
	@Override
	public void process(Entity e) {
		AttackComponent ac = (AttackComponent) e.getComponent(AttackComponent.class);
		GaugeComponent gc = (GaugeComponent) e.getComponent(GaugeComponent.class);
		WeaponComponent wc = (WeaponComponent) e.getComponent(WeaponComponent.class);
		
		if( ac.doAttack )
		{
			if( gc.currentGauge > wc.computeShootCost() )
			{
				gc.currentGauge = Math.max(0, gc.currentGauge - wc.computeShootCost());
			}
			else
			{
				ac.doAttack = false;
			}
		}

		// move to player stats
		//final float RECOVER_RATE = 5;
		final float RECOVER_RATE = 60;
		
		gc.currentGauge = Math.min(gc.currentGauge + Gdx.graphics.getDeltaTime() * gc.regenRate, gc.maxGauge);
	}

	@Override
	public void notifyEntityRemoved(Entity e) {
		// TODO Auto-generated method stub
		
	}
	

}
